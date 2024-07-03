package com.carRental.server.task;

import com.carRental.pojo.po.Rental;
import com.carRental.pojo.po.User;
import com.carRental.server.service.IRentalService;
import com.carRental.server.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class rentalTask {

    private final IRentalService iRentalService;
    private final IUserService iUserService;

    /**
     * 超时违约处理
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void rentalOverTimeTask(){

        List<Rental> list = iRentalService.lambdaQuery().lt(Rental::getEndDate, LocalDate.now())
                .eq(Rental::getStatus, "ongoing")
                .list();
        if(list!=null&& !list.isEmpty()){
            List<Integer> userIdList = list.stream().map(Rental::getUserId).collect(Collectors.toList());
            if(!userIdList.isEmpty()){
                List<User> userList = iUserService.lambdaQuery().in(User::getId,userIdList).list();
                userList.forEach(user -> user.setBusinessReminder("您有一个在租订单超时还车，超时还车租金将提升至三倍，请就近还车！避免对其它预约该车用户造成不便，谢谢！"));
            }
        }

    }
}
