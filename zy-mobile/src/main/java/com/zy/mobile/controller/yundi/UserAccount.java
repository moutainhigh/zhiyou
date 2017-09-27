package com.zy.mobile.controller.yundi;

import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.ProfitComponent;
import com.zy.entity.fnc.Account;
import com.zy.entity.fnc.Profit;
import com.zy.mobile.controller.notify.WeixinMpNotifyController;
import com.zy.model.Principal;
import com.zy.model.query.ProfitQueryModel;
import com.zy.service.AccountService;
import com.zy.service.ProfitService;
import com.zy.vo.ProfitListVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by it001 on 2017-09-21.
 */
@RequestMapping("/yundi/account")
@Controller
public class UserAccount  {

    final Logger logger = LoggerFactory.getLogger(UserAccount.class);

    @Autowired
    private ProfitService profitService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ProfitComponent profitComponent;

    @RequestMapping(value="/countIncomeDataByUser", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> countIncomeDataByUser(HttpServletRequest request, HttpServletResponse response,String userIdstr) {
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST");
            response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
            if (userIdstr == null || "".equals(userIdstr)) {
                return ResultBuilder.error("参数有错");
            }
            Long userId = Long.valueOf(userIdstr);
            Map<String, Object> map = new HashMap<>();
            List<Account> accounts = accountService.findByUserId(userId);//统计u币 ，积分，股权
            accounts.stream().forEach(v -> {
                switch (v.getCurrencyType()) {
                    case 现金:
                        map.put("amount1", v.getAmount());
                        break;
                    case 积分:
                        map.put("amount2", v.getAmount());
                        break;
                    case 货币期权:
                        map.put("amount3", v.getAmount());
                        break;
                    case 货币股份:
                        map.put("amount4", v.getAmount());
                        break;
                    default:
                        break;
                }
            });
            Map<String, Object> returnMap = profitService.countIncomeDataByUser(userId, map);
            return ResultBuilder.result(returnMap);
        }catch (Exception e){
            logger.info(e.getMessage());
            e.printStackTrace();
            return ResultBuilder.error("调用失败，请联系管理员");
        }

    }


    @RequestMapping(value = "/orderRevenue", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> profit(HttpServletRequest request, HttpServletResponse response,String type, String userIdstr){
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST");
            response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
            if (type == null || "".equals(type) || userIdstr == null || "".equals(userIdstr)) {
                return ResultBuilder.error("参数有错");
            }
            Long userId = Long.valueOf(userIdstr);
            ProfitQueryModel profitQueryModel = new ProfitQueryModel();
            profitQueryModel.setUserIdEQ(userId);
            if(type.equals("7")){//返利奖
                profitQueryModel.setProfitTypeEQ(Profit.ProfitType.返利奖);
            }
            if(type.equals("2")){//历史收益
                profitQueryModel.setProfitTypeEQ(Profit.ProfitType.历史收益);
            }
            if(type.equals("3")){//销量奖
                profitQueryModel.setProfitTypeEQ(Profit.ProfitType.销量奖);
            }
            if(type.equals("4")){//特级平级奖
                profitQueryModel.setProfitTypeEQ(Profit.ProfitType.特级平级奖);
            }
            if(type.equals("8")){//董事贡献奖
                profitQueryModel.setProfitTypeEQ(Profit.ProfitType.董事贡献奖);
            }
            if(type.equals("5")){//平级推荐奖
                profitQueryModel.setProfitTypeEQ(Profit.ProfitType.平级推荐奖);
            }
            if(type.equals("6")){//特级推荐奖
                profitQueryModel.setProfitTypeEQ(Profit.ProfitType.推荐奖);
            }
            if(type.equals("1")){//订单收益
                profitQueryModel.setProfitTypeEQ(Profit.ProfitType.订单收款);
            }
            Profit.ProfitType profit = profitQueryModel.getProfitTypeEQ();

            Map<String, Object> returnMap = profitService.queryRevenue(profitQueryModel);
            returnMap.put("type",profit.ordinal());
            returnMap.put("types",profit);

            return ResultBuilder.result(returnMap);
        }catch (Exception e){
            logger.info(e.getMessage());
            e.printStackTrace();
            return ResultBuilder.error("调用失败，请联系管理员");
        }
    }

    @RequestMapping(value = "/revenueDetail", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> revenueDetailAjax(HttpServletRequest request, HttpServletResponse response,String type, String monthstr, String userIdstr) {
        try {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST");
            response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
            if (type == null || "".equals(type) || userIdstr == null || "".equals(userIdstr)||monthstr==null||"".equals(monthstr)) {
                return ResultBuilder.error("参数有错");
            }
            Long userId = Long.valueOf(userIdstr);
            int month = Integer.valueOf(monthstr);
            ProfitQueryModel profitQueryModel = new ProfitQueryModel();
            profitQueryModel.setUserIdEQ(userId);
            if(type.equals("7")){//返利奖
                profitQueryModel.setProfitTypeEQ(Profit.ProfitType.返利奖);
            }
            if(type.equals("2")){//历史收益
                profitQueryModel.setProfitTypeEQ(Profit.ProfitType.历史收益);
            }
            if(type.equals("3")){//销量奖
                profitQueryModel.setProfitTypeEQ(Profit.ProfitType.销量奖);
            }
            if(type.equals("4")){//特级平级奖
                profitQueryModel.setProfitTypeEQ(Profit.ProfitType.特级平级奖);
            }
            if(type.equals("8")){//董事贡献奖
                profitQueryModel.setProfitTypeEQ(Profit.ProfitType.董事贡献奖);
            }
            if(type.equals("5")){//平级推荐奖
                profitQueryModel.setProfitTypeEQ(Profit.ProfitType.平级推荐奖);
            }
            if(type.equals("6")){//特级推荐奖
                profitQueryModel.setProfitTypeEQ(Profit.ProfitType.推荐奖);
            }
            if(type.equals("1")){//订单收益
                profitQueryModel.setProfitTypeEQ(Profit.ProfitType.订单收款);
            }
            profitQueryModel.setMonth(month);
            profitQueryModel.setProfitStatusEQ(Profit.ProfitStatus.已发放);
            List<Profit> orderRevenueDetails = profitService.orderRevenueDetail(profitQueryModel);
            List<ProfitListVo> detailVoPage = orderRevenueDetails.stream().map(v -> {
                return profitComponent.buildListVo(v);
            }).collect(Collectors.toList());
            return ResultBuilder.result(detailVoPage);
        }catch (Exception e){
            e.printStackTrace();
            logger.info(e.getMessage());
            return ResultBuilder.error("调用失败，请联系管理员");
        }
    }

}
