package com.zy.task.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.entity.mal.Order;
import com.zy.model.Constants;
import com.zy.model.query.OrderQueryModel;
import com.zy.service.OrderService;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.zy.entity.mal.Order.OrderStatus.待支付;
import static com.zy.model.Constants.SETTING_ORDER_EXPIRE_IN_MINUTES;
import static org.apache.commons.lang3.time.DateUtils.addMinutes;
import static org.slf4j.LoggerFactory.getLogger;
import static com.zy.model.query.OrderQueryModel.builder;


/**
 * Created by freeman on 16/9/21.
 */
public class OrderCancelJob implements Job {

    private Logger logger = getLogger(OrderCancelJob.class);

    @Autowired
    private OrderService orderService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("begin..");
        //this.orderService.findAll(builder().createdTimeLT(addMinutes(new Date(), -SETTING_ORDER_EXPIRE_IN_MINUTES)).orderStatusEQ(待支付).build())
        this.orderService.findAll(builder().expiredTimeLT(new Date()).orderStatusEQ(待支付).build())
                .stream()
                .map(order -> order.getId())
                .forEach(this::cancel);
        logger.info("end..");
    }

    private void cancel(Long orderId) {
        try {
            this.orderService.cancel(orderId);
            logger.info("取消订单 {} 成功", orderId);
        } catch (ConcurrentException e) {
            try {TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e1) {}
            cancel(orderId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
