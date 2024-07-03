package com.carRental.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
@ApiModel(description = "用户修改密码传递的数据模型")
public class UserChangePasswordDTO implements Serializable {

    @ApiModelProperty(value = "当前的密码",required = true)
    private String oldPassword;

    @ApiModelProperty(value = "修改后的密码",required = true)
    private String changePassword;

}
