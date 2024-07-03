package com.carRental.server.service;

import com.carRental.pojo.dto.*;
import com.carRental.pojo.po.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.carRental.pojo.vo.UserLoginVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 曾先生
 * @since 2024-06-13
 */
public interface IUserService extends IService<User> {

    UserLoginVO login(UserLoginDTO userLoginDTO);

    void register(UserRegisterDTO userRegisterDTO);

    void changePassword(UserChangePasswordDTO userChangePasswordDto);

    void userUpdate(UserDTO userDTO);

    void rechargeMoney(float money);

    void retrievePassword(UserRetrievePasswordDTO userRetrievePasswordDTO);
}
