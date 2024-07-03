package com.carRental.pojo.vo;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.carRental.common.context.BaseContext;
import com.carRental.pojo.po.Car;
import com.carRental.pojo.po.Rental;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Data
@ApiModel(description = "分页结果")
public class PageVO<T> implements Serializable {
    @ApiModelProperty("总条数")
    private Long total;

    @ApiModelProperty("总页数")
    private Long pages;

    @ApiModelProperty("集合")
    private List<T> records; //当前页数据集合

    //封装VO
    public  static <PO,VO> PageVO<VO> of(Page<PO> p,Class<VO> clazz){
        PageVO<VO> dto=new PageVO<>();
        //总条数
        dto.setTotal(p.getTotal());
        //总页数
        dto.setPages(p.getPages());
        //当前页数据
        List<PO> list=p.getRecords();
        if(CollectionUtils.isEmpty(list)){
            dto.setRecords(Collections.emptyList());
            return dto;
        }
        dto.setRecords(BeanUtil.copyToList(list,clazz));
        return dto;
    }

    public  static  PageVO<RentalVO> rentalOf(Page<Rental> p){
        PageVO<RentalVO> dto=new PageVO<>();
        //总条数
        dto.setTotal(p.getTotal());
        //总页数
        dto.setPages(p.getPages());
        //当前页数据
        List<Rental> list=p.getRecords();
        if(CollectionUtils.isEmpty(list)){
            dto.setRecords(Collections.emptyList());
            return dto;
        }
        List<Car> carList = Db.lambdaQuery(Car.class).list();
        List<RentalVO> result = list.stream().map(rental -> {
            Car car = carList.stream().filter(u -> u.getId().equals(rental.getCarId())).findFirst().orElse(null);
                return new RentalVO(
                        BaseContext.getCurrentId(),
                        car.getId(),
                        car.getModel(),
                        rental.getStartDate(),
                        rental.getEndDate(),
                        rental.getStatus(),
                        rental.getRentalDays(),
                        rental.getTotalCost(),
                        rental.getCreatedAt()
                );
        }).collect(Collectors.toList());
        dto.setRecords(BeanUtil.copyToList(result,RentalVO.class));
        return dto;
    }
}
