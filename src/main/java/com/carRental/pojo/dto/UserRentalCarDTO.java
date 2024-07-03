package com.carRental.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "用户租车时传递的数据模型")
public class UserRentalCarDTO implements Serializable {

    @ApiModelProperty(value = "汽车Id",required = true)
    private Integer carId;

    @ApiModelProperty(value = "租赁天数",required = true)
    private Integer rentalDays;
}
