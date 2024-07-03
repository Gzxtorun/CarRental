package com.carRental.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.carRental.common.constant.MessageConstant;
import com.carRental.common.context.BaseContext;
import com.carRental.common.exception.*;
import com.carRental.pojo.dto.UserRentalCarDTO;
import com.carRental.pojo.po.Car;
import com.carRental.pojo.po.Rental;
import com.carRental.pojo.po.Reservation;
import com.carRental.pojo.po.User;
import com.carRental.pojo.query.RentalPageQuery;
import com.carRental.pojo.vo.PageVO;
import com.carRental.pojo.vo.RentalVO;
import com.carRental.server.mapper.RentalMapper;
import com.carRental.server.service.IRentalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 曾先生
 * @since 2024-06-13
 */
@Service
public class RentalServiceImpl extends ServiceImpl<RentalMapper, Rental> implements IRentalService {


    @Transactional
    @Override
    public void rentalCar(UserRentalCarDTO userRentalCarDTO) {
        User user = Db.getById(BaseContext.getCurrentId(), User.class);
        if(user==null){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        if(user.getStatus()==0){
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        //数据锁
        Car car = Db.lambdaQuery(Car.class)
                .eq(Car::getId, userRentalCarDTO.getCarId())
                .last("FOR UPDATE")
                .one();
        if(user.getBalance()<car.getCashPledge()){
            throw new InsufficientBalance(MessageConstant.INSUFFICIENT_BALANCE);
        }
        if(!car.getStatus().equals("available")){
            throw new CarLockedException(MessageConstant.CAR_LOCKED);
        }
        user.setBalance(user.getBalance()-car.getCashPledge());
        user.setStatus(0);
        car.setStatus("rented");
        Rental rental = Rental.builder().userId(BaseContext.getCurrentId())
                .carId(userRentalCarDTO.getCarId())
                .startDate(LocalDate.now())
                .rentalDays(userRentalCarDTO.getRentalDays())
                .endDate(LocalDate.now().plusDays(userRentalCarDTO.getRentalDays()))
                .totalCost(userRentalCarDTO.getRentalDays()*car.getPricePerDay())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(1).build();
        save(rental);
        Db.updateById(user);
        Db.updateById(car);

    }

    @Override
    @Transactional( noRollbackFor = InsufficientBalance.class)
    public void returnCar(Integer id) {
        Rental rental = getById(id);
        if(rental==null){
            throw new BaseException(MessageConstant.ORDER_NOT_FOUND);
        }
        Car car = Db.lambdaQuery(Car.class)
                .eq(Car::getId, rental.getCarId())
                .one();
        if(car==null){
            throw new BaseException(MessageConstant.CAR_IS_NULL);
        }

        User user = Db.lambdaQuery(User.class).eq(User::getId, rental.getUserId()).one();
        if(user==null){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        Reservation reservedCar = Db.lambdaQuery(Reservation.class).eq(Reservation::getCarId, car.getId())
                .eq(Reservation::getStatus, "valid")
                .one();
        if(reservedCar!=null){
            car.setStatus("reserved");
        }
        else{
            car.setStatus("available");
        }
        Db.updateById(car);
        //归还超时处理
        if(LocalDate.now().isAfter(rental.getEndDate())){
            Long moreDays=ChronoUnit.DAYS.between(rental.getEndDate(), LocalDate.now());
            int daysBetween = Math.toIntExact(moreDays);
            rental.setTotalCost(rental.getRentalDays()*car.getPricePerDay()+daysBetween*car.getPricePerDay()*3);
            rental.setRentalDays(rental.getRentalDays()+daysBetween);
            user.setBusinessReminder(null);
        }
        float balance= user.getBalance()+car.getCashPledge()- rental.getTotalCost();
        user.setBalance(balance);
        if(balance<0){
            user.setBalance(0.0f);
            user.setBusinessReminder("您存在欠费订单，为避免对您个人信用造成影响，请充值支付欠费订单，谢谢！");
            Db.updateById(user);
            rental.setOverdueMoney(balance);
            rental.setStatus(3);
            updateById(rental);
            TransactionAspectSupport.currentTransactionStatus().flush();
            throw new InsufficientBalance(MessageConstant.ARREARAGE_BALANCE);
        }
        user.setStatus(1);
        Db.updateById(user);
        rental.setStatus(2);
        updateById(rental);
    }

    @Override
    public PageVO<RentalVO> rentalPageQuery(RentalPageQuery rentalPageQuery) {
        //构建分页条件
        Page<Rental> page=rentalPageQuery.toMpPageDefaultSortByCreateTime();
        //分页查询
        Page<Rental> rentalPage=lambdaQuery()
                .eq(rentalPageQuery.getStatus()!=null,Rental::getStatus,rentalPageQuery.getStatus())
                .eq(Rental::getUserId,BaseContext.getCurrentId())
                .page(page);

        return PageVO.rentalOf(rentalPage);
    }

    @Override
    public PageVO<RentalVO> allRentalPageQuery(RentalPageQuery rentalPageQuery) {
        Page<Rental> page=rentalPageQuery.toMpPageDefaultSortByCreateTime();
        Page<Rental> pageList = lambdaQuery().eq(rentalPageQuery.getStatus() != null, Rental::getStatus, rentalPageQuery.getStatus())
                .page(page);
        return PageVO.rentalOf(pageList);
    }

    @Override
    @Transactional
    public void payOverdueRental(Integer rentalId) {
        Rental arrears = lambdaQuery().eq(Rental::getId, rentalId)
                .eq(Rental::getStatus, 3)
                .one();
        if(arrears==null){
            throw new BaseException(MessageConstant.ORDER_STATUS_ERROR);
        }
        User user = Db.getById(BaseContext.getCurrentId(), User.class);
        if(user.getBalance()+arrears.getOverdueMoney()<0){
            throw new InsufficientBalance(MessageConstant.ARREARAGE_BALANCE);
        }
        //可以避免null被默认为不更新
        LambdaUpdateWrapper<User> wrapper=Wrappers.lambdaUpdate(User.class);
        wrapper.eq(User::getId,BaseContext.getCurrentId())
                .set(User::getBalance,user.getBalance() + arrears.getOverdueMoney())
                .set(User::getBusinessReminder,null)
                .set(User::getStatus,1)
                        .set(User::getUpdatedAt,LocalDateTime.now());
        Db.update(wrapper);
        arrears.setStatus(2);
        arrears.setOverdueMoney(0.0f);
        arrears.setUpdatedAt(LocalDateTime.now());
        updateById(arrears);
    }


}
