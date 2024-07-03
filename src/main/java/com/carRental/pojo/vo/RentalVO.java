package com.carRental.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(description = "订单分页结果类型")
public class RentalVO {

    @ApiModelProperty("用户名Id")
    private Integer userId;

    @ApiModelProperty("汽车Id")
    private Integer carId;

    @ApiModelProperty("车型")
    private String model;

    @ApiModelProperty("租车时间")
    private LocalDate startDate;

    @ApiModelProperty("归还时间")
    private LocalDate endDate;

    @ApiModelProperty("订单状态")
    private Integer status;

    @ApiModelProperty("租赁天数")
    private Integer rentalDays;

    @ApiModelProperty("租赁总费用")
    private Float totalCost;

    @ApiModelProperty("订单创建时间")
    private LocalDateTime createdAt;


}
