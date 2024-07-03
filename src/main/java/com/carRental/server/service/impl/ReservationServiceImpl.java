package com.carRental.server.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.carRental.common.constant.MessageConstant;
import com.carRental.common.context.BaseContext;
import com.carRental.common.exception.AccountLockedException;
import com.carRental.common.exception.BaseException;
import com.carRental.common.exception.CarLockedException;
import com.carRental.common.exception.InsufficientBalance;
import com.carRental.pojo.dto.UserReservesCarDTO;
import com.carRental.pojo.po.Car;
import com.carRental.pojo.po.Rental;
import com.carRental.pojo.po.Reservation;
import com.carRental.pojo.po.User;
import com.carRental.server.mapper.ReservationMapper;
import com.carRental.server.service.IReservationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 曾先生
 * @since 2024-06-13
 */
@Service
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservation> implements IReservationService {

    @Override
    public void reserveCar(UserReservesCarDTO userReservesCarDTO) {
        User user = Db.getById(BaseContext.getCurrentId(), User.class);
        Car car = Db.getById(userReservesCarDTO.getCarId(), Car.class);
        Reservation reservedCar = lambdaQuery().eq(Reservation::getCarId, userReservesCarDTO.getCarId())
                .eq(Reservation::getStatus, "valid")
                .one();
        if(user.getStatus()==0){
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        if(reservedCar!=null){
            throw new CarLockedException(MessageConstant.CAR_RESERVED);
        }
        if(!car.getStatus().equals("available")){
            throw new CarLockedException(MessageConstant.CAR_LOCKED);
        }
        if(user.getBalance()<car.getCashPledge())
        {
            throw new InsufficientBalance(MessageConstant.INSUFFICIENT_BALANCE);
        }
        user.setBalance(user.getBalance()-car.getCashPledge());
        user.setStatus(0);
        Db.updateById(user);
        Db.lambdaUpdate(Car.class).eq(Car::getId,userReservesCarDTO.getCarId())
                .set(Car::getStatus,"reserved")
                .update();
        Reservation reservation = Reservation.builder()
                .userId(BaseContext.getCurrentId())
                .carId(userReservesCarDTO.getCarId())
                .reservationDate(userReservesCarDTO.getReservationDate())
                .rentalDays(userReservesCarDTO.getRentalDays())
                .createdAt(LocalDateTime.now())
                .status("valid")
                .updatedAt(LocalDateTime.now())
                .build();

        // 插入或更新记录
        save(reservation);


    }

    @Override
    @Transactional
    public void rentalReservationCar(Integer orderId) {
        Reservation reservation = getById(orderId);
        if(reservation==null){
            throw new BaseException(MessageConstant.ORDER_STATUS_ERROR);
        }
        if(reservation.getStatus().equals("cancelled")){
            throw new BaseException(reservation.getCancelledReason());
        }
        if(!Objects.equals(reservation.getReservationDate(), LocalDate.now())){
            throw new BaseException(MessageConstant.ERROR_TIME);
        }
        reservation.setStatus("cancelled");
        reservation.setUpdatedAt(LocalDateTime.now());
        reservation.setCancelledReason("订单订单已兑现成功！");
        updateById(reservation);
        Car car = Db.getById(reservation.getCarId(), Car.class);
        car.setStatus("rented");
        Db.updateById(car);
        Rental rental = Rental.builder()
                .userId(reservation.getUserId())
                .carId(reservation.getCarId())
                .startDate(reservation.getReservationDate())
                .endDate(reservation.getReservationDate().plusDays(reservation.getRentalDays()))
                .status(1)
                .rentalDays(reservation.getRentalDays())
                .totalCost(car.getPricePerDay() * reservation.getRentalDays())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();
        Db.save(rental);
    }
}
