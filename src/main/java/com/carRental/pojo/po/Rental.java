package com.carRental.pojo.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 曾先生
 * @since 2024-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("rental")
@Builder
@ApiModel(value="Rental对象", description="")
public class Rental implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer carId;

    private LocalDate startDate;

    private LocalDate endDate;

    //'1租用中','2已归还'，'3欠费'
    private Integer status;

    private Integer rentalDays;

    private Float totalCost;

    private Float overdueMoney;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
