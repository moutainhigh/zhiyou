package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.mal.OrderStore;
import com.zy.service.UserService;
import com.zy.vo.OrderStoreAdminVo;
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

    public OrderStoreAdminVo buildOrderStoreAdminVo(OrderStore orderStore) {

        OrderStoreAdminVo orderStoreAdminVo = new OrderStoreAdminVo();
        BeanUtils.copyProperties(orderStore, orderStoreAdminVo);
        orderStoreAdminVo.setCreateDate(formatDate(orderStore.getCreateDate(), TIME_PATTERN));
        String userName = userService.findRealName(orderStore.getUserId());
        orderStoreAdminVo.setUserName(userName);
        return orderStoreAdminVo;
    }
}
