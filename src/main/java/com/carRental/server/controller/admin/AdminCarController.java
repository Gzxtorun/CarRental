package com.carRental.server.controller.admin;

import com.carRental.common.result.Result;
import com.carRental.pojo.dto.CarDTO;
import com.carRental.server.service.ICarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.type.Alias;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Alias("AdminCarController")
@RequestMapping("/admin/car")
@Api(tags = "管理员端汽车管理接口")
@RequiredArgsConstructor
public class AdminCarController {
    private final ICarService iCarService;

    /**
     * 添加车型
     */
    @PostMapping("/save")
    @ApiOperation("管理员添加车型")
    @CacheEvict(value = "carCache", allEntries = true)
    public Result save(@RequestBody CarDTO carDTO){
        iCarService.saveCar(carDTO);
        return Result.success();
    }

}
