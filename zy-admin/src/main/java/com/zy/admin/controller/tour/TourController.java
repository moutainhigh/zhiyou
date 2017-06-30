package com.zy.admin.controller.tour;

import com.zy.admin.controller.CaptchaController;
import com.zy.admin.model.AdminPrincipal;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.TourComponent;
import com.zy.entity.tour.Tour;
import com.zy.entity.usr.User;
import com.zy.model.Constants;
import com.zy.model.query.BlackOrWhiteQueryModel;
import com.zy.model.query.TourQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.TourService;
import com.zy.service.UserService;
import com.zy.vo.BlackOrWhiteAdminVo;
import com.zy.vo.TourAdminVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    Logger logger = LoggerFactory.getLogger(CaptchaController.class);

    @Autowired
    private TourService tourService;

    @Autowired
    private TourComponent tourComponent;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "tour/tourList";
    }

    @RequiresPermissions("tour:view")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Grid<TourAdminVo> list(TourQueryModel tourQueryModel) {
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

    @RequiresPermissions("article:edit")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(Tour tour, Model model, RedirectAttributes redirectAttributes,AdminPrincipal principal) {
        tour.setCreateby(principal.getUserId());
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
        tour.setUpdateby(principal.getId());
        try {
            tourService.updatTour(tour);
            redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("旅游信息更新成功"));
            return "redirect:/tour";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
            return "redirect:/tour/update?id=" + tour.getId();
        }

    }

    @RequestMapping(value = "/findTourTime", method = RequestMethod.GET)
    public String  findTourTime(Model model, @RequestParam Long tourId){

        return "tour/addTourTime";
    }

    @RequiresPermissions("tourSetting:*")
    @RequestMapping(value = "/blackOrWhite" , method = RequestMethod.GET)
    public String list(Model model) {
        return "tour/blackOrWhiteList";
    }

    @RequiresPermissions("tourSetting:*")
    @RequestMapping(value = "/blackOrWhite" , method = RequestMethod.POST)
    @ResponseBody
    public Grid<BlackOrWhiteAdminVo> list(BlackOrWhiteQueryModel blackOrWhiteQueryModel, String nicknameLK, String phoneEQ) {

        if (StringUtils.isNotBlank(nicknameLK) || StringUtils.isNotBlank(phoneEQ)) {
            List<User> all = userService.findAll(UserQueryModel.builder().nicknameLK(nicknameLK).phoneEQ(phoneEQ).build());
            if (all.isEmpty()) {
                return new Grid<BlackOrWhiteAdminVo>(PageBuilder.empty(blackOrWhiteQueryModel.getPageSize(), blackOrWhiteQueryModel.getPageNumber()));
            }
            blackOrWhiteQueryModel.setUserIdIN(all.stream().map(v -> v.getId()).toArray(Long[]::new));
        }
//        Page<Activity> page = activityService.findPage(activityQueryModel);
 //       Page<ActivityAdminVo> voPage = PageBuilder.copyAndConvert(page, v -> activityComponent.buildAdminVo(v, false));
    //    return new Grid<>(voPage);
        return null;
    }

}
