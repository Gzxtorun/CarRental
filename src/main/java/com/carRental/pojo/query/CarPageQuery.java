package com.carRental.pojo.query;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(description = "车辆查询实体")
public class CarPageQuery extends PageQuery {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("车型")
    private String model;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("租金每日")
    private Float pricePerDay;

    @ApiModelProperty("押金")
    private Float cashPledge;



}
