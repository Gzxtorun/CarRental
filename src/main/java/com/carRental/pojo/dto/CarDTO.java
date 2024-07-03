package com.carRental.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(description = "管理员上传汽车车型的数据模型")
public class CarDTO implements Serializable {


    @ApiModelProperty(value = "车型",required = true)
    private String model;

    @ApiModelProperty(value = "租金每日",required = true)
    private float pricePerDay;

    @ApiModelProperty(value = "押金",required = true)
    private Float cashPledge;

    @ApiModelProperty(value = "图片",required = true)
    private String imageUrl;

    @ApiModelProperty(value = "排座",required = true)
    private Integer seats;

    @ApiModelProperty(value = "排量",required = true)
    private String displacement;

    @ApiModelProperty(value = "续航",required = true)
    private Float rangeKm;

    @ApiModelProperty(value = "能源类型",required = true)
    private String type;
}
