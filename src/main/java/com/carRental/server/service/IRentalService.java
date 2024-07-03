package com.carRental.server.service;

import com.carRental.pojo.dto.UserRentalCarDTO;
import com.carRental.pojo.po.Rental;
import com.baomidou.mybatisplus.extension.service.IService;
import com.carRental.pojo.query.RentalPageQuery;
import com.carRental.pojo.vo.PageVO;
import com.carRental.pojo.vo.RentalVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 曾先生
 * @since 2024-06-13
 */
public interface IRentalService extends IService<Rental> {

    void rentalCar(UserRentalCarDTO userRentalCarDTO);

    void returnCar(Integer rentalId);

    PageVO<RentalVO> rentalPageQuery(RentalPageQuery rentalPageQuery);

    PageVO<RentalVO> allRentalPageQuery(RentalPageQuery rentalPageQuery);

    void payOverdueRental(Integer rentalId);
}
