package com.zy.mobile.controller.ucenter;

import com.zy.component.UserComponent;
import com.zy.entity.usr.User;
import com.zy.model.Principal;
import com.zy.model.query.OrderQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.OrderService;
import com.zy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * 查询销量
 * Author: Xuwq
 * Date: 2017/6/16.
 */
@RequestMapping("/u/salesVolume")
@Controller
public class UcenterSalesVolumeController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserComponent userComponent;

    /**
     * 我的销量
     * @param principal
     * @param model
     * @return
     */
    @RequestMapping(value = "/salesVolume", method = RequestMethod.GET)
    public String salesVolume(Principal principal, Model model) {
        OrderQueryModel orderQueryModel = new OrderQueryModel();
        orderQueryModel.setUserIdEQ(principal.getUserId());
        orderQueryModel.setSellerIdEQ(principal.getUserId());
        Map<String ,Object> returnMap = orderService.querySalesVolume(orderQueryModel);

        //我的直属下级
        UserQueryModel userQueryModel = new UserQueryModel();
        userQueryModel.setParentIdEQ(principal.getUserId());
        List<User> userList = userService.findAll(userQueryModel);

        //我的直属特级
        List<User> v4List = userComponent.conyteamTotalV4(principal.getUserId());
        returnMap.put("userList",userList);
        returnMap.put("v4List",v4List);
        model.addAttribute("dateMap", returnMap);
        return "ucenter/salesVolume/salesVolume";
    }

    /**
     * 查看个人进、出量详情
     * @param userId
     * @param model
     * @return
     */
    @RequestMapping(value = "/salesVolumeDetail", method = RequestMethod.GET)
    public String salesVolumeDetail(Long userId, Model model) {
        OrderQueryModel orderQueryModel = new OrderQueryModel();
        orderQueryModel.setUserIdEQ(userId);
        orderQueryModel.setSellerIdEQ(userId);
        Map<String ,Object> returnMap = orderService.querySalesVolumeDetail(orderQueryModel);
        model.addAttribute("dateMap", returnMap);
        return "ucenter/salesVolumeDetail/salesVolumeDetail";
    }
}
