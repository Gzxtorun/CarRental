package com.carRental.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "汽车分页结果类型")
public class CarVO implements Serializable {

    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("车型")
    private String model;

    @ApiModelProperty("租金每日")
    private float pricePerDay;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("图片")
    private String imageUrl;

    @ApiModelProperty(value = "排座")
    private Integer seats;

    @ApiModelProperty(value = "排量")
    private String displacement;

    @ApiModelProperty(value = "续航")
    private String rangeKm;

    @ApiModelProperty(value = "能源类型")
    private String type;


}
