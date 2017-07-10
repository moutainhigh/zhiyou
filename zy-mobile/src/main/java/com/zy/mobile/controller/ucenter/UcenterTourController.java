package com.zy.mobile.controller.ucenter;

import com.zy.common.model.query.Page;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.util.DateUtil;
import com.zy.component.TourComponent;
import com.zy.component.TourUserComponent;
import com.zy.entity.act.Report;
import com.zy.entity.tour.Tour;
import com.zy.entity.tour.TourUser;
import com.zy.entity.usr.User;
import com.zy.model.Principal;
import com.zy.model.query.TourQueryModel;
import com.zy.model.query.TourUserQueryModel;
import com.zy.service.ReportService;
import com.zy.service.TourService;
import com.zy.service.UserService;
import com.zy.vo.TourTimeVo;
import com.zy.vo.TourUserAdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @Autowired
    private TourUserComponent tourUserComponent;


    @Autowired
    private ReportService reportService;

    @Autowired
    private TourComponent tourComponent;

    @RequestMapping
    public String tourList(Principal principal , Model model){
        User user = userService.findOne(principal.getUserId());
        TourUserQueryModel tourUserQueryModel = new TourUserQueryModel();
        tourUserQueryModel.builder().userPhone(user.getPhone()).createdTime(DateUtil.getCurrYearFirst());
        Page<TourUser> page = tourService.findAll(tourUserQueryModel);
        List<TourUserAdminVo> list = page.getData().stream().map(v -> {
            return tourUserComponent.buildAdminVo(v);
        }).collect(Collectors.toList());
        model.addAttribute("tourUsers" , list);
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
     * 查询所有的 路由线路信息get请求
     * @return
     */
    @RequestMapping(value = "/findTourApple",method = RequestMethod.GET)
    public String findTourApple(String phone, RedirectAttributes redirectAttributes){
        return "forward:/tour/findTourApple?method=post&phone"+phone;
    }

    /**
     * 查询所有的 路由线路信息
     * @return
     */
    @RequestMapping(value = "/findTourApple",method = RequestMethod.POST)
    public String findTourApple(String phone,Long reporId,Model model){
        TourQueryModel tourQueryModel = new TourQueryModel();
        tourQueryModel.setDelfage(0);
        tourQueryModel.setIsReleased(true);
        List<Tour> tourList = tourService.findAllByTour(tourQueryModel);
        List<Tour> nowTourList = new ArrayList<Tour>();
        for (Tour tour:tourList){
            tour.setImage(getThumbnail(tour.getImage(), 640, 320));
            nowTourList.add(tour);
        }
        model.addAttribute("tourList",nowTourList);
        model.addAttribute("parentPhone",phone);
        model.addAttribute("reporId",reporId);
        return "ucenter/tour/tourApply";
    }

    /**
     * 跳转到 旅游报名申请表
     * @return
     */
    @RequestMapping(value = "/tourAppleTable")
    public String tourAppleTable(Long tourId,String parentPhone,Model model){
        return "ucenter/tour/tourAppleTable";
    }

    /**
     * 旅游客服 信息
     * @return
     */
    @RequestMapping(value = "/findTourDetail")
    public String findTourUserVo(Long tourId,String parentPhone,Long reporId,Model model){
        Tour tour = tourService.findTourOne(tourId);
        model.addAttribute("tour",tour);
        model.addAttribute("parentPhone",parentPhone);
        model.addAttribute("reporId",reporId);
        Report report = reportService.findOne(reporId);
        List<String> list= DateUtil.getMonthBetween(report.getCreatedTime(),DateUtil.getMonthData(report.getCreatedTime(),3,-1));
        model.addAttribute("list",list);
        return "ucenter/tour/tourDetail";
    }

    /**
     * 获取 旅游线路   时间
     * @return
     */
    @RequestMapping(value = "/ajaxTourTime",method = RequestMethod.POST)
    @ResponseBody
    public Result<?> ajaxTourTime(Long reporId,Long tourId,String tourTime){
      List<TourTimeVo> listVo =tourComponent.findTourTimeVo(reporId,tourId,tourTime);
       return ResultBuilder.result(listVo);
    }

    @RequestMapping(value = "/findTourUserVo",method = RequestMethod.POST)
    public String findTourUserVo(String phone,Long reporId,Long tourTimeid,Long tourId,Principal principal, Model model ){
        Long userId = principal.getUserId();
        model.addAttribute("tour",tourService.findTourOne(tourId));
        model.addAttribute("tourTime",tourService.findTourTimeOne(tourTimeid));
        model.addAttribute("userp",userService.findByPhone(phone));
        model.addAttribute("reporId",reporId);
        return "ucenter/tour/tourAppleTable";
    }

}
