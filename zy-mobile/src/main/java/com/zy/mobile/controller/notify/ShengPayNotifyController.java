package com.zy.mobile.controller.notify;

import com.zy.common.exception.BizException;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.support.shengpay.*;
import com.zy.common.util.Encodes;
import com.zy.common.util.JsonUtils;
import com.zy.entity.fnc.Deposit;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.service.ActivityApplyService;
import com.zy.service.ActivityService;
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
	private ShengPayMobileClient shengPayMobileClient;

	@Autowired
	private DepositService depositService;

	@Autowired
	private ActivityApplyService activityApplyService;

	@Autowired
	private ActivityService activityService;

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

	@RequestMapping("/mobile/sync")
	public String mobileSync(RedirectAttributes redirectAttributes, String backMessage) {
		log.info("enter sheng pay mobile notify controller");

		try {

			PayMobileResponse payMobileResponse = JsonUtils.fromJson(backMessage, PayMobileResponse.class);

			if ("01".equals(payMobileResponse.getTransStatus())) {
				Long id = Long.valueOf(payMobileResponse.getOrderNo());
				activityApplyService.success(id, null);
				redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("支付成功"));
			} else {
				redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("支付失败"));
			}
			return "redirect:/u";
		} catch (Throwable throwable) {
			log.error("pay error", throwable);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("支付失败"));
			return "redirect:/u";
		}
	}

	@RequestMapping("/mobile/async")
	@ResponseBody
	public String mobileAsync(PayNotifyMobile payNotify) {
		log.info("enter sheng pay notify controller");
		try {
			if (!shengPayMobileClient.checkPayNotify(payNotify)) {
				throw new BizException(BizCode.ERROR, "签名验证失败:签名不一致");
			}
			if (shengPayMobileClient.isSuccess(payNotify)) {
				String transNo = payNotify.getTransNo();
				String orderNo = payNotify.getOrderNo();
				log.info("amount " + payNotify.getTransAmount());
				log.info(JsonUtils.toJson(payNotify));
				activityApplyService.success(Long.valueOf(orderNo), orderNo);
				return "OK";
			} else {
				throw new BizException(BizCode.ERROR, "未支付成功" + JsonUtils.toJson(payNotify));
			}

		} catch (Throwable throwable) {
			log.error("pay error", throwable);
			return "ERROR";
		}
	}

	public static void main(String[] args) {
		String result = Encodes.urlDecode("backMessage=%7B%22transStatus%22%3A%2201%22%2C%22orderNo%22%3A%2214%22%2C%22msg%22%3A%22%E4%BB%98%E6%AC%BE%E6%88%90%E5%8A%9F%22%7D");
		String backMessage = JsonUtils.toJson(result);
		//result = StringUtils.right(result, result.length()-12);
		//PayMobileResponse payMobileResponse = JsonUtils.fromJson(result, PayMobileResponse.class);
		System.out.println(backMessage);
	}
}
