package com.zy.mobile.controller.ucenter;

import com.zy.common.model.query.Page;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.util.DateUtil;
import com.zy.component.TourComponent;
import com.zy.component.TourUserComponent;
import com.zy.entity.act.Policy;
import com.zy.entity.act.Report;
import com.zy.entity.sys.SystemCode;
import com.zy.entity.tour.Tour;
import com.zy.entity.tour.TourUser;
import com.zy.entity.usr.User;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.query.TourQueryModel;
import com.zy.model.query.TourUserQueryModel;
import com.zy.service.ReportService;
import com.zy.service.TourService;
import com.zy.service.UserInfoService;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import com.zy.vo.TourTimeVo;
import com.zy.vo.TourUserInfoVo;
import com.zy.vo.TourUserAdminVo;
import com.zy.vo.TourUserListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.zy.util.GcUtils.getThumbnail;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping
    public String tourList(Principal principal , Model model){
        User user = userService.findOne(principal.getUserId());
        TourUserQueryModel tourUserQueryModel = TourUserQueryModel.builder().userPhone(user.getPhone()).createdTime(DateUtil.getCurrYearFirst()).isEffect(1).build();
        Page<TourUser> page = tourService.findAll(tourUserQueryModel);
        List<TourUserListVo> list = page.getData().stream().map(v -> {
            return tourUserComponent.buildListVo(v);
        }).collect(Collectors.toList());
        List<TourUserListVo> tourUsers1 = list.stream().filter(v -> v.getAuditStatus() == 2).collect(Collectors.toList());
        List<TourUserListVo> tourUsers2 = list.stream().filter(v -> v.getAuditStatus() != 2).collect(Collectors.toList());
        //Boolean isNothing = tourUsers1.size() == 0 && tourUsers2.size() == 0 ? true : false;
        model.addAttribute("tourUsers1" , tourUsers1);
        model.addAttribute("tourUsers2" , tourUsers2);
       // model.addAttribute("isNothing" , isNothing);
        return "ucenter/tour/tourList";
    }

    @RequestMapping(value = "/addInfo")
    public String addInfo(Model model,@RequestParam Long tourUserId){
        model.addAttribute("tourUserId",tourUserId);
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
    public String findTourDetail(Long tourId,String parentPhone,Long reporId,Model model){
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

    /**
     *封装user旅游信息
     * @param phone
     * @param reporId
     * @param tourTimeid
     * @param tourId
     * @param principal
     * @param model
     * @return
     */
    @RequestMapping(value = "/findTourUserVo",method = RequestMethod.POST)
    public String findTourUserVo(String phone,Long reporId,Long tourTimeid,Long tourId,Principal principal, Model model ){
        Long userId = principal.getUserId(); //userInfoService
        model.addAttribute("userinfoVo",tourComponent.findUserInfoVo(userId));
       /* User user = userService.findOne(userId);
        user.setNickname(userService.findRealName(user.getId()));*/
        model.addAttribute("user", userService.findOne(userId));
        model.addAttribute("tour",tourService.findTourOne(tourId));
        model.addAttribute("tourTime",tourService.findTourTimeOne(tourTimeid));
        User userP =userService.findByPhone(phone);
        userP.setNickname(userService.findRealName(userP.getId()));
        model.addAttribute("userp",userP);
        model.addAttribute("reporId",reporId);
        return "ucenter/tour/tourAppleTable";
    }

    @RequestMapping(value = "/create", method = POST)
    public String create(Long tourUserId, TourUser tourUser, Principal principal, Model model, RedirectAttributes redirectAttributes) {

        tourUser.setId(tourUserId);
        tourUser.setUpdateBy(principal.getUserId());
        tourUser.setUpdateDate(new Date());
        tourUser.setAuditStatus(3);
        tourUser.setCarImages(GcUtils.getThumbnail(tourUser.getCarImages(), 750, 450));
        try {
            tourService.addCarInfo(tourUser);
        } catch (Exception e) {
            model.addAttribute("tourUser", tourUser);
            model.addAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
            return "redirect:/tour";
        }
        redirectAttributes.addFlashAttribute(ResultBuilder.ok("补充旅游信息成功"));
        return "redirect:/tour";
    }


    /**
     * 检测旅游信息参数
     * @param tourUserInfoVo
     * @param principal
     * @return
     */
    @RequestMapping(value = "/ajaxCheckParam",method = RequestMethod.POST)
    @ResponseBody
    public Result<?>ajaxCheckParam(TourUserInfoVo tourUserInfoVo,Principal principal){
       String result = tourComponent.checkParam(tourUserInfoVo);
        if (result!=null){
            return ResultBuilder.error(result);
        }else{
            return ResultBuilder.ok(null);
        }
    }
    /**
     * 提交旅游信息
     * @return
     */
    @RequestMapping(value = "/addTourforUser",method = RequestMethod.POST)
    @ResponseBody
    public Result<?>addTourforUser(TourUserInfoVo tourUserInfoVo,Principal principal){
        try {
            tourComponent.updateOrInster(tourUserInfoVo,principal.getUserId());
            return ResultBuilder.ok(null);
        }catch (Exception e){
            e.printStackTrace();
            return ResultBuilder.error(null);
        }

    }

    /**
     * 检测 是否还可以申请旅游  检测报告超过三个月  或者  一年内申请
     * @return
     */
    @RequestMapping(value = "/ajaxCheckTour",method = RequestMethod.POST)
    @ResponseBody
   public Result<?> ajaxCheckTour(String reportId){
       String result = tourComponent.checkTour(reportId);
       if (result!=null){
           return ResultBuilder.error(result);
       }else{
           return ResultBuilder.ok(null);
       }
   }


    /**
     * 检测是否提交检测报告
     * @param reportId
     * @return
     */
    @RequestMapping(value = "/ajaxCheckReport",method = RequestMethod.POST)
    @ResponseBody
    public Result<?> ajaxCheckReport(String reportId){
        String result = tourComponent.checkReport(reportId);
        if (result!=null){
            return ResultBuilder.error(result);
        }else{
            return ResultBuilder.ok(null);
        }
    }

}
