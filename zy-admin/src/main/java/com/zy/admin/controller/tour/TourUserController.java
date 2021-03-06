package com.zy.admin.controller.tour;

import com.zy.admin.model.AdminPrincipal;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.common.util.ExcelUtils;
import com.zy.common.util.WebUtils;
import com.zy.component.TourUserComponent;
import com.zy.component.UserComponent;
import com.zy.entity.tour.TourUser;
import com.zy.entity.usr.User;
import com.zy.model.query.TourUserQueryModel;
import com.zy.service.TourService;
import com.zy.service.UserInfoService;
import com.zy.service.UserService;
import com.zy.vo.TourUserAdminVo;
import com.zy.vo.TourUserExportVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

/**
 * Author: Xuwq
 * Date: 2017/6/29.
 */
@RequestMapping("/tourUser")
@Controller
public class TourUserController {

    Logger logger = LoggerFactory.getLogger(TourUserController.class);

    @Autowired
    private TourService tourService;

    @Autowired
    private TourUserComponent tourUserComponent;

    @Autowired
    private UserService userService;


    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserComponent userComponent;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "tour/tourUserList";
    }

    @RequiresPermissions("tourUser:view")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Grid<TourUserAdminVo> list(TourUserQueryModel tourUserQueryModel) {
        Long visitUserId = getPrincipalUserId();
        if(!SecurityUtils.getSubject().isPermitted("tourUser:visitUser") &&
                !SecurityUtils.getSubject().isPermitted("tourUser:visit")) {
            tourUserQueryModel.setVisitUserId(visitUserId);
        }
        Page<TourUser> page = tourService.findAll(tourUserQueryModel);
        List<TourUserAdminVo> list = page.getData().stream().map(v -> {
            return tourUserComponent.buildAdminVo(v);
        }).collect(Collectors.toList());
        return new Grid<TourUserAdminVo>(PageBuilder.copyAndConvert(page, list));
    }

    /**
     * 审核
     * @param id
     * @param isSuccess
     * @param revieweRemark
     * @return
     */
    @RequiresPermissions("tourUser:edit")
    @RequestMapping(value = "/updateAuditStatus", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> updateAuditStatus(@RequestParam Long id, @RequestParam boolean isSuccess, String revieweRemark) {
        Long loginUserId = getPrincipalUserId();
        tourService.updateAuditStatus(id, isSuccess, revieweRemark, loginUserId);
        return ResultBuilder.ok("审核成功");
    }

    /**
     * 回访
     * @param id
     * @param firstVisitStatus
     * @param secondVisitStatus
     * @param thirdVisitStatus
     * @param visitRemark
     * @return
     */
    @RequiresPermissions("tourUser:edit")
    @RequestMapping(value = "/updateVisitStatus", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> updateVisitStatus(@RequestParam Long id, Integer firstVisitStatus, Integer secondVisitStatus, Integer thirdVisitStatus, String visitRemark) {
        Long loginUserId = getPrincipalUserId();
        tourService.updateVisitStatus(id, firstVisitStatus,secondVisitStatus,thirdVisitStatus, visitRemark, loginUserId);
        return ResultBuilder.ok("审核成功");
    }

    @RequiresPermissions("tourUser:edit")
    @RequestMapping(value = "/addInfo", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> addInfo(@RequestParam Long id, @RequestParam Integer isJoin, BigDecimal amount) {
        Long loginUserId = getPrincipalUserId();
        tourService.addInfo(id, isJoin, amount, loginUserId);
        return ResultBuilder.ok("补充信息成功");
    }

    /**
     * 旅客信息导出
     * @param tourUserQueryModel
     * @param response
     * @return
     * @throws IOException
     */
    @RequiresPermissions("tourUser:export")
    @RequestMapping("/tourUserExport")
    public String export(TourUserQueryModel tourUserQueryModel, HttpServletResponse response) throws IOException {

        tourUserQueryModel.setPageSize(null);
        tourUserQueryModel.setPageNumber(null);
        Page<TourUser> page = tourService.findExAll(tourUserQueryModel);
        String fileName = "旅客信息.xlsx";
        WebUtils.setFileDownloadHeader(response, fileName);

        List<TourUserExportVo > tourUserExportVo = page.getData().stream().map(tourUserComponent::buildExportVo).collect(Collectors.toList());
        OutputStream os = response.getOutputStream();
        ExcelUtils.exportExcel(tourUserExportVo, TourUserExportVo.class, os);

        return null;
    }

    @RequiresPermissions("tourUser:edit")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable Long id, Model model) {
        TourUser tourUser = tourService.findTourUser(id);
        TourUserAdminVo tourUserAdminVo = tourUserComponent.buildAdminVo(tourUser);
        model.addAttribute("tourUserAdminVo",tourUserAdminVo);
        return "tour/tourUserEdit";
    }

    /**
     * 编辑
     * @param tourUser
     * @return
     */
    @RequiresPermissions("tourUser:edit")
    @RequestMapping(value = "/editTourUser", method = RequestMethod.POST)
    public String editTourUser(TourUser tourUser,RedirectAttributes redirectAttributes) {
        Long tourUserId = tourUser.getId();
        validate(tourUser, NOT_NULL, "tourUser id is null");
        Long loginUserId = getPrincipalUserId();
        tourUser.setUpdateBy(loginUserId);
        try {
            tourService.modify(tourUser);
            redirectAttributes.addFlashAttribute(ResultBuilder.ok("保存成功"));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ResultBuilder.error("资源保存失败, 原因" + e.getMessage()));
            return "redirect:/tourUser/update/" + tourUserId;
        }
        return "redirect:/tourUser";
    }

    /**
     * 重置
     * @param id
     * @return
     */
    @RequiresPermissions("tourUser:edit")
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> reset(@RequestParam Long id) {
        Long loginUserId = getPrincipalUserId();
        tourService.reset(id, loginUserId);
        return ResultBuilder.ok("重置成功");
    }


    /**
     * 客服分配
     * @param id
     * @param phone
     * @return
     */
    @RequiresPermissions("tourUser:visitUser")
    @RequestMapping(value = "/visitUser", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> visitUser(@RequestParam Long id, @RequestParam String phone) {
        User user = userService.findByPhone(phone);
        if(user == null ) {
            return ResultBuilder.error("客服不存在");
        }
        try{
            tourService.visitUser(id, user.getId());
            return ResultBuilder.ok("操作成功");
        } catch (Exception e) {
            return ResultBuilder.error(e.getMessage());
        }
    }

    @RequiresPermissions("tourUser:visitUser")
    @RequestMapping(value = "/visitUserList", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> visitUserList(@RequestParam String ids, @RequestParam String phone) {
        User user = userService.findByPhone(phone);
        if(user == null ) {
            return ResultBuilder.error("客服不存在");
        }
        if(StringUtils.isBlank(ids)){
            return ResultBuilder.error("请至少选择一条记录！");
        } else {
            String[] idStringArray = ids.split(",");
            for (String idString : idStringArray) {
                Long id = new Long(idString);
                try{
                    tourService.visitUser(id, user.getId());
                } catch (Exception e) {
                    return ResultBuilder.error(e.getMessage());
                }
            }
        }
        return ResultBuilder.ok("操作成功");
    }

    /**
     * 获取登录人的id
     * @return
     */
    private Long getPrincipalUserId() {
        AdminPrincipal principal = (AdminPrincipal) SecurityUtils.getSubject().getPrincipal();
        return principal.getUserId();
    }
}
