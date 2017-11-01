package com.zy.task.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.entity.mal.Order;
import com.zy.entity.mergeusr.MergeUser;
import com.zy.entity.usr.User;
import com.zy.model.query.OrderQueryModel;
import com.zy.service.MergeUserService;
import com.zy.service.OrderService;
import com.zy.service.UserService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    private MergeUserService mergeUserService;


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
            List<Order> orders = orderService.findAll(OrderQueryModel.builder().productTypeEQ(2).build());
            //过滤订单
            List<Order> filterOrders = orders.stream().filter(order -> order.getOrderStatus() == Order.OrderStatus.已完成
                     || order.getOrderStatus() == Order.OrderStatus.已支付 || order.getOrderStatus() == Order.OrderStatus.已发货)
                    .collect(Collectors.toList());
            Map<Long, List<Order>> orderMap = filterOrders.stream().collect(Collectors.groupingBy(Order::getUserId));
            for (Long key : orderMap.keySet()) {
                User user = userService.findOne(key);
                MergeUser mergeUser = new MergeUser();
                mergeUser.setUserId(key);
                mergeUser.setInviterId(user.getParentId());
                mergeUser.setProductType(2);
                mergeUser.setUserRank(user.getUserRank());
                //查询直属上级
                User parent = null;
                if(user.getParentId() == null){
                    //原始关系没有上级，将新上级设置为万伟民
                    mergeUser.setParentId(10370l);
                }else {
                    do{
                        parent = userService.findOne(user.getParentId());
                    }while (parent != null && (orderMap.get(parent.getId()) == null || orderMap.get(parent.getId()).isEmpty()));
                    if(parent == null){
                        //父级团队里面没有一个转移的，将新上级设置为万伟民
                       mergeUser.setParentId(10370l);
                    }else {
                       mergeUser.setParentId(parent.getId());
                    }
                }
                User.UserRank parentRank = parent == null ? null : parent.getUserRank();
                //查询新的直属特级
                User v4Parent = null;
                do{
                    v4Parent = userService.findOne(mergeUser.getParentId());
                }while (v4Parent != null && ((orderMap.get(v4Parent.getId()) == null || orderMap.get(v4Parent.getId()).isEmpty()) || v4Parent.getUserRank() != User.UserRank.V4 ));
                Long v4UserId = v4Parent == null ? null : v4Parent.getId();
                if(user.getUserRank() == User.UserRank.V4){
                    List<Order> orderList = orderMap.get(key);
                    for (Order o: orderList) {
                        o.setSellerId(1l);
                        o.setSellerUserRank(null);
                        o.setV4UserId(v4UserId);
                        orderService.modifyOrder(o);
                    }
                }else if(user.getUserRank() == User.UserRank.V3){
                    List<Order> orderList = orderMap.get(key);
                    for (Order o: orderList) {
                        o.setSellerId(mergeUser.getParentId());
                        o.setSellerUserRank(parentRank);
                        o.setV4UserId(v4UserId);
                        orderService.modifyOrder(o);
                    }
                }

                mergeUserService.create(mergeUser);
            }
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

