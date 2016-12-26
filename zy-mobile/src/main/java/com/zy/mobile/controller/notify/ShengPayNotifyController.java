package com.zy.mobile.controller.notify;

import com.zy.common.exception.BizException;
import com.zy.common.support.shengpay.PayNotify;
import com.zy.common.support.shengpay.ShengPayClient;
import com.zy.common.util.JsonUtils;
import com.zy.entity.fnc.Deposit;
import com.zy.model.BizCode;
import com.zy.service.DepositService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.zy.common.support.weixinpay.WeixinPayClient.logger;

@Controller
@RequestMapping("/notify/shengPay")
@Slf4j
public class ShengPayNotifyController {

	@Autowired
	private ShengPayClient shengPayClient;

	@Autowired
	private DepositService depositService;

	@RequestMapping
	@ResponseBody
	public String notify(PayNotify payNotify) {
		log.info("enter sheng pay notify controller");
		try {
			if (!shengPayClient.checkPayNotify(payNotify)) {
				throw new BizException(BizCode.ERROR, "签名验证失败:签名不一致");
			}
			if (shengPayClient.isSuccess(payNotify)) {
				String transNo = payNotify.getTransNo();
				String orderNo = payNotify.getOrderNo();
				logger.info("amount " + payNotify.getTransAmount());
				logger.info(JsonUtils.toJson(payNotify));
				Deposit deposit = depositService.findBySn(orderNo);
				depositService.success(deposit.getId(), transNo);
				return "OK";
			} else {
				log.error("支付失败, " + JsonUtils.toJson(payNotify));
				return "ERROR";
			}

		} catch (Throwable throwable) {
			logger.warn("pay error", throwable);
			return "ERROR";
		}
	}

	@RequestMapping("/page")
	@ResponseBody
	public String page(PayNotify payNotify) {
		log.info("enter sheng pay notify controller");
		try {
			if (!shengPayClient.checkPayNotify(payNotify)) {
				throw new BizException(BizCode.ERROR, "签名验证失败:签名不一致");
			}
			if (shengPayClient.isSuccess(payNotify)) {
				String transNo = payNotify.getTransNo();
				String orderNo = payNotify.getOrderNo();
				logger.info("amount " + payNotify.getTransAmount());
				logger.info(JsonUtils.toJson(payNotify));
				Deposit deposit = depositService.findBySn(orderNo);
				depositService.success(deposit.getId(), transNo);
				return "OK";
			} else {
				log.error("支付失败, " + JsonUtils.toJson(payNotify));
				return "ERROR";
			}

		} catch (Throwable throwable) {
			logger.warn("pay error", throwable);
			return "ERROR";
		}
	}

}
