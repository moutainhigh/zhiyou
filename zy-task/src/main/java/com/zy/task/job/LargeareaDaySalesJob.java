package com.zy.task.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.common.util.DateUtil;
import com.zy.entity.mal.Order;
import com.zy.entity.report.LargeareaDaySales;
import com.zy.entity.sys.SystemCode;
import com.zy.entity.usr.User;
import com.zy.model.query.OrderQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.LargeareaDaySalesService;
import com.zy.service.OrderService;
import com.zy.service.SystemCodeService;
import com.zy.service.UserService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by liang on 2017/9/26. 处理大区日销量报表
 */
public class LargeareaDaySalesJob implements Job {

    private Logger logger = LoggerFactory.getLogger(LargeareaDaySalesJob.class);

    @Autowired
    private LargeareaDaySalesService largeareaDaySalesService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SystemCodeService systemCodeService;

    /**
     * job 开始
     * @param jobExecutionContext
     * @throws JobExecutionException
     */

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("LargeareaDaySalesJob begin.......");
           this.disposeLargeareaDaySales();
        logger.info("LargeareaDaySalesJob end.......");
    }

    private void disposeLargeareaDaySales(){
        try {
            Date now = new Date();
            List<Order> orders = orderService.findAll(OrderQueryModel.builder().paidTimeGTE(DateUtil.getMonthData(now, 0, -1)).paidTimeLT(now).build());
            //过滤订单
            List<Order> filterOrders = orders.stream().filter(order -> order.getOrderStatus() == Order.OrderStatus.已完成
                     || order.getOrderStatus() == Order.OrderStatus.已支付 || order.getOrderStatus() == Order.OrderStatus.已发货)
                    .filter(order -> order.getSellerUserRank() == User.UserRank.V4).collect(Collectors.toList());
            Map<Long, List<Order>> orderMap = filterOrders.stream().collect(Collectors.groupingBy(Order::getUserId));

            //过滤大区非空特级
            List<User> v4Users = userService.findAll(UserQueryModel.builder().userRankEQ(User.UserRank.V4).build()).stream().filter(v -> v.getLargearea() != null).collect(Collectors.toList());
            List<SystemCode> largeAreaTypes = systemCodeService.findByType("LargeAreaType");
            Map<String, LargeareaDaySales> map = largeAreaTypes.stream().collect(Collectors.toMap(v -> v.getSystemValue(), v -> {
                LargeareaDaySales largeareaDaySales = new LargeareaDaySales();
                largeareaDaySales.setCreateTime(new Date());
                largeareaDaySales.setMonth(DateUtil.getMothNum(DateUtil.getMonthData(new Date(),0,-1)));
                largeareaDaySales.setYear(DateUtil.getYear(DateUtil.getMonthData(new Date(),0,-1)));
                largeareaDaySales.setDay(DateUtil.getDay(DateUtil.getMonthData(new Date(),0,-1)));
                largeareaDaySales.setLargeareaName(v.getSystemName());
                largeareaDaySales.setLargeareaValue(Integer.parseInt(v.getSystemValue()));
                largeareaDaySales.setSales(0);
                return largeareaDaySales;
            }));
            //处理逻辑，计算各区销量
            for (User user : v4Users){
                LargeareaDaySales largeareaDaySales = map.get(user.getLargearea()     );
                if(largeareaDaySales != null){
                    List<Order> orderList = orderMap.get(user.getId());
                    if(orderList != null ){
                        for (Order order: orderList ) {
                           largeareaDaySales.setSales(largeareaDaySales.getSales() + order.getQuantity().intValue());
                        }
                    }
                }
            }
            List<LargeareaDaySales> las = new ArrayList<>(map.values());
            //插入数据库
            for (LargeareaDaySales la : las) {
                 largeareaDaySalesService.insert(la);
            }
        } catch (ConcurrentException e) {
                try {
                    TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e1) {}
                    this.disposeLargeareaDaySales();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }
}

