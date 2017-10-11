package com.zy.admin.controller.rpt;

import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.LocalCacheComponent;
import com.zy.component.NewReportComponent;
import org.slf4j.LoggerFactory;
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

    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(NewReportController.class);

    @Autowired
    private NewReportComponent newReportComponent;

    @Autowired
    private LocalCacheComponent localCacheComponent;

    /**
     * 跳转到  首页
     * @param model
     * @param type  类型  公司（0）或者大区
     * @return 跳转到首页并准备 头部数据
     */
    @RequestMapping("/base")
    public String toMian(Model model,String type){
        model.addAttribute("type",type);
        return "base";
    }


    //统计 服务次数
    @RequestMapping(value = "/ajaxOrderNumber", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> ajaxOrderNumber(String type) {
       String number ="000000000";
        try {
            number = newReportComponent.statOrderNumber(type,"d",localCacheComponent);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultBuilder.error(e.getMessage());
        }
        return ResultBuilder.ok(number);
    }


    /**
     * 查询团队信息 人数
     * @param type
     * @return
     */
    @RequestMapping(value = "/ajaxNewReportTeamNumber", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> ajaxTeamNumber(String type) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = newReportComponent.disposeTeamAreatNumber(type,localCacheComponent);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBuilder.error(e.getMessage());
        }
        return ResultBuilder.result(resultMap);
    }

    /**
     * 查询团队信息 新晋特级占比
     * @param type
     * @return
     */
    @RequestMapping(value = "/ajaxNewReportTeamNewNumber", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> ajaxTeamNewNumber(String type) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = newReportComponent.disposeTeamAreatNewNumber(type,localCacheComponent);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBuilder.error(e.getMessage());
        }
        return ResultBuilder.result(resultMap);
    }
    /**
     * 查询团队信息 沉睡特级占比
     * @param type
     * @return
     */
    @RequestMapping(value = "/ajaxNewReportTeamSleepNumber", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> ajaxTeamSleepNumber(String type) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = newReportComponent.disposeTeamAreatSleepNumber(type,localCacheComponent);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBuilder.error(e.getMessage());
        }
        return ResultBuilder.result(resultMap);
    }

    /**
     * 查询团队地区分布详细信息
     * @param type
     * @return
     */
    @RequestMapping(value = "/ajaxNewReportTeamAreat", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> ajaxTeamAreat(String type) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = newReportComponent.disposeTeamAreat(type,localCacheComponent);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultBuilder.error(e.getMessage());
        }
        return ResultBuilder.result(resultMap);
    }



    /**
     * 查询团队 特价逻辑
     * @param type
     * @return
     */
    @RequestMapping(value = "/ajaxNewReportTeamV4", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> ajaxTeamV4(String type) {
        Map<String, String> resultMap = new HashMap<>();
        try {
            resultMap = newReportComponent.disposeTeamV4(type,localCacheComponent);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultBuilder.error(e.getMessage());
        }
        return ResultBuilder.result(resultMap);

    }


    /**
     * 查询团队  U币  以及奖金发放情况（上月）
     * @param type
     * @return
     */
    @RequestMapping(value = "/ajaxNewReportTeamUb", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> ajaxTeamUb(String type) {
        Map<String, String> resultMap = new HashMap<>();
        try {
            resultMap = newReportComponent.disposeTeamUb(type,localCacheComponent);
         }catch (Exception e){
            logger.error(e.getMessage());
            return ResultBuilder.error(e.getMessage());
        }
        return ResultBuilder.result(resultMap);

    }

    /**
     * 查询团队 目标量
     * @param type
     * @return
     */
    @RequestMapping(value = "/ajaxNewReportTeamTage", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> ajaxTeamTage(String type) {
        Map<String, String> resultMap = new HashMap<>();
        try {
            resultMap = newReportComponent.disposeTeamTage(type,localCacheComponent);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResultBuilder.error(e.getMessage());
        }
        return ResultBuilder.result(resultMap);

    }

    /**
     * 查询大区本月销量占比
     * @param type
     * @return
     */
    @RequestMapping(value = "/ajaxLargeAreaThisMonthSalesHaveRate", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> ajaxSalesHaveRate(String type) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = newReportComponent.disposeLargeareaSalesHaveRate(type,localCacheComponent);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBuilder.error(e.getMessage());
        }
        return ResultBuilder.result(resultMap);
    }

    /**
     * 查询各大区年份销量
     * @param type
     * @return
     */
    @RequestMapping(value = "/largeAreaMonthSalesRelativeRate", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> ajaxSalesRelativeRate(String type) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = newReportComponent.disposeLargeAreaMonthSalesRelativeRate(type);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBuilder.error(e.getMessage());
        }
        return ResultBuilder.result(resultMap);
    }

    /**
     * 查询各大区当月销量
     * @param type
     * @return
     */
    @RequestMapping(value = "/monthSalesAndTarget", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> ajaxMonthSalesAndTarget(String type) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = newReportComponent.disposeMonthSalesAndTarget(type,localCacheComponent);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBuilder.error(e.getMessage());
        }
        return ResultBuilder.result(resultMap);
    }

    /**
     * 查询各大区当月销量
     * @param type
     * @return
     */
    @RequestMapping(value = "/largeAreaDaySales", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> ajaxLargeAreaDaySales(String type) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = newReportComponent.disposeLargeAreaDaySales(type);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResultBuilder.error(e.getMessage());
        }
        return ResultBuilder.result(resultMap);
    }

}
