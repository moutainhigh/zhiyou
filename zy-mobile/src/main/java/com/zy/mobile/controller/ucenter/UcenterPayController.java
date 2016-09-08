package com.zy.mobile.controller.ucenter;

import com.zy.common.exception.BizException;
import com.zy.common.extend.BigDecimalBinder;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Deposit;
import com.zy.entity.fnc.Deposit.DepositStatus;
import com.zy.entity.fnc.PayType;
import com.zy.model.BizCode;
import com.zy.model.Principal;
import com.zy.model.query.DepositQueryModel;
import com.zy.service.DepositService;
import com.zy.service.UserService;
import io.gd.generator.api.query.Direction;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.validate;

@Controller
@RequestMapping("/u/pay")
public class UcenterPayController {

	final Logger logger = LoggerFactory.getLogger(UcenterPayController.class);

	@Autowired
	private DepositService depositService;

	@Autowired
	private UserService userService;

	@Autowired
	private WxMpConfigStorage wxMpConfigStorage;

	@Autowired
	private WxMpService wxMpService;

	@RequestMapping
	public String create(@BigDecimalBinder BigDecimal money, @RequestParam PayType payType, Model model, Principal principal, HttpServletRequest request) {

		if (payType != PayType.银行汇款) {
			throw new BizException(BizCode.ERROR, "暂只支持银行汇款");
		}

		final BigDecimal zero = new BigDecimal("0.00");
		String title = "余额充值";
		Long userId = principal.getUserId();
		validate(money, v -> v.compareTo(zero) > 0, "money must be more than 0.00");

		DepositQueryModel depositQueryModel = new DepositQueryModel();
		depositQueryModel.setUserIdEQ(principal.getUserId());
		depositQueryModel.setDepositStatusEQ(DepositStatus.待充值);
		depositQueryModel.setOrderBy("createdTime");
		depositQueryModel.setDirection(Direction.DESC);
		List<Deposit> deposits = depositService.findAll(depositQueryModel);
		Deposit deposit = deposits.stream().filter(v -> v.getPayType() == payType)
				.filter(v -> v.getExpiredTime() == null || v.getExpiredTime().after(new Date()))
				.filter(v -> v.getAmount1().equals(money) && v.getCurrencyType1() == CurrencyType.现金 && v.getCurrencyType2() == null)
				.findFirst().orElse(null);

		if (deposit == null) {
			deposit = new Deposit();
			deposit.setPayType(payType);
			deposit.setCurrencyType1(CurrencyType.金币);
			deposit.setTitle(title);
			deposit.setAmount1(money);
			deposit.setUserId(userId);
			deposit = depositService.create(deposit);
		}

		if (payType == PayType.银行汇款) {
			return "ucenter/currency/balancePay";
		} else {
			return null;
		}

	}



}
