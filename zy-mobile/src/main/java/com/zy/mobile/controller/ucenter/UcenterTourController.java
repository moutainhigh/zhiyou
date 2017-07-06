package com.zy.mobile.controller.ucenter;

import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.entity.tour.Tour;
import com.zy.entity.usr.User;
import com.zy.model.Principal;
import com.zy.model.query.TourQueryModel;
import com.zy.service.TourService;
import com.zy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zy.util.GcUtils.getThumbnail;

/**
 * Created by Administrator on 2017/7/5.
 */
@RequestMapping("/tour")
@Controller
public class UcenterTourController {

    @Autowired
    private UserService userService;

    @Autowired
    private TourService tourService;

    @RequestMapping
    public String tourList(){
        return "ucenter/tour/tourList";
    }

    @RequestMapping(value = "/addInfo")
    public String addInfo(){
        return "ucenter/tour/addInfo";
    }


    /**
     * 查询 推荐人信息
     * @param principal
     * @param model
     * @return
     */
    @RequestMapping(value = "/findparentInfo")
    public String findparentInfo(Principal principal, Model model){

        User user = userService.findOne(principal.getUserId());
        if (user!=null&&user.getParentId()!=null){
            User userp = userService.findOne(user.getParentId());
            model.addAttribute("parentPhone",userp.getPhone());
        }
        return "ucenter/tour/parentInfo";
    }

    /**
     * 查询 推荐人信息 根据 手机号
     * @param phone
     * @return
     */
    @RequestMapping(value = "/findparentInfobyPhone",method = RequestMethod.POST)
    @ResponseBody
    public Result<?> findparentInfo(String phone){
        User user = userService.findByPhone(phone);
        user.setNickname(userService.findRealName(user.getId()));//放真实姓名
        if (user!=null){
            return ResultBuilder.result(user);
        }else{
            return ResultBuilder.error("推荐人不存在");
        }
    }

    /**
     * 查询所有的 路由线路信息
     * @return
     */
    @RequestMapping(value = "/findTourApple",method = RequestMethod.POST)
    public String findTourApple(String phone,Model model){
        TourQueryModel tourQueryModel = new TourQueryModel();
        tourQueryModel.setDelfage(0);
        List<Tour> tourList = tourService.findAllByTour(tourQueryModel);
        List<Tour> nowTourList = new ArrayList<Tour>();
        for (Tour tour:tourList){
            tour.setImage(getThumbnail(tour.getImage(), 640, 320));
            nowTourList.add(tour);
        }
        model.addAttribute("tourList",nowTourList);
        model.addAttribute("parentPhone",phone);
        return "ucenter/tour/tourApply";
    }

    /**
     * 跳转到 旅游报名申请表
     * @return
     */
    @RequestMapping(value = "/tourAppleTable")
    public String tourAppleTable(){
    return "ucenter/tour/tourAppleTable";
    }

    /**
     * 旅游客服 信息
     * @return
     */
    @RequestMapping(value = "/findTourUserVo")
    public String findTourUserVo(){
        return "ucenter/tour/tourDetail";
    }
}
