package com.carRental.server.task;

import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.carRental.pojo.po.Car;
import com.carRental.pojo.po.Reservation;
import com.carRental.pojo.po.User;
import com.carRental.server.service.IReservationService;
import com.carRental.server.webSocketServer.WebSocketServer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional
public class ReservationTask {
    private final IReservationService iReservationService;
    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 处理预约超时订单超过预约日未提车系统自动取消预约订单
     */
    //每日凌晨1点自动取消超时订单
    @Scheduled(cron = "0 0 1 * * ?")
    public void processTimeoutOrder(){
        List<Reservation> list = iReservationService.lambdaQuery()
                .lt(Reservation::getReservationDate, LocalDate.now())
                .eq(Reservation::getStatus, "valid")
                .list();
        if(list!=null&& !list.isEmpty()){
            list.forEach(reservation -> reservation.setStatus("cancelled"));
            list.forEach(reservation -> reservation.setCancelledReason("订单超时已自动取消并处罚押金"));
            iReservationService.saveBatch(list);
            List<Integer> CarIds = list.stream().map(Reservation::getCarId).collect(Collectors.toList());
            List<Integer> UserIds = list.stream().map(Reservation::getUserId).collect(Collectors.toList());
            if( !CarIds.isEmpty()){
                // 查询符合条件的 Car 记录
                List<Car> reservedCars = Db.lambdaQuery(Car.class)
                        .in(Car::getId, CarIds)
                        .eq(Car::getStatus, "reserved")
                        .list();

                if(reservedCars != null && !reservedCars.isEmpty()){
                    reservedCars.forEach(car -> car.setStatus("available"));
                    Db.updateBatchById(reservedCars);
                }
            }
            if( !UserIds.isEmpty()){
                // 查询符合条件的 Car 记录
                List<User> userList = Db.lambdaQuery(User.class)
                        .in(User::getId,UserIds)
                        .list();

                if(userList != null && !userList.isEmpty()){
                    userList.forEach(user -> user.setBusinessReminder("您有一个订单超时违约，平台已自动取消并处罚押金"));
                    Db.updateBatchById(userList);
                }
            }
        }



    }
}
