package com.zy.mobile.controller.notify;

import com.zy.common.exception.BizException;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.support.shengpay.PayNotify;
import com.zy.common.support.shengpay.ShengPayClient;
import com.zy.common.util.JsonUtils;
import com.zy.entity.fnc.Deposit;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.service.DepositService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notify/shengPay")
@Slf4j
public class ShengPayNotifyController {

	@Autowired
	private ShengPayClient shengPayClient;

	@Autowired
	private DepositService depositService;

	@RequestMapping("/sync")
	public String sync(PayNotify payNotify, RedirectAttributes redirectAttributes) {
		log.info("enter sheng pay notify controller");
		try {
			if (!shengPayClient.checkPayNotify(payNotify)) {
				throw new BizException(BizCode.ERROR, "签名验证失败:签名不一致");
			}
			if (shengPayClient.isSuccess(payNotify)) {
				String transNo = payNotify.getTransNo();
				String orderNo = payNotify.getOrderNo();
				log.info("amount " + payNotify.getTransAmount());
				log.info(JsonUtils.toJson(payNotify));
				Deposit deposit = depositService.findBySn(orderNo);
				depositService.success(deposit.getId(), transNo);
				redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("充值成功"));
				return "redirect:/u/money";
			} else {
				throw new BizException(BizCode.ERROR, "未支付成功" + JsonUtils.toJson(payNotify));
			}

		} catch (Throwable throwable) {
			log.error("pay error", throwable);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("充值失败"));
			return "redirect:/u/money";
		}
	}

	@RequestMapping("/async")
	@ResponseBody
	public String async(PayNotify payNotify) {
		log.info("enter sheng pay notify controller");
		try {
			if (!shengPayClient.checkPayNotify(payNotify)) {
				throw new BizException(BizCode.ERROR, "签名验证失败:签名不一致");
			}
			if (shengPayClient.isSuccess(payNotify)) {
				String transNo = payNotify.getTransNo();
				String orderNo = payNotify.getOrderNo();
				log.info("amount " + payNotify.getTransAmount());
				log.info(JsonUtils.toJson(payNotify));
				Deposit deposit = depositService.findBySn(orderNo);
				depositService.success(deposit.getId(), transNo);
				return "OK";
			} else {
				throw new BizException(BizCode.ERROR, "未支付成功" + JsonUtils.toJson(payNotify));
			}

		} catch (Throwable throwable) {
			log.error("pay error", throwable);
			return "ERROR";
		}
	}

}