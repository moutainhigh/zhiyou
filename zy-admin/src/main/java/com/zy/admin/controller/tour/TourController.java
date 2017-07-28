package com.zy.admin.controller.tour;

import com.zy.admin.controller.CaptchaController;
import com.zy.admin.model.AdminPrincipal;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.TourComponent;
import com.zy.entity.sys.SystemCode;
import com.zy.entity.tour.BlackOrWhite;
import com.zy.entity.tour.Tour;
import com.zy.entity.tour.TourTime;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.model.Constants;
import com.zy.model.query.BlackOrWhiteQueryModel;
import com.zy.model.query.TourQueryModel;
import com.zy.model.query.TourTimeQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.BlackOrWhiteService;
import com.zy.service.SystemCodeService;
import com.zy.service.TourService;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import com.zy.vo.BlackOrWhiteAdminVo;
import com.zy.vo.TourAdminVo;
import com.zy.vo.TourTimeDetailVo;
import com.zy.vo.TourTimeVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

/**
 * Created by it001 on 2017/6/28.
 */
@RequestMapping("/tour")
@Controller
public class TourController {
    Logger logger = LoggerFactory.getLogger(TourController.class);

    @Autowired
    private TourService tourService;

    @Autowired
    private TourComponent tourComponent;

    @Autowired
    private BlackOrWhiteService blackOrWhiteService;

    @Autowired
    private UserService userService;

    @Autowired
    private SystemCodeService systemCodeService;


    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "tour/tourList";
    }

    @RequiresPermissions("tour:view")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Grid<TourAdminVo> list(TourQueryModel tourQueryModel) {
        tourQueryModel.setDelfage(0);
        if (tourQueryModel.getTitle()!=null&&!"".equals(tourQueryModel.getTitle())) {
            tourQueryModel.setTitle("%" + tourQueryModel.getTitle() + "%");
        }
        Page<Tour> page = tourService.findPageBy(tourQueryModel);
        List<TourAdminVo> list = page.getData().stream().map(v -> {
            return tourComponent.buildAdminVo(v, false);
        }).collect(Collectors.toList());
        return new Grid<TourAdminVo>(PageBuilder.copyAndConvert(page, list));
    }

    @RequiresPermissions("tour:edit")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create() {
        return "tour/createTour";
    }

    @RequiresPermissions("tour:edit")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Tour tour, Model model, RedirectAttributes redirectAttributes,AdminPrincipal principal) {
        tour.setCreateby(principal.getUserId());
        tour.setIsReleased(false);
        tour.setDelfage(0);
        try {
            tourService.createTour(tour);
            redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("旅游信息创建成功"));
            return "redirect:/tour";
        } catch (Exception e) {
            model.addAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
            model.addAttribute("tour", tour);
            return "tour/createTour";
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public String update(Model model, @RequestParam Long id) {
        validate(id, NOT_NULL, "Tour id is null");

        Tour tour = tourService.findTourOne(id);
        validate(tour, NOT_NULL, "Tour id " + id + " is not found");

        String content = tour.getContent();
        content = StringUtils.replaceEach(content, new String[] {"'", "\r\n", "\r", "\n"}, new String[] {"\\'", "", "", ""});
        tour.setContent(content);
        model.addAttribute("tour", tourComponent.buildAdminVo(tour, false));
        return "tour/updateTour";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Tour tour, RedirectAttributes redirectAttributes,AdminPrincipal principal) {
        Long id = tour.getId();
        validate(id, NOT_NULL, "Tour id is null");
        tour.setUpdateby(principal.getUserId());
        try {
            tourService.updatTour(tour);
            redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("旅游信息更新成功"));
            return "redirect:/tour";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
            return "redirect:/tour/update?id=" + tour.getId();
        }

    }

    @RequiresPermissions("tourSetting:view")
    @RequestMapping(value = "/ajaxupdate", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> ajaxupdate(Long id,AdminPrincipal principal) {
        validate(id, NOT_NULL, "Tour id is null");
        Tour tour = new Tour();
        tour.setId(id);
        tour.setIsReleased(true);
        tour.setUpdateby(principal.getUserId());
        try {
            tourService.updatTour(tour);
            return ResultBuilder.ok("操作成功");
        } catch (Exception e) {
          e.printStackTrace();
          return ResultBuilder.error("系统错误:"+e.getMessage());
        }

    }

    @RequestMapping(value = "/findTourTime", method = RequestMethod.GET)
    public String  findTourTime(Model model, @RequestParam Long tourId,Integer flage){
        model.addAttribute("tour",tourService.findTourOne(tourId));
        model.addAttribute("flage",flage);
        return "tour/tourTimeList";
    }

    @RequestMapping(value = "/findTourTime",method = RequestMethod.POST)
    @ResponseBody
    public Grid<TourTimeDetailVo> findTourTimelist(TourTimeQueryModel tourTimeQueryModel) {
        tourTimeQueryModel.setDelfage(0);
        Page<TourTime> page = tourService.findTourTimePage(tourTimeQueryModel);
        List<TourTimeDetailVo> list = page.getData().stream().map(v -> {
            return tourComponent.buildTourTimeVo(v, false);
        }).collect(Collectors.toList());
        return new Grid<TourTimeDetailVo>(PageBuilder.copyAndConvert(page, list));
    }

    @RequestMapping(value = "/createTourTime", method = RequestMethod.GET)
    public String createTourTime(Model model, @RequestParam Long tourId){
        model.addAttribute("tourId",tourId);
        return "tour/createTourTime";
    }

    @RequestMapping(value = "/ajaxCreateTourTime", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> ajaxCreateTourTime(TourTimeVo tourTimeVo, AdminPrincipal principal){
        tourTimeVo.setCreateby(principal.getUserId());
        try {
            TourTime  tourTime =  tourComponent.buildTourTime(tourTimeVo);
            tourService.createTourTime(tourTime);
            return ResultBuilder.ok("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBuilder.error("系统错误:"+e.getMessage());
        }

    }

    @RequestMapping(value = "/release", method = RequestMethod.POST)
    @ResponseBody
    public Result<?>ajaxRelease(Long tourTimeId,Integer isreleased,Integer delFlage,AdminPrincipal principal){
        TourTime tourTime = new TourTime();
        tourTime.setUpdateby(principal.getUserId());
        tourTime.setId(tourTimeId);
        tourTime.setIsreleased(isreleased);
        tourTime.setDelflag(delFlage);
       try{
          tourService.updateTourTime(tourTime);
          return ResultBuilder.ok("操作成功");
         }catch(Exception e){
           logger.error(e.getMessage());
          e.printStackTrace();
          return ResultBuilder.error("系统错误" + e.getMessage());
      }
    }

    @RequestMapping(value = "/ajaxTourrelease", method = RequestMethod.POST)
    @ResponseBody
    public  Result<?>ajaxTourrelease(Long tourId,Boolean isReleased,AdminPrincipal principal){
        Tour tour = new Tour();
        tour.setId(tourId);
        tour.setIsReleased(isReleased);
        tour.setUpdateby(principal.getUserId());
        try {
            tourService.updatTour(tour);
            return ResultBuilder.ok("操作成功");
        }catch (Exception e){
            e.printStackTrace();
            return ResultBuilder.error("系统错误："+e.getMessage());
        }

    }

    @RequestMapping(value = "/ajaxTourDelete", method = RequestMethod.POST)
    @ResponseBody
    public  Result<?>ajaxTourDelete(Long tourId,Boolean isReleased,AdminPrincipal principal){
        Tour tour = new Tour();
        tour.setId(tourId);
        tour.setIsReleased(isReleased);
        tour.setUpdateby(principal.getUserId());
        try {
            tourService.deleteTour(tour);
            return ResultBuilder.ok("操作成功");
        }catch (Exception e){
            e.printStackTrace();
            return ResultBuilder.error("系统错误:"+e.getMessage());
        }
    }

    @RequiresPermissions("tourSetting:view")
    @RequestMapping(value = "/blackOrWhite" , method = RequestMethod.GET)
    public String blackOrWhiteList(Model model) {
        model.addAttribute("userRankMap", Arrays.asList(User.UserRank.values()).stream().collect(Collectors.toMap(v->v, v-> GcUtils.getUserRankLabel(v),(u, v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); }, LinkedHashMap::new)) );
        return "tour/blackOrWhiteList";
    }

    /**
     * 黑白名单列表
     * @param blackOrWhiteQueryModel
     * @param nicknameLK
     * @param phoneEQ
     * @param userRankEQ
     * @return
     */
    @RequiresPermissions("tourSetting:view")
    @RequestMapping(value = "/blackOrWhite" , method = RequestMethod.POST)
    @ResponseBody
    public Grid<BlackOrWhiteAdminVo> blackOrWhiteList(BlackOrWhiteQueryModel blackOrWhiteQueryModel, String nicknameLK, String phoneEQ,UserRank userRankEQ) {
        if (StringUtils.isNotBlank(nicknameLK) || StringUtils.isNotBlank(phoneEQ) || null != userRankEQ) {
            List<User> all = userService.findAll(UserQueryModel.builder().nicknameLK(nicknameLK).phoneEQ(phoneEQ).userRankEQ(userRankEQ).build());
            if (all.isEmpty()) {
                return new Grid<BlackOrWhiteAdminVo>(PageBuilder.empty(blackOrWhiteQueryModel.getPageSize(), blackOrWhiteQueryModel.getPageNumber()));
            }
            blackOrWhiteQueryModel.setUserIdIN(all.stream().map(v -> v.getId()).toArray(Long[]::new));
        }
        Page<BlackOrWhite> page = blackOrWhiteService.findPage(blackOrWhiteQueryModel);
        Page<BlackOrWhiteAdminVo> voPage = PageBuilder.copyAndConvert(page, v -> tourComponent.buildBlackOrWhiteAdminVo(v));
        return new Grid<>(voPage);
    }

    @RequiresPermissions("tourSetting:edit")
    @RequestMapping(value = "/createBlackWhite", method = RequestMethod.GET)
    public String createBlackWhite(Model model) {
        SystemCode systemCode = systemCodeService.findByTypeAndName("BLACKORWHITENUMBER", "BLACKORWHITE");
        int number;
        if(null == systemCode){
            number = 8;
        }else{
            number = Integer.parseInt(systemCode.getSystemValue());
        }
        model.addAttribute("number",number);
        return "tour/blackWhiteCreate";
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
     * 新增黑白名单
     * @param blackOrWhite
     * @param phone
     * @return
     */
    @RequiresPermissions("tourSetting:edit")
    @RequestMapping(value = "/createBlackWhite", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> create(BlackOrWhite blackOrWhite, @RequestParam String phone) {
        try{
            User user = userService.findByPhone(phone);
            if(null != user){
                BlackOrWhite one = blackOrWhiteService.findByUserId(user.getId());
                if(null != one){
                    return ResultBuilder.error("该用户已经加入了黑/白名单");
                }else {
                    blackOrWhite.setUserId(user.getId());
                    blackOrWhiteService.create(blackOrWhite);
                    return ResultBuilder.ok("新增成功");
                }
            }else{
                return ResultBuilder.error("手机号不存在");
            }
        } catch (Exception e) {
            return ResultBuilder.error(e.getMessage());
        }
    }


    @RequiresPermissions("tourSetting:edit")
    @RequestMapping(value = "/editBlackWhite/{id}", method = RequestMethod.GET)
    public String updateBlackWhite(@PathVariable Long id, Model model) {
        SystemCode systemCode = systemCodeService.findByTypeAndName("BLACKORWHITENUMBER", "BLACKORWHITE");
        int number;
        if(null == systemCode){
            number = 8;
        }else{
            number = Integer.parseInt(systemCode.getSystemValue());
        }
        BlackOrWhite blackOrWhite = blackOrWhiteService.findOne(id);
        BlackOrWhiteAdminVo blackOrWhiteAdminVo = tourComponent.buildBlackOrWhiteAdminVo(blackOrWhite);
        model.addAttribute("blackOrWhiteAdminVo",blackOrWhiteAdminVo);
        model.addAttribute("number",number);
        return "tour/blackWhiteEdit";
    }

    /**
     * 编辑黑白名单
     * @param blackOrWhite
     * @return
     */
    @RequiresPermissions("tourSetting:edit")
    @RequestMapping(value = "/editBlackWhite", method = RequestMethod.POST)
    public String updateBlackWhite(BlackOrWhite blackOrWhite,RedirectAttributes redirectAttributes) {
        Long blackOrWhiteId = blackOrWhite.getId();
        validate(blackOrWhite, NOT_NULL, "blackOrWhite id is null");
        try {
            blackOrWhiteService.modify(blackOrWhite);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ResultBuilder.error(e.getMessage()));
            return "redirect:/tour/blackWhiteEdit/" + blackOrWhiteId;
        }
        return "redirect:/tour/blackOrWhite";
    }

    @RequiresPermissions("tourSetting:edit")
    @RequestMapping(value = "/deleteBlackWhite", method = RequestMethod.POST)
    @ResponseBody
    public Boolean deleteBlackWhite(@RequestParam Long id ) {
        BlackOrWhite blackOrWhite = blackOrWhiteService.findOne(id);
        validate(blackOrWhite, NOT_NULL, "blackOrWhite not found, id is " + id);
        blackOrWhiteService.delete(id);
        return true;
    }





}
