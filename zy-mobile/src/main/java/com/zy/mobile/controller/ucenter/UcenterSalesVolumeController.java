package com.zy.mobile.controller.ucenter;

import com.zy.model.Principal;
import com.zy.model.query.OrderQueryModel;
import com.zy.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(value = "/salesVolume", method = RequestMethod.GET)
    public String profit(Principal principal, Model model) {
        OrderQueryModel orderQueryModel = new OrderQueryModel();
        orderQueryModel.setUserIdEQ(principal.getUserId());
        orderQueryModel.setSellerIdEQ(principal.getUserId());
        Map<String ,Object> returnMap = orderService.querySalesVolume(orderQueryModel);
        model.addAttribute("dateMap", returnMap);
        return "ucenter/salesVolume/salesVolume";
    }
}
