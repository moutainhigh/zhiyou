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
import com.zy.vo.TourUserAdminVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

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

    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "tour/tourUserList";
    }

    @RequiresPermissions("tourUser:view")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Grid<TourUserAdminVo> list(TourUserQueryModel tourUserQueryModel) {
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


    @RequiresPermissions("tourUser:export")
    @RequestMapping("/export")
    public String export(TourUserQueryModel tourUserQueryModel, HttpServletResponse response) throws IOException {

        tourUserQueryModel.setPageSize(null);
        tourUserQueryModel.setPageNumber(null);
        Page<TourUser> page = tourService.findAll(tourUserQueryModel);
        String fileName = "旅客信息.xlsx";
        WebUtils.setFileDownloadHeader(response, fileName);

        List<TourUserAdminVo > tourUserAdminVo = page.getData().stream().map(tourUserComponent::buildAdminVo).collect(Collectors.toList());
        OutputStream os = response.getOutputStream();
        ExcelUtils.exportExcel(tourUserAdminVo, TourUserAdminVo.class, os);

        return null;
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
