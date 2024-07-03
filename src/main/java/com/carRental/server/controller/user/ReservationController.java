package com.carRental.server.controller.user;


import com.carRental.common.result.Result;
import com.carRental.pojo.dto.UserReservesCarDTO;
import com.carRental.server.service.IReservationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 曾先生
 * @since 2024-06-13
 */
@Api(tags = "汽车预约订单管理接口")
@RequiredArgsConstructor
@RequestMapping("/user/reserves")
@RestController
public class ReservationController {
    private final IReservationService iReservationService;

    /**
     * 预约汽车
     */
    @PostMapping
    @ApiOperation("预约汽车")
    @CacheEvict(value = "carCache", allEntries = true)
    public Result reserveCar(@RequestBody UserReservesCarDTO userReservesCarDTO){
        iReservationService.reserveCar(userReservesCarDTO);
        return Result.success();
    }

    /**
     * 预约日当天租用汽车
     * @param orderId
     * @return
     */
    @PutMapping("/rentl/{id}")
    @ApiOperation("预约日当天租用汽车")
    @CacheEvict(value = "carCache", allEntries = true)
    public Result rentalReservationCar(@PathVariable("id") Integer orderId){
        iReservationService.rentalReservationCar(orderId);
        return Result.success();
    }

}
