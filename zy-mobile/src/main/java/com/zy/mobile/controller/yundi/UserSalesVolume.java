package com.zy.mobile.controller.yundi;

import com.zy.common.model.query.Page;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.UserComponent;
import com.zy.entity.fnc.Account;
import com.zy.entity.mal.Order;
import com.zy.entity.usr.User;
import com.zy.model.Principal;
import com.zy.model.dto.OrderSumDto;
import com.zy.model.query.OrderQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.AccountService;
import com.zy.service.OrderService;
import com.zy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author: Xuwq
 * Date: 2017-09-21.
 */
@RequestMapping("/yundi")
@Controller
public class UserSalesVolume {

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserComponent userComponent;

    /**
     * 我的钱包 订单 服务单
     * @param userId
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> index(HttpServletRequest request, HttpServletResponse response, String userId) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
        Map<String, Object> map = new HashMap<>();
        Long id =  Long.parseLong(userId);
        if (userService.findOne(id)==null){
            ResultBuilder.error("用户信息不存在");
        }
        List<Account> accounts = accountService.findByUserId(id);
        for (Account account : accounts) {
            switch (account.getCurrencyType()) {
                case 现金:
                    map.put("money", account.getAmount());
                    break;
                default:
                    break;
            }
        }

        OrderSumDto inSum = orderService.sum(OrderQueryModel.builder()
                .orderStatusIN(new Order.OrderStatus[]{Order.OrderStatus.已完成, Order.OrderStatus.已发货, Order.OrderStatus.已支付})
                .userIdEQ(id).build());

        OrderSumDto outSum = orderService.sum(OrderQueryModel.builder()
                .orderStatusIN(new Order.OrderStatus[]{Order.OrderStatus.已完成, Order.OrderStatus.已发货, Order.OrderStatus.已支付})
                .sellerIdEQ(id).build());

        map.put("inSumQuantity", inSum.getSumQuantity() == null ? 0 : inSum.getSumQuantity());
        map.put("outSumQuantity", outSum.getSumQuantity() == null ? 0 : outSum.getSumQuantity());
        return ResultBuilder.result(map);
    }

    /**
     * 我的销量
     * @return
     */
    @RequestMapping(value = "/salesVolume", method = RequestMethod.POST)
    @ResponseBody
    public Result<?>  salesVolume(HttpServletRequest request, HttpServletResponse response,String userId,String userRank) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
        Long id =  Long.parseLong(userId);
        if (userService.findOne(id)==null){
            ResultBuilder.error("用户信息不存在");
        }
        OrderQueryModel orderQueryModel = new OrderQueryModel();
        orderQueryModel.setUserIdEQ(id);
        orderQueryModel.setSellerIdEQ(id);
        Map<String, Object> returnMap = orderService.querySalesVolume(orderQueryModel);

        //我的直属下级
        UserQueryModel userQueryModel = new UserQueryModel();
        userQueryModel.setParentIdEQ(id);
        List<User> userList = userService.findAll(userQueryModel);
        userList= userList.stream().filter(user -> user.getUserRank()!=User.UserRank.V0).collect(Collectors.toList());
        List<User> v4List = new ArrayList<>();
        if (userRank.equals("V4")) {
            //我的直属特级
            v4List = userComponent.conyteamTotalV4(id);
        }
        returnMap.put("v4List", v4List);
        returnMap.put("userList", userList);
        returnMap.put("userRank", userRank);
        return ResultBuilder.result(returnMap);
    }

    /**
     * 查看个人进、出量详情
     * @param userId
     * @return
     */
    @RequestMapping(value = "/salesVolumeDetail", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> salesVolumeDetail(HttpServletRequest request, HttpServletResponse response,String userId) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
        Long id =  Long.parseLong(userId);
        if (userService.findOne(id)==null){
            ResultBuilder.error("用户信息不存在");
        }
        OrderQueryModel orderQueryModel = new OrderQueryModel();
        orderQueryModel.setUserIdEQ(id);
        orderQueryModel.setSellerIdEQ(id);
        Map<String ,Object> returnMap = orderService.querySalesVolumeDetail(orderQueryModel);
        return ResultBuilder.result(returnMap);
    }


    /**
     * 直属团队
     */
    @RequestMapping(value = "teamDetail",method = RequestMethod.POST)
    @ResponseBody
    public Result<?> ajaxTeamDetail(HttpServletRequest request, HttpServletResponse response,String userId, String nameorPhone) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
        Long id =  Long.parseLong(userId);
        if (userService.findOne(id)==null){
            ResultBuilder.error("用户信息不存在");
        }
        UserQueryModel userQueryModel = new UserQueryModel();
        userQueryModel.setParentIdEQ(id);
        if (null != nameorPhone) {
            userQueryModel.setNameorPhone("%" + nameorPhone + "%");
        }
        Page<User> page = userService.findPage1(userQueryModel);
        page.setData(page.getData().stream().filter(user -> user.getUserRank()!=User.UserRank.V0).collect(Collectors.toList()));
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        return ResultBuilder.result(map);
    }


    /**
     * 查看直属特级详情
     */
    @RequestMapping(value = "directlyUnderGradeDetail",method = RequestMethod.POST)
    @ResponseBody
    public Result<?> directlyUnderGradeDetail(HttpServletRequest request, HttpServletResponse response,String userId) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
        Long id =  Long.parseLong(userId);
        if (userService.findOne(id)==null){
            ResultBuilder.error("用户信息不存在");
        }
        //我的直属特级
        List<User> v4List = userComponent.conyteamTotalV4(id);
        Map<String, Object> map = new HashMap<>();
        map.put("v4List", v4List);
        return ResultBuilder.result(map);
    }


    /**
     * 查看直属下级详情
     * @return
     */
    @RequestMapping(value = "/subordinateSubordinateDetail", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> subordinateSubordinateDetail(HttpServletRequest request, HttpServletResponse response,String userId) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
        Map<String, Object> map = new HashMap<>();
        Long id =  Long.parseLong(userId);
        if (userService.findOne(id)==null){
            ResultBuilder.error("用户信息不存在");
        }
        //我的直属下级
        UserQueryModel userQueryModel = new UserQueryModel();
        userQueryModel.setParentIdEQ(id);
        Page<User> page= userService.findPage(userQueryModel);

        map.put("v4",page.getData().stream().filter(v -> v.getUserRank() == User.UserRank.V4).collect(Collectors.toList()));
        map.put("v3",page.getData().stream().filter(v -> v.getUserRank() == User.UserRank.V3).collect(Collectors.toList()));
        map.put("v2",page.getData().stream().filter(v -> v.getUserRank() == User.UserRank.V2).collect(Collectors.toList()));
        map.put("v1",page.getData().stream().filter(v -> v.getUserRank() == User.UserRank.V1).collect(Collectors.toList()));
        map.put("v0",page.getData().stream().filter(v -> v.getUserRank() == User.UserRank.V0).collect(Collectors.toList()));
        return ResultBuilder.result(map);
    }
}
