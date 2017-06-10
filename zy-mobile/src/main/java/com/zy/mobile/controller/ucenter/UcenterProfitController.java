package com.zy.mobile.controller.ucenter;

import com.zy.entity.fnc.Profit;
import com.zy.model.Principal;
import com.zy.model.query.ProfitQueryModel;
import com.zy.service.ProfitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

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

    @RequestMapping(value = "/orderRevenue", method = RequestMethod.GET)
    public String profit(@RequestParam Profit.ProfitType type, Principal principal, Model model) {
        ProfitQueryModel profitQueryModel = new ProfitQueryModel();
        profitQueryModel.setUserIdEQ(principal.getUserId());
        profitQueryModel.setProfitTypeEQ(type);
        List<BigDecimal> revenue = profitService.queryRevenue(profitQueryModel);
        model.addAttribute("revenue", revenue);
        return "ucenter/account/orderRevenue";
    }
}
