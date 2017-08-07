package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.OrderStore;
import com.zy.entity.usr.User;
import com.zy.service.OrderService;
import com.zy.service.UserService;
import com.zy.vo.OrderListVo;
import com.zy.vo.OrderStoreAdminVo;
import com.zy.vo.OrderStoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.zy.util.GcUtils.formatDate;

/**
 * Author: Xuwq
 * Date: 2017/8/3.
 */
@Component
public class OrderStoreComponent {

    private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final String SIMPLE_TIME_PATTERN = "M月d日";

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    public OrderStoreAdminVo buildOrderStoreAdminVo(OrderStore orderStore) {

        OrderStoreAdminVo orderStoreAdminVo = new OrderStoreAdminVo();
        BeanUtils.copyProperties(orderStore, orderStoreAdminVo);
        orderStoreAdminVo.setCreateDate(formatDate(orderStore.getCreateDate(), TIME_PATTERN));
        String userName = userService.findRealName(orderStore.getUserId());
        User user = userService.findOne(orderStore.getUserId());
        if (user != null){
            orderStoreAdminVo.setPhone(user.getPhone());
        }
        orderStoreAdminVo.setUserName(userName);
        return orderStoreAdminVo;
    }

    public OrderStoreVo buildListVo(OrderStore orderStore) {
        OrderStoreVo orderStoreVo = new OrderStoreVo();
        BeanUtils.copyProperties(orderStore, orderStoreVo);
        orderStoreVo.setCreateDate(formatDate(orderStore.getCreateDate(), TIME_PATTERN));
        Order order = orderService.findOne(orderStore.getOrderId());
        if(order!=null){
            orderStoreVo.setSn(order.getSn());
        }
      return orderStoreVo;
    }
}
