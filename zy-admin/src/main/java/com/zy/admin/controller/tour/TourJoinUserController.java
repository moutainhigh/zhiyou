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
import com.zy.entity.tour.TourUser;
import com.zy.model.query.TourUserQueryModel;
import com.zy.service.TourService;
import com.zy.vo.TourJoinUserExportVo;
import com.zy.vo.TourUserAdminVo;
import org.apache.commons.lang.StringUtils;
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
 * Date: 2017/7/6.
 */
@RequestMapping("/tourJoinUser")
@Controller
public class TourJoinUserController {

    Logger logger = LoggerFactory.getLogger(TourUserController.class);

    @Autowired
    private TourService tourService;

    @Autowired
    private TourUserComponent tourUserComponent;

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "tour/tourJoinUserList";
    }

    @RequiresPermissions("tourJoinUser:view")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Grid<TourUserAdminVo> list(TourUserQueryModel tourUserQueryModel) {
        Page<TourUser> page = tourService.findJoinAll(tourUserQueryModel);
        List<TourUserAdminVo> list = page.getData().stream().map(v -> {
            return tourUserComponent.buildAdminVo(v);
        }).collect(Collectors.toList());
        return new Grid<TourUserAdminVo>(PageBuilder.copyAndConvert(page, list));
    }

    /**
     * 参游旅客信息导出
     * @param tourUserQueryModel
     * @param response
     * @return
     * @throws IOException
     */
    @RequiresPermissions("tourJoinUser:export")
    @RequestMapping("/tourJoinUserExport")
    public String export(TourUserQueryModel tourUserQueryModel, HttpServletResponse response) throws IOException {

        tourUserQueryModel.setPageSize(null);
        tourUserQueryModel.setPageNumber(null);
        Page<TourUser> page = tourService.findJoinExAll(tourUserQueryModel);
        String fileName = "参游旅客信息.xlsx";
        WebUtils.setFileDownloadHeader(response, fileName);

        List<TourJoinUserExportVo> tourJoinUserExportVo = page.getData().stream().map(tourUserComponent::buildJoinExportVo).collect(Collectors.toList());
        OutputStream os = response.getOutputStream();
        ExcelUtils.exportExcel(tourJoinUserExportVo, TourJoinUserExportVo.class, os);

        return null;
    }

    @RequiresPermissions("tourJoinUser:edit")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(@PathVariable Long id, Model model) {
        TourUser tourUser = tourService.findTourUser(id);
        TourUserAdminVo tourUserAdminVo = tourUserComponent.buildAdminVo(tourUser);
        model.addAttribute("tourUserAdminVo",tourUserAdminVo);
        return "tour/tourJoinUserEdit";
    }

    /**
     * 编辑
     * @param tourUser
     * @return
     */
    @RequiresPermissions("tourJoinUser:edit")
    @RequestMapping(value = "/editTourJoinUser", method = RequestMethod.POST)
    public String editTourJoinUser(TourUser tourUser,RedirectAttributes redirectAttributes) {
        Long tourUserId = tourUser.getId();
        validate(tourUser, NOT_NULL, "tourUser id is null");
        Long loginUserId = getPrincipalUserId();
        tourUser.setUpdateBy(loginUserId);
        tourUser.setCarImages(tourUser.getImage());
        try {
            tourService.modify(tourUser);
            redirectAttributes.addFlashAttribute(ResultBuilder.ok("保存成功"));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ResultBuilder.error("资源保存失败, 原因" + e.getMessage()));
            return "redirect:/tourJoinUser/update/" + tourUserId;
        }
        return "redirect:/tourJoinUser";
    }


    @RequiresPermissions("tourJoinUser:edit")
    @RequestMapping(value = "/amount", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> amount(@RequestParam Long id, Long guaranteeAmount, Long refundAmount, Long surcharge) {
        Long loginUserId = getPrincipalUserId();
        tourService.amount(id, guaranteeAmount, refundAmount, surcharge, loginUserId);
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
