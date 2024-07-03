package com.carRental.server.controller.user;


import com.carRental.common.result.Result;
import com.carRental.pojo.dto.UserRentalCarDTO;
import com.carRental.pojo.query.RentalPageQuery;
import com.carRental.pojo.vo.PageVO;
import com.carRental.pojo.vo.RentalVO;
import com.carRental.server.service.IRentalService;
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
@RestController
@Api(tags = "汽车租赁订单管理接口")
@RequiredArgsConstructor
@RequestMapping("/user/rental")
public class RentalController {
    private final IRentalService iRentalService;
    /**
     * 租赁汽车
     */
    @PostMapping()
    @ApiOperation("用户租赁汽车")
    @CacheEvict(value = "carCache", allEntries = true)
    public Result rentalCar(@RequestBody UserRentalCarDTO userRentalCarDTO){
        iRentalService.rentalCar(userRentalCarDTO);
        return Result.success();
    }

    /**
     * 归还租赁
     */
    @PostMapping("/return/{rentalId}")
    @ApiOperation("用户归还汽车")
    @CacheEvict(value = "carCache", allEntries = true)
    public Result returnCar(@PathVariable("rentalId") Integer id){
        iRentalService.returnCar(id);
        return Result.success();
    }

    /**
     * 用户查询自己的租车记录
     */
    @PostMapping("/user/page")
    @ApiOperation("用户个人租车记录分页查询")
    public PageVO<RentalVO> userRentalPageQuery(@RequestBody RentalPageQuery rentalPageQuery){
        return iRentalService.rentalPageQuery(rentalPageQuery);
    }

    /**
     * 用户个人租车记录分页查询
     * @param rentalPageQuery
     * @return
     */
    @PostMapping("/page")
    @ApiOperation("所有租车记录分页查询")
    public PageVO<RentalVO> allRentalPageQuery(@RequestBody RentalPageQuery rentalPageQuery){
        return iRentalService.allRentalPageQuery(rentalPageQuery);
    }
    /**
     * 用户支付欠费订单
     */
    @PutMapping("/{rentalId}")
    @ApiOperation("用户支付欠费订单")
    public Result payOverdueRental(@PathVariable("rentalId") Integer rentalId){
        iRentalService.payOverdueRental(rentalId);
        return  Result.success();
    }
}
