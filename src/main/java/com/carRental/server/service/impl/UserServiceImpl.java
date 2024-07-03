package com.carRental.server.service.impl;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.carRental.common.constant.MessageConstant;
import com.carRental.common.context.BaseContext;
import com.carRental.common.exception.*;
import com.carRental.common.properties.JwtProperties;
import com.carRental.common.utils.JwtUtil;
import com.carRental.pojo.dto.*;
import com.carRental.pojo.po.Rental;
import com.carRental.pojo.po.Reservation;
import com.carRental.pojo.po.User;
import com.carRental.pojo.vo.UserLoginVO;
import com.carRental.server.mapper.UserMapper;
import com.carRental.server.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carRental.server.webSocketServer.WebSocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 曾先生
 * @since 2024-06-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private JwtProperties jwtProperties;
    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        User user = lambdaQuery().eq(User::getUsername, username)
                .one();
        if(user==null)
        {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        //m5加密
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(user.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);

        }
        //生成jwp令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .token(token)
                .id(user.getId())
                .userName(username).build();
        //违规业务提醒
        if(user.getBusinessReminder()!=null){
            webSocketServer.sendToAllClient("这是来自租赁平台的提醒:"+user.getBusinessReminder());
            if(user.getBusinessReminder().equals("您有一个订单超时违约，平台已自动取消并处罚押金")){
                user.setBusinessReminder(null);
                updateById(user);
            }
        }
        return userLoginVO;
    }

    @Override
    public void register(UserRegisterDTO userRegisterDTO) {
        if(userRegisterDTO.getUsername()==null&&userRegisterDTO.getPassword()==null){
            throw new RegisterFailedException(MessageConstant.ACCOUNT_ERROR);
        }
        User user = lambdaQuery().eq(User::getUsername, userRegisterDTO.getUsername())
                .one();
        if(user!=null){
            throw new AccountAlreadyExistsException(MessageConstant.AlREADY_EXISTS);
        }
        String password= userRegisterDTO.getPassword();
        User build = User.builder().createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .password(DigestUtils.md5DigestAsHex(password.getBytes()))
                .username(userRegisterDTO.getUsername())
                .email(userRegisterDTO.getEmail())
                .phone(userRegisterDTO.getPhone()).build();

        save(build);
    }


    public void changePassword(UserChangePasswordDTO userChangePasswordDto) {
        User user = getById(BaseContext.getCurrentId());
        String oldPassword=DigestUtils.md5DigestAsHex(userChangePasswordDto.getOldPassword().getBytes());
        if(!oldPassword.equals(user.getPassword())){
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        String newPassword=DigestUtils.md5DigestAsHex(userChangePasswordDto.getChangePassword().getBytes());
        lambdaUpdate().set(User::getPassword,newPassword)
                .set(User::getUpdatedAt,LocalDateTime.now())
                .eq(User::getId,BaseContext.getCurrentId())
                .update();
    }

    @Override
    public void userUpdate(UserDTO userDTO) {
        User user = User.builder().id(BaseContext.getCurrentId()).build();
        BeanUtils.copyProperties(userDTO,user);
        updateById(user);
    }

    @Override
    public void rechargeMoney(float money) {
        User user = getById(BaseContext.getCurrentId());
        float balance=user.getBalance()+money;
        List<Rental> rentalList = Db.lambdaQuery(Rental.class).eq(Rental::getUserId, BaseContext.getCurrentId())
                .eq(Rental::getStatus, "ongoing")
                .list();
        List<Reservation> reservationList = Db.lambdaQuery(Reservation.class).eq(Reservation::getUserId, BaseContext.getCurrentId())
                .eq(Reservation::getStatus, "valid")
                .list();
        if(balance>=0||rentalList.isEmpty()||reservationList.isEmpty()){
            lambdaUpdate().set(User::getStatus,1);
        }
        user.setBalance(balance);
        updateById(user);
    }

    @Override
    public void retrievePassword(UserRetrievePasswordDTO userRetrievePasswordDTO) {
        User user = getById(BaseContext.getCurrentId());
        if(!user.getEmail().equals(userRetrievePasswordDTO.getEmail())||!user.getPhone().equals(userRetrievePasswordDTO.getPhone())){
            throw new BaseException(MessageConstant.IDENTITY_ERROR);
        }
        if(!userRetrievePasswordDTO.getChangePassword().equals(userRetrievePasswordDTO.getConfirmPassword())){
            throw new BaseException(MessageConstant.TWO_INCONSISTENCIES);
        }
        String newPassword=DigestUtils.md5DigestAsHex(userRetrievePasswordDTO.getConfirmPassword().getBytes());
        user.setPassword(newPassword);
        updateById(user);

    }
}
