package com.carRental.server.controller.user;


import com.carRental.common.context.BaseContext;
import com.carRental.common.result.Result;
import com.carRental.pojo.dto.*;
import com.carRental.pojo.po.User;
import com.carRental.pojo.vo.UserLoginVO;
import com.carRental.server.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 曾先生
 * @since 2024-06-13
 */
@RestController
@RequestMapping("/user/user")
@Api(tags = "用户管理接口")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        UserLoginVO userLoginVO=userService.login(userLoginDTO);
        return Result.success(userLoginVO);
    }

    /**
     * 用户注册
     * @param userRegisterDTO
     * @return
     */
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result UserRegister(@RequestBody UserRegisterDTO userRegisterDTO){
            userService.register(userRegisterDTO);
            return Result.success();
    }

    /**
     * 用户修改密码
     */
    @PutMapping("/changePassword")
    @ApiOperation("用户修改密码")
    public Result changePassword(@RequestBody UserChangePasswordDTO userChangePasswordDto){
            userService.changePassword(userChangePasswordDto);
            return Result.success();

    }

    /**
     * 用户找回密码
     */

    @PostMapping("/retrievePassword")
    @ApiOperation("找回密码")
    public Result retrievePassword(@RequestBody UserRetrievePasswordDTO userRetrievePasswordDTO){
        userService.retrievePassword(userRetrievePasswordDTO);
        return Result.success();
    }
    /**
     * 用户查询账号信息
     */
    @GetMapping
    @ApiOperation("用户查询账号信息")
    public Result<User> getById(){
        User user = userService.getById(BaseContext.getCurrentId());
        user.setPassword("********");
        return Result.success(user);
    }

    /**
     * 用户修改账号信息
     */
    @PutMapping
    @ApiOperation("用户修改账号信息")
    public Result update(@RequestBody UserDTO userDTO){
        userService.userUpdate(userDTO);
        return Result.success();
    }

    /**
     * 用户充值
     * @return
     */
    @PostMapping ("/recharge/{money}")
    @ApiOperation("用户充值")
    public Result rechargeMoney(@PathVariable float money){
        userService.rechargeMoney(money);
        return Result.success();
    }

}
