package com.carRental.server.service;

import com.carRental.pojo.dto.UserReservesCarDTO;
import com.carRental.pojo.po.Reservation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 曾先生
 * @since 2024-06-13
 */
public interface IReservationService extends IService<Reservation> {

    void reserveCar(UserReservesCarDTO userReservesCarDTO);

    void rentalReservationCar(Integer orderId);
}
