package com.carRental.pojo.query;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "车辆查询实体")
public class RentalPageQuery extends PageQuery {



    @ApiModelProperty("订单状态")
    private String status;

}