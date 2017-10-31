package com.zy.task.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.common.util.DateUtil;
import com.zy.entity.mal.Order;
import com.zy.entity.usr.User;
import com.zy.model.query.OrderQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.OrderService;
import com.zy.service.UserService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by liang on 2017/10/31. 平移用户
 */
public class MoveUserJob implements Job {

    private Logger logger = LoggerFactory.getLogger(MoveUserJob.class);


    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;


    /**
     * job 开始
     * @param jobExecutionContext
     * @throws JobExecutionException
     */

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("MoveUserJob begin......................................................");
           this.disposeMoveUser();
        logger.info("MoveUserJob end.........................................................");
    }

    private void disposeMoveUser(){
        try {
            Date now = new Date();
            List<Order> orders = orderService.findAll(OrderQueryModel.builder().paidTimeGTE(DateUtil.getMonthData(now, 0, -1)).paidTimeLT(now).build());
            //过滤订单
            List<Order> filterOrders = orders.stream().filter(order -> order.getOrderStatus() == Order.OrderStatus.已完成
                     || order.getOrderStatus() == Order.OrderStatus.已支付 || order.getOrderStatus() == Order.OrderStatus.已发货)
                    .filter(order -> order.getSellerId()==1).collect(Collectors.toList());
            Map<Long, List<Order>> orderMap = filterOrders.stream().collect(Collectors.groupingBy(Order::getUserId));
            //过滤大区非空特级
            List<User> v4Users = userService.findAll(UserQueryModel.builder().userRankEQ(User.UserRank.V4).build()).stream().filter(v -> v.getLargearea() != null).collect(Collectors.toList());
            //过滤所有大区总裁
            List<User> allPresidentList = v4Users.stream().filter(u -> u.getIsPresident() != null && u.getIsPresident()).collect(Collectors.toList());
            Map<Integer, List<User>> presidentMap = allPresidentList.stream().collect(Collectors.groupingBy(User::getLargearea));
        } catch (ConcurrentException e) {
                try {
                    TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e1) {}
                    this.disposeMoveUser();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }
}

