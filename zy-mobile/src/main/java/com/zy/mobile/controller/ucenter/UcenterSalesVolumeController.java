package com.zy.mobile.controller.ucenter;

import com.zy.common.model.query.Page;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.UserComponent;
import com.zy.entity.usr.User;
import com.zy.model.Principal;
import com.zy.model.query.OrderQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.OrderService;
import com.zy.service.UserService;
import io.gd.generator.api.query.Direction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public String salesVolume(Principal principal,String userRank, Model model) {
        OrderQueryModel orderQueryModel = new OrderQueryModel();
        orderQueryModel.setUserIdEQ(principal.getUserId());
        orderQueryModel.setSellerIdEQ(principal.getUserId());
        Map<String ,Object> returnMap = orderService.querySalesVolume(orderQueryModel);

        //我的直属下级
        UserQueryModel userQueryModel = new UserQueryModel();
        userQueryModel.setParentIdEQ(principal.getUserId());
        List<User> userList = userService.findAll(userQueryModel);

        List<User> v4List = new ArrayList<>();
        if (userRank.equals("V4")){
            //我的直属特级
            v4List = userComponent.conyteamTotalV4(principal.getUserId());
        }
        returnMap.put("v4List",v4List);
        returnMap.put("userList",userList);
        returnMap.put("userRank",userRank);
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
    public String salesVolumeDetail(Long userId,String userName, Model model) {
        OrderQueryModel orderQueryModel = new OrderQueryModel();
        orderQueryModel.setUserIdEQ(userId);
        orderQueryModel.setSellerIdEQ(userId);
        Map<String ,Object> returnMap = orderService.querySalesVolumeDetail(orderQueryModel);
        model.addAttribute("dateMap", returnMap);
        model.addAttribute("userName", userName);
        return "ucenter/salesVolume/peopleVolume";
    }


    /**
     * 查看直属下级详情
     * @param principal
     * @param model
     * @return
     */
    @RequestMapping(value = "/subordinateSubordinateDetail", method = RequestMethod.GET)
    public String subordinateSubordinateDetail(Principal principal, Model model) {
        //我的直属下级
        UserQueryModel userQueryModel = new UserQueryModel();
        userQueryModel.setParentIdEQ(principal.getUserId());
        Page<User> page= userService.findPage(userQueryModel);

        model.addAttribute("v4",page.getData().stream().filter(v -> v.getUserRank() == User.UserRank.V4).collect(Collectors.toList()));
        model.addAttribute("v3",page.getData().stream().filter(v -> v.getUserRank() == User.UserRank.V3).collect(Collectors.toList()));
        model.addAttribute("v2",page.getData().stream().filter(v -> v.getUserRank() == User.UserRank.V2).collect(Collectors.toList()));
        model.addAttribute("v1",page.getData().stream().filter(v -> v.getUserRank() == User.UserRank.V1).collect(Collectors.toList()));
        model.addAttribute("v0",page.getData().stream().filter(v -> v.getUserRank() == User.UserRank.V0).collect(Collectors.toList()));
        return "ucenter/salesVolume/findVolume";
    }

    /**
     * 异步加载  直属团队
     */
    @RequestMapping(value = "ajaxTeamDetail",method = RequestMethod.POST)
    @ResponseBody
    public Result<?> ajaxTeamDetail(Principal principal, String nameorPhone, @RequestParam(required = true) Integer pageNumber){
        Long userId = principal.getUserId();
        UserQueryModel userQueryModel = new UserQueryModel();
        userQueryModel.setParentIdEQ(userId);
        userQueryModel.setDirection(Direction.DESC);
        userQueryModel.setOrderBy("user_rank");
        if (null!=nameorPhone){
            userQueryModel.setNameorPhone("%"+nameorPhone+"%");
        }

        userQueryModel.setPageNumber(pageNumber);
        userQueryModel.setPageSize(10);
        Page<User> page= userService.findPage(userQueryModel);
        return ResultBuilder.result(page.getData());
    }

    /**
     * 查看直属特级详情
     * @param principal
     * @param model
     * @return
     */
    @RequestMapping(value = "/directlyUnderGradeDetail", method = RequestMethod.GET)
    public String directlyUnderGradeDetail(Principal principal, Model model) {
        //我的直属特级
        List<User> v4List = userComponent.conyteamTotalV4(principal.getUserId());
        model.addAttribute("v4List", v4List);
        return "ucenter/salesVolume/mustVolume";
    }
}
