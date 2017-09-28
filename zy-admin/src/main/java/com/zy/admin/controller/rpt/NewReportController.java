package com.zy.admin.controller.rpt;

import com.zy.admin.model.AdminPrincipal;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.NewReportComponent;
import com.zy.service.SystemCodeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by it001 on 2017-09-28.
 */
@Controller
@RequestMapping("/newReport")
public class NewReportController {

    @Autowired
    private NewReportComponent newReportComponent;

    /**
     * 跳转到  首页
     * @param model
     * @param type  类型  公司（0）或者大区
     * @return 跳转到首页并准备 头部数据
     */
    @RequestMapping("/toMian")
    public String toMian(Model model,String type){

        return "";
    }


    /**
     * 查询团队信息
     * @param type
     * @return
     */
    @RequestMapping(value = "/ajaxNewReportTeam", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> ajaxTeam(String type) {
        Map<String,Object> resultMap = newReportComponent.disposeTeam(type);
        return ResultBuilder.result(null);
    }
}
