package com.carRental.pojo.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Api(tags = "用户预定租车的数据模型")
public class UserReservesCarDTO implements Serializable {
    @ApiModelProperty(value = "汽车Id",required = true)
    private Integer carId;

    @ApiModelProperty(value = "预约租车日",required = true)
    private LocalDate reservationDate;

    @ApiModelProperty(value = "租赁天数",required = true)
    private Integer rentalDays;
}
