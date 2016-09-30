package com.zy.component;

import com.zy.entity.mal.Order;
import com.zy.entity.mal.OrderItem;
import com.zy.entity.usr.User;
import com.zy.model.query.OrderQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.OrderItemService;
import com.zy.service.OrderService;
import com.zy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class LocalCacheComponent {

    final Logger logger = LoggerFactory.getLogger(LocalCacheComponent.class);

    private Map<Long, OrderItem> orderItemMap = new ConcurrentHashMap<>();

    private List<User> users = new ArrayList<>();

    private List<Order> orders = new ArrayList<>();

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private UserService userService;

    public List<User> getUsers() {
        return users;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public Map<Long, OrderItem> getOrderItemMap() {
        return orderItemMap;
    }

    public void refresh() {
        logger.info("refresh begin...");
        List<User> newUsers = userService.findAll(new UserQueryModel());
        List<Order> newOrders = orderService.findAll(new OrderQueryModel());
        Map<Long, OrderItem> newOrderItemMap = orderItemService.findAll().stream().collect(Collectors.toMap(v -> v.getOrderId(), v -> v));
        users = newUsers;
        orders = newOrders;
        orderItemMap = newOrderItemMap;
        logger.info("refresh end...");

    }

    @PostConstruct
    public void init() {
        refresh();
    }


}
