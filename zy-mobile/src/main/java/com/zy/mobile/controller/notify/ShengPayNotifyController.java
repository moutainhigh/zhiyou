package com.zy.mobile.controller.notify;

import com.zy.common.exception.BizException;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.support.shengpay.*;
import com.zy.common.util.Encodes;
import com.zy.common.util.JsonUtils;
import com.zy.entity.fnc.Deposit;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.service.DepositService;
import com.zy.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

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
	private PaymentService paymentService;

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
	public String mobileSync(RedirectAttributes redirectAttributes, HttpServletRequest request) {

		String result = request.getParameter("backMessage");
		log.info("enter sheng pay mobile notify controller");
		log.info(result);
		try {

			PayMobileResponse payMobileResponse = JsonUtils.fromJson(result, PayMobileResponse.class);
			log.info(payMobileResponse.getTransStatus());
			if ("01".equals(payMobileResponse.getTransStatus())) {
				Long id = Long.valueOf(payMobileResponse.getOrderNo());
				paymentService.success(id, null);
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
	public String mobileAsync(HttpServletRequest request) {

		log.info("enter sheng pay mobile notify controller");
		PayNotifyMobile payNotify = new PayNotifyMobile();
		String name = request.getParameter("Name");
		String version = request.getParameter("Version");
		String charset = request.getParameter("Charset");
		String traceNo = request.getParameter("TraceNo");
		log.info("name:" + name + ";version:" + version + ";charset:" + charset + ";traceNo:" + traceNo);
		payNotify.setName(name);
		payNotify.setVersion(version);
		payNotify.setCharset(charset);
		payNotify.setTraceNo(traceNo);
		payNotify.setMsgSender(request.getParameter("MsgSender"));
		payNotify.setSendTime(request.getParameter("SendTime"));
		payNotify.setInstCode(request.getParameter("InstCode"));
		payNotify.setOrderNo(request.getParameter("OrderNo"));
		payNotify.setOrderAmount(request.getParameter("OrderAmount"));
		payNotify.setTransNo(request.getParameter("TransNo"));
		payNotify.setTransAmount(request.getParameter("TransAmount"));
		payNotify.setTransStatus(request.getParameter("TransStatus"));
		payNotify.setTransType(request.getParameter("TransType"));
		payNotify.setTransTime(request.getParameter("TransTime"));
		payNotify.setMerchantNo(request.getParameter("MerchantNo"));
		payNotify.setErrorCode(request.getParameter("ErrorCode"));
		payNotify.setErrorMsg(request.getParameter("ErrorMsg"));
		payNotify.setExt1(request.getParameter("Ext1"));
		payNotify.setSignType(request.getParameter("SignType"));
		payNotify.setSignMsg(request.getParameter("SignMsg"));
		log.info(JsonUtils.toJson(payNotify));
		try {
			if (!shengPayMobileClient.checkPayNotify(payNotify)) {
				throw new BizException(BizCode.ERROR, "签名验证失败:签名不一致");
			}
			if (shengPayMobileClient.isSuccess(payNotify)) {
				String transNo = payNotify.getTransNo();
				Long id = Long.valueOf(payNotify.getOrderNo());
				paymentService.success(Long.valueOf(id), transNo);
				log.info("sheng pay nofity success");
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
