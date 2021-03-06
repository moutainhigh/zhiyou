package com.zy.mobile.controller.ucenter;

import com.zy.common.model.query.Page;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.util.DateUtil;
import com.zy.component.TourComponent;
import com.zy.component.TourUserComponent;
import com.zy.entity.act.Policy;
import com.zy.entity.act.PolicyCode;
import com.zy.entity.act.Report;
import com.zy.entity.sys.SystemCode;
import com.zy.entity.tour.Tour;
import com.zy.entity.tour.TourTime;
import com.zy.entity.tour.TourUser;
import com.zy.entity.usr.User;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.query.TourQueryModel;
import com.zy.model.query.TourUserQueryModel;
import com.zy.service.*;
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

import java.util.*;
import java.util.stream.Collectors;

import static com.zy.util.GcUtils.getThumbnail;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
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

    @Autowired
    private PolicyCodeService policyCodeService;


    @RequestMapping
    public String tourList(Principal principal , Model model){
        User user = userService.findOne(principal.getUserId());
        TourUserQueryModel tourUserQueryModel = TourUserQueryModel.builder().userPhone(user.getPhone()).createdTime(DateUtil.getCurrYearFirst()).build();
        Page<TourUser> page = tourService.findListAll(tourUserQueryModel);
        List<TourUserListVo> list = page.getData().stream().map(v -> {
            return tourUserComponent.buildListVo(v);
        }).collect(Collectors.toList());
        List<TourUserListVo> tourUsers1 = list.stream().filter(v -> v.getAuditStatus() == 5).collect(Collectors.toList());
        List<TourUserListVo> tourUsers2 = list.stream().filter(v -> v.getAuditStatus() != 5).collect(Collectors.toList());
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
    @RequestMapping(value = "/findparentInfo",method = RequestMethod.POST)
    @ResponseBody
    public Result<?>  findparentInfo(Principal principal, Model model){
        User user = userService.findOne(principal.getUserId());
        if (user!=null&&user.getParentId()!=null){
            User userp = userService.findOne(user.getParentId());
            String  phone=userp.getPhone();
            return ResultBuilder.result(phone);
        }
        /*return "ucenter/tour/parentInfo";*/
        return ResultBuilder.result(null);
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
        if (user!=null){
            user.setNickname(userService.findRealName(user.getId()));//放真实姓名
            return ResultBuilder.result(user);
        }else{
            return ResultBuilder.error("推荐人不存在");
        }
    }

    @RequestMapping(value = "/findName",method = RequestMethod.POST)
    @ResponseBody
    public Result<?> findName(String phone){
        User user = userService.findByPhone(phone);
        if (user!=null){
            user.setNickname(userService.findRealName(user.getId()));//放真实姓名
            return ResultBuilder.ok(user.getNickname());
        }else{
            return ResultBuilder.error("人不存在");
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
    @ResponseBody
    public Result<?> findTourApple(Model model){
        TourQueryModel tourQueryModel = new TourQueryModel();
        tourQueryModel.setDelfage(0);
        tourQueryModel.setIsReleased(true);
        List<Tour> tourList = tourService.findAllByTour(tourQueryModel);
        List<Tour> nowTourList = new ArrayList<Tour>();
        for (Tour tour:tourList){
            tour.setImage(getThumbnail(tour.getImage(), 640, 320));
            nowTourList.add(tour);
        }
        //model.addAttribute("tourList",nowTourList);
      /*  model.addAttribute("parentPhone",phone);
        model.addAttribute("reporId",reporId);*/
        /*return "ucenter/tour/tourApply";*/
        return ResultBuilder.result(nowTourList);
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
    @RequestMapping(value = "/findTourDetail" ,method = POST)
    @ResponseBody
    public Result<?> findTourDetail(Long tourId,Long reporId,Model model){
        Tour tour = tourService.findTourOne(tourId);
      /*  model.addAttribute("tour",tour);
        model.addAttribute("parentPhone",parentPhone);
        model.addAttribute("reporId",reporId);*/
        Report report = reportService.findOne(reporId);
        List<String> list= DateUtil.getMonthBetween(report.getCreatedTime(),DateUtil.getMonthData(report.getCreatedTime(),3,-1));
     //   model.addAttribute("list",list);
        //return "ucenter/tour/tourDetail";
        Map<String ,Object>map = new HashMap<>();
        map.put("tour",tour);
        map.put("str",list);
        return ResultBuilder.result(map);
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
     * 检测 旅游人数限制
     * @param phone
     * @param tourTimeId
     * @return
     */
    @RequestMapping(value = "/ajaxCheckPraentNumber",method = RequestMethod.POST)
    @ResponseBody
    public  Result<?>ajaxCheckPraentNumber(String  phone,Long tourTimeId){
        User userP =userService.findByPhone(phone);
        String result =  tourComponent.checkParetNumber(userP.getId(),tourTimeId);
        if (result!=null){
            return ResultBuilder.error(result);
        }else{
            return ResultBuilder.ok(null);
        }
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
    @ResponseBody
    public Result<?> findTourUserVo(String phone,Long reporId,Long tourTimeid,Long tourId,Principal principal, Model model ){
        Long userId = principal.getUserId(); //userInfoService
        Map<String,Object> map = new HashMap<>();
        map.put("userinfoVo",tourComponent.findUserInfoVo(userId,reporId));
        map.put("user", userService.findOne(userId));
        map.put("tour",tourService.findTourOne(tourId));
        TourTime tourTime =tourService.findTourTimeOne(tourTimeid);
        map.put("tourTime",tourTime);
        map.put("timedate",GcUtils.formatDate(tourTime.getBegintime(), "yyyy-MM-dd"));
        User userP =userService.findByPhone(phone);
        userP.setNickname(userService.findRealName(userP.getId()));
        map.put("userp",userP);
        map.put("reporId",reporId);
        String productNumber = tourComponent.findproductNumber(reporId);
        map.put("productNumber",productNumber==null?"":productNumber);
        return ResultBuilder.result(map);
      /*  return "ucenter/tour/tourApplyTable";*/
    }

    @RequestMapping(value = "/create", method = POST)
    public String create(Long tourUserId, TourUser tourUser, Principal principal, Model model, RedirectAttributes redirectAttributes) {

        tourUser.setId(tourUserId);
        tourUser.setUpdateBy(principal.getUserId());
        tourUser.setUpdateDate(new Date());
        tourUser.setAuditStatus(3);
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
       String result[] = tourComponent.checkParam(tourUserInfoVo);
        if (result!=null){
            if ("1".equals(result[1])){
                return ResultBuilder.ok(result[0]);
            }else{
                return ResultBuilder.error(result[0]);
            }
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
            PolicyCode policyCode = policyCodeService.findByCode(tourUserInfoVo.getProductNumber());
            if (policyCode == null) {
                return ResultBuilder.error("产品编号不存在");
            }
            if (policyCode.getTourUsed()!=null) {
                if (policyCode.getTourUsed()) {
                    return ResultBuilder.error("产品编号已被使用");
                }
            }
            tourComponent.updateOrInster(tourUserInfoVo,principal.getUserId());
            return ResultBuilder.ok(null);
        }catch (Exception e){
            e.printStackTrace();
            return ResultBuilder.error("数据异常,请联系客服");
        }

    }

    /**
     * 检测 是否还可以申请旅游  检测报告超过三个月  或者  一年内申请
     * @return
     */
    @RequestMapping(value = "/ajaxCheckTour",method = RequestMethod.POST)
    @ResponseBody
   public Result<?> ajaxCheckTour(String reportId,Principal principal){
       String result = tourComponent.checkTour(reportId,principal.getUserId());
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
    public Result<?> ajaxCheckReport(Long reportId,Principal principal){
        Long loginUserId = principal.getUserId();
        String result = tourComponent.checkReport(reportId,loginUserId);
        if (result!=null){
            return ResultBuilder.error(result);
        }else{
            return ResultBuilder.ok(null);
        }
    }


    /**
     * 旅游入口进入
     * @return
     */
    @RequestMapping(value = "/findTourDetailbyTour", method = GET)
   public String  findTourDetailbyTour(Long tourId,String parentPhone,Long reporId,Long tourTimeId ,Long tourUserId, Model model){
       Tour tour = tourService.findTourOne(tourId);
       model.addAttribute("tour",tour);
       TourTime tourTime = tourService.findTourTimeOne(tourTimeId);
       model.addAttribute("sel",GcUtils.formatDate(tourTime.getBegintime(),"yyyy-MM"));
       List tourTimeList = new ArrayList<TourTime>();
       tourTimeList.add(tourTime);
       model.addAttribute("tourTimeVo",tourComponent.changeVo(tourTimeList,false).get(0));
        model.addAttribute("tourUser",tourService.findTourUser(tourUserId));
       return "ucenter/tour/tourDetailNew";

   }

    /**
     * 计划到达时间检测
     * @param tourUserId
     * @param planTime
     * @return
     */
    @RequestMapping(value = "/ajaxCheckPlantTime",method = RequestMethod.POST)
    @ResponseBody
    public Result<?> ajaxCheckPlantTime(Long tourUserId ,String planTime){
        String  result =  tourComponent.checkPlantTime(tourUserId,planTime);
        if (result!=null){
            return ResultBuilder.error(result);
        }else {
            return ResultBuilder.ok(null);
        }
    }

}
