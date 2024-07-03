package com.carRental.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carRental.pojo.dto.CarDTO;
import com.carRental.pojo.po.Car;
import com.carRental.pojo.query.CarPageQuery;
import com.carRental.pojo.vo.CarVO;
import com.carRental.pojo.vo.PageVO;
import com.carRental.server.mapper.CarMapper;
import com.carRental.server.service.ICarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 曾先生
 * @since 2024-06-13
 */
@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements ICarService {

    @Override
    public void saveCar(CarDTO carDTO) {
        Car car = Car.builder().createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        BeanUtils.copyProperties(carDTO,car);
        save(car);
    }

    @Override
    public PageVO<CarVO> carPageQuery(CarPageQuery carPageQuery) {
        String model = carPageQuery.getModel();
        Float pricePerDay = carPageQuery.getPricePerDay();
        String status= carPageQuery.getStatus();
        //构建分页条件
        Page<Car> page=carPageQuery.toMpPageDefaultSortByCreateTime();
        //分页查询
        Page<Car> carPage = lambdaQuery()
                .like(model != null, Car::getModel, model)
                .eq(pricePerDay!=null, Car::getPricePerDay, pricePerDay)
                .eq(status != null, Car::getStatus, status)
                .eq(carPageQuery.getCashPledge()!=null,Car::getCashPledge,carPageQuery.getCashPledge())
                .page(page);

        return PageVO.of(carPage, CarVO.class);
    }


}
