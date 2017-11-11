package com.zy.task.job;

import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.entity.mal.Order;
import com.zy.entity.mergeusr.MergeUser;
import com.zy.entity.usr.User;
import com.zy.model.BizCode;
import com.zy.model.query.OrderQueryModel;
import com.zy.service.MergeUserService;
import com.zy.service.OrderService;
import com.zy.service.UserCheckService;
import com.zy.service.UserService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.ArrayList;
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

    @Autowired
    private UserCheckService userCheckService;


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
            Date now = new Date();
            MergeUser wan = mergeUserService.findByUserIdAndProductType(10370l, 2);
            MergeUser company = mergeUserService.findByUserIdAndProductType(1l, 2);
            User one = userService.findOne(10370l);
            if(wan == null){
                MergeUser me = new MergeUser();
                me.setUserId(10370l);
                me.setInviterId(one.getParentId());
                me.setProductType(2);
                me.setUserRank(one.getUserRank());
                me.setRegisterTime(now);
                me.setLastUpgradedTime(now);
                me.setCode(this.getCode());
                mergeUserService.create(me);
            }
            if(company == null){
                MergeUser com = new MergeUser();
                com.setUserId(1l);
                com.setProductType(2);
                com.setRegisterTime(now);
                com.setCode(this.getCode());
                mergeUserService.create(com);
            }
            //平移user
            for (Long key : orderMap.keySet()) {
                MergeUser m = mergeUserService.findByUserIdAndProductType(key, 2);
                if(m == null){
                    User user = userService.findOne(key);
                    MergeUser mergeUser = new MergeUser();
                    mergeUser.setUserId(key);
                    mergeUser.setInviterId(user.getParentId());
                    mergeUser.setProductType(2);
                    mergeUser.setUserRank(user.getUserRank());
                    mergeUser.setRegisterTime(now);
                    mergeUser.setLastUpgradedTime(now);
                    mergeUser.setCode(this.getCode());
                    if(user.getUserRank() == User.UserRank.V3){
                        //查询直属上级
                        User parent = user;
                        if(user.getParentId() != null){
                            do{
                                parent = userService.findOne(parent.getParentId());
                            }while (parent != null  && (orderMap.get(parent.getId()) == null || orderMap.get(parent.getId()).isEmpty()) && parent.getParentId() != null );
                            if(parent == null){
                                //父级团队里面没有一个转移的，将新上级设置为万伟民
                                mergeUser.setParentId(10370l);
                            }else {
                                if(orderMap.get(parent.getId()) == null || orderMap.get(parent.getId()).isEmpty()){
                                    mergeUser.setParentId(10370l);
                                }else {
                                    mergeUser.setParentId(parent.getId());
                                }
                            }
                        }
                    }else if(user.getUserRank() == User.UserRank.V4) {
                        //查询直属上级
                        User parent = user;
                        if(user.getParentId() != null){
                            do{
                                parent = userService.findOne(parent.getParentId());
                            }while (parent != null  && (orderMap.get(parent.getId()) == null || orderMap.get(parent.getId()).isEmpty()) && parent.getParentId() != null );
                            if(parent != null ){
                                if(orderMap.get(parent.getId()) != null && !orderMap.get(parent.getId()).isEmpty()){
                                    mergeUser.setParentId(parent.getId());
                                }
                            }
                        }
                    }
                    mergeUserService.create(mergeUser);
                }

            }
            List<Order> v3Orders = new ArrayList<>();
            //修改订单
            for (Long key : orderMap.keySet()) {
                MergeUser mergeUser = mergeUserService.findByUserIdAndProductType(key, 2);
                Long v4UserId = this.calculateV4UserId(mergeUser);
                //修改user的直属特级
                mergeUserService.modifyV4Id(key,v4UserId,2);
                Long sellerId = v4UserId == null ? 10370l : v4UserId;
                if(mergeUser.getUserRank() == User.UserRank.V4){
                    List<Order> orderList = orderMap.get(key);
                    for (Order o: orderList) {
                        o.setSellerId(1l);
                        o.setSellerUserRank(null);
                        o.setV4UserId(v4UserId);
                        //初始化库存
                        userCheckService.editOderStoreIn(o.getId(),o.getUserId(),2);
                        orderService.modifyOrder(o);
                    }
                }else if(mergeUser.getUserRank() == User.UserRank.V3){
                    List<Order> orderList = orderMap.get(key);
                    for (Order o: orderList) {
                        o.setSellerId(sellerId);
                        o.setSellerUserRank(User.UserRank.V4);
                        o.setV4UserId(v4UserId);
                        v3Orders.add(o);
                        orderService.modifyOrder(o);
                    }
                }
            }
            //初始化库存
            if(v3Orders != null && !v3Orders.isEmpty()){
                for(Order o : v3Orders){
                    userCheckService.editOderStoreIn(o.getId(),o.getUserId(),2);
                    userCheckService.editOrderStoreOut(o.getId(),o.getSellerId(),2);
                }
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

    private Long calculateV4UserId(MergeUser mergeUser) {
        Long parentId = mergeUser.getParentId();
        int whileTimes = 0;
        while (parentId != null) {
            if (whileTimes > 1000) {
                throw new BizException(BizCode.ERROR, "循环引用错误, mergeUser id is " + mergeUser.getUserId());
            }
            MergeUser parent = mergeUserService.findByUserIdAndProductType(parentId,2);
            if (parent.getUserRank() == User.UserRank.V4) {
                return parentId;
            }
            parentId = parent.getParentId();
            whileTimes ++;
        }
        return null;
    }


    static char[] codeSeq = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6',
            '7', '8', '9'};
    static Random random = new Random();
    private String createCode() {
        //授权书
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 14; i++) {
            s.append(codeSeq[random.nextInt(codeSeq.length)]);
        }
        return "ZY" + s.toString();
    }

    public String getCode() {

        String code = this.createCode();
        MergeUser mergeUser = mergeUserService.findBycodeAndProductType(code,2);
        int times = 0;
        while (mergeUser != null) {
            if (times > 1000) {
                throw new BizException(BizCode.ERROR, "生成code失败");
            }
            code = this.createCode();
            mergeUser = mergeUserService.findBycodeAndProductType(code,2);
            times++;
        }
        return code;
    }

}

