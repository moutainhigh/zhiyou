package com.zy.mobile.controller.ucenter;

import com.zy.component.ProfitComponent;
import com.zy.entity.fnc.Profit;
import com.zy.model.Principal;
import com.zy.model.query.ProfitQueryModel;
import com.zy.service.ProfitService;
import com.zy.vo.ProfitListVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author: Xuwq
 * Date: 2017/6/10.
 */
@RequestMapping("/u/profit")
@Controller
public class UcenterProfitController {

    Logger logger = LoggerFactory.getLogger(UcenterProfitController.class);

    @Autowired
    private ProfitService profitService;

    @Autowired
    private ProfitComponent profitComponent;

    @RequestMapping(value = "/orderRevenue", method = RequestMethod.GET)
    public String profit(@RequestParam Profit.ProfitType type, Principal principal, Model model) {
        ProfitQueryModel profitQueryModel = new ProfitQueryModel();
        profitQueryModel.setUserIdEQ(principal.getUserId());
        profitQueryModel.setProfitTypeEQ(type);
        Map<String ,Object> returnMap = profitService.queryRevenue(profitQueryModel);
        model.addAttribute("dateMap", returnMap);
        model.addAttribute("type", type.ordinal());
        model.addAttribute("types", type);
        return "ucenter/account/orderRevenue";
    }

    @RequestMapping(value = "/orderRevenueDetail", method = RequestMethod.GET)
    public String orderRevenueDetail(@RequestParam Profit.ProfitType type,int month, Principal principal, Model model) {
        ProfitQueryModel profitQueryModel = new ProfitQueryModel();
        profitQueryModel.setUserIdEQ(principal.getUserId());
        profitQueryModel.setProfitTypeEQ(type);
        profitQueryModel.setMonth(month);
        profitQueryModel.setProfitStatusEQ(Profit.ProfitStatus.已发放);
        List<Profit> orderRevenueDetails = profitService.orderRevenueDetail(profitQueryModel);
        List<ProfitListVo> detailVoPage = orderRevenueDetails.stream().map(v -> {
            return profitComponent.buildListVo(v);
        }).collect(Collectors.toList());
        model.addAttribute("orderRevenueDetails", detailVoPage);
        model.addAttribute("type", type.ordinal());
        return "ucenter/account/orderRevenueDetail";
    }
}
