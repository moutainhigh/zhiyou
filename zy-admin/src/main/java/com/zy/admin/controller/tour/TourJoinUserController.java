package com.zy.admin.controller.tour;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.common.util.ExcelUtils;
import com.zy.common.util.WebUtils;
import com.zy.component.TourUserComponent;
import com.zy.entity.tour.TourUser;
import com.zy.model.query.TourUserQueryModel;
import com.zy.service.TourService;
import com.zy.vo.TourJoinUserExportVo;
import com.zy.vo.TourUserAdminVo;
import com.zy.vo.TourUserExportVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

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
        Page<TourUser> page = tourService.findAll(tourUserQueryModel);
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
        Page<TourUser> page = tourService.findAll(tourUserQueryModel);
        String fileName = "参游旅客信息.xlsx";
        WebUtils.setFileDownloadHeader(response, fileName);

        List<TourJoinUserExportVo> tourJoinUserExportVo = page.getData().stream().map(tourUserComponent::buildJoinExportVo).collect(Collectors.toList());
        OutputStream os = response.getOutputStream();
        ExcelUtils.exportExcel(tourJoinUserExportVo, TourJoinUserExportVo.class, os);

        return null;
    }
}
