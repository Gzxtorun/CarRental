package com.carRental.server.service;

import com.carRental.pojo.dto.CarDTO;
import com.carRental.pojo.po.Car;
import com.baomidou.mybatisplus.extension.service.IService;
import com.carRental.pojo.query.CarPageQuery;
import com.carRental.pojo.vo.CarVO;
import com.carRental.pojo.vo.PageVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 曾先生
 * @since 2024-06-13
 */
public interface ICarService extends IService<Car> {

    void saveCar(CarDTO carDTO);

    PageVO<CarVO> carPageQuery(CarPageQuery carPageQuery);

}
