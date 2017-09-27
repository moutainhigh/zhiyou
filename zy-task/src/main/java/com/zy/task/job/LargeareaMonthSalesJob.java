package com.zy.task.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.common.util.DateUtil;
import com.zy.entity.mal.Order;
import com.zy.entity.report.LargeareaMonthSales;
import com.zy.entity.sys.SystemCode;
import com.zy.entity.usr.User;
import com.zy.model.query.LargeareaMonthSalesQueryModel;
import com.zy.model.query.OrderQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.LargeareaMonthSalesService;
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
 * Created by liang on 2017/9/26.处理大区月销量报表
 */
public class LargeareaMonthSalesJob implements Job {

    private Logger logger = LoggerFactory.getLogger(LargeareaMonthSalesJob.class);

    @Autowired
    private LargeareaMonthSalesService largeareaMonthSalesService;

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
        logger.info("LargeareaMonthSalesJob begin.......");
           this.disposeLargeareaMonthSales();
        logger.info("LargeareaMonthSalesJob end.......");
    }

    private void disposeLargeareaMonthSales(){
        try {
            Date now = new Date();
            List<Order> orders = orderService.findAll(OrderQueryModel.builder().paidTimeGTE(DateUtil.getMonthData(now, -1, 0)).paidTimeLT(now).build());
            //过滤订单
            List<Order> filterOrders = orders.stream().filter(order -> order.getOrderStatus() == Order.OrderStatus.已完成
                    || order.getOrderStatus() == Order.OrderStatus.已支付 || order.getOrderStatus() == Order.OrderStatus.已发货)
                    .filter(order -> order.getSellerUserRank() == User.UserRank.V4).collect(Collectors.toList());
            Map<Long, List<Order>> orderMap = filterOrders.stream().collect(Collectors.groupingBy(Order::getUserId));

            //过滤大区非空特级
            List<User> v4Users = userService.findAll(UserQueryModel.builder().userRankEQ(User.UserRank.V4).build()).stream().filter(v -> v.getLargearea() != null).collect(Collectors.toList());
            List<SystemCode> largeAreaTypes = systemCodeService.findByType("LargeAreaType");
            Map<String, LargeareaMonthSales> map = largeAreaTypes.stream().collect(Collectors.toMap(v -> v.getSystemName(), v -> {
                LargeareaMonthSales largeareaMonthSales = new LargeareaMonthSales();
                largeareaMonthSales.setCreateTime(new Date());
                largeareaMonthSales.setMonth(DateUtil.getMothNum(DateUtil.getMonthData(now,-1,0)));
                largeareaMonthSales.setYear(DateUtil.getYear(DateUtil.getMonthData(now,-1,0)));
                largeareaMonthSales.setLargeareaName(v.getSystemName());
                largeareaMonthSales.setLargeareaValue(Integer.parseInt(v.getSystemValue()));
                largeareaMonthSales.setSales(0);
                largeareaMonthSales.setRelativeRate(0.00);
                largeareaMonthSales.setSameRate(0.00);
                largeareaMonthSales.setTargetCount(0);
                return largeareaMonthSales;
            }));
            //处理逻辑，计算各区销量
            for (User user : v4Users){
                LargeareaMonthSales largeareaMonthSales = map.get(user.getLargearea());
                if(largeareaMonthSales != null){
                    List<Order> orderList = orderMap.get(user.getId());
                    if(orderList != null ){
                        for (Order order: orderList ) {
                            largeareaMonthSales.setSales(largeareaMonthSales.getSales() + order.getQuantity().intValue());
                        }
                    }
                }
            }
            List<LargeareaMonthSales> las = new ArrayList<>(map.values());
            //计算环比、同比
            for (LargeareaMonthSales la : las) {
                List<LargeareaMonthSales> all = largeareaMonthSalesService.findAll(LargeareaMonthSalesQueryModel.builder().largeareaNameEQ(la.getLargeareaName()).yearEQ(DateUtil.getYear(DateUtil.getMonthData(now, -2, 0))).monthEQ(DateUtil.getMothNum(DateUtil.getMonthData(now, -2, 0))).build());
                List<LargeareaMonthSales> all2 = largeareaMonthSalesService.findAll(LargeareaMonthSalesQueryModel.builder().largeareaNameEQ(la.getLargeareaName()).yearEQ(DateUtil.getYear(DateUtil.getMonthData(now, -12, 0))).monthEQ(DateUtil.getMothNum(DateUtil.getMonthData(now, -12, 0))).build());
                if( all != null && (all.size() > 0 && all.get(0).getSales() > 0)){
                    la.setRelativeRate(DateUtil.formatDouble( Double.valueOf(la.getSales()) / Double.valueOf(all.get(0).getSales()) * 100));
                }else {
                    la.setRelativeRate(100.00);
                }
                if( all2 != null && (all2.size()>0 && all2.get(0).getSales() > 0) ){
                    la.setSameRate(DateUtil.formatDouble( Double.valueOf(la.getSales()) / Double.valueOf(all2.get(0).getSales()) * 100));
                }else {
                    la.setSameRate(100.00);
                }
                largeareaMonthSalesService.insert(la);
            }
        } catch (ConcurrentException e) {
                try {
                    TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e1) {}
                    this.disposeLargeareaMonthSales();
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }
}
