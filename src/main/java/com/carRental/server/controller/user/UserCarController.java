package com.carRental.server.controller.user;



import com.carRental.common.result.Result;
import com.carRental.pojo.query.CarPageQuery;
import com.carRental.pojo.vo.CarVO;
import com.carRental.pojo.vo.PageVO;
import com.carRental.server.service.ICarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.type.Alias;
import org.springframework.cache.annotation.Cacheable;
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
@Alias("UserCarController")
@RequestMapping("/user/car")
@RequiredArgsConstructor
@Api(tags = "用户租车服务接口")
public class UserCarController {
    private  final ICarService carService;

    /**
     * 打印所有车辆信息
     * @param carPageQuery
     * @return
     */
    @PostMapping ("/page")
    @ApiOperation("汽车信息分页查询")
    @Cacheable(value = "carCache", key = "#carPageQuery.toString()")
    public PageVO<CarVO> carPageQuery(@RequestBody CarPageQuery carPageQuery){
        return carService.carPageQuery(carPageQuery);
    }

}
