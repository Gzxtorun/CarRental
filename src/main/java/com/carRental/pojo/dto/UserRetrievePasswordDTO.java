package com.carRental.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@ApiModel(description = "用户找回密码传递的数据模型")
public class UserRetrievePasswordDTO implements Serializable {

    @ApiModelProperty(value = "邮箱号",required = true)
    private String email;

    @ApiModelProperty(value = "手机号",required = true)
    private String phone;

    @ApiModelProperty(value = "修改的密码",required = true)
    private String changePassword;

    @ApiModelProperty(value = "确认修改密码",required = true)
    private String confirmPassword;

}
