package com.zy.mobile.controller.notify;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpPayCallback;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zy.common.exception.BizException;
import com.zy.common.util.BeanUtils;
import com.zy.common.util.JsonUtils;
import com.gc.entity.fnc.Deposit;
import com.gc.model.BizCode;
import com.gc.service.DepositService;

@Controller
@RequestMapping("/notify/weixinPay")
public class WeixinPayNotifyController {

	Logger logger = LoggerFactory.getLogger(WeixinPayNotifyController.class);

	@Autowired
	private WxMpService wxMpService;

	@Autowired
	private DepositService depositService;

	@RequestMapping(produces = "application/xml;charset=utf-8")
	@ResponseBody
	public WxOrderNotifyRes payNotify(@RequestBody String wxOrderNotifyReqStr) {
		logger.info("enter weixin pay notify controller");
		WxMpPayCallback wxMpPayCallback = null;
		WxOrderNotifyRes wxOrderNotifyRes = new WxOrderNotifyRes();
		try {
			wxMpPayCallback = wxMpService.getJSSDKCallbackData(wxOrderNotifyReqStr);
			String sign = wxMpPayCallback.getSign();
			if (StringUtils.isBlank(sign)) {
				throw new BizException(BizCode.ERROR, "签名验证失败:签名为空");
			}
			Map<String, String> mapForSign = BeanUtils.toMap(wxMpPayCallback);
			mapForSign.remove("sign");
			boolean checkSignResult = wxMpService.checkJSSDKCallbackDataSignature(mapForSign, sign);
			if (!checkSignResult) {
				throw new BizException(BizCode.ERROR, "签名验证失败:签名不一致");
			}
			String returnCode = wxMpPayCallback.getReturn_code();
			String resultCode = wxMpPayCallback.getResult_code();
			if ("SUCCESS".equals(returnCode) && "SUCCESS".equals(resultCode)) {
				String totalFee = wxMpPayCallback.getTotal_fee();
				String sn = wxMpPayCallback.getOut_trade_no();
				logger.info("payment fee " + totalFee);
				logger.info(JsonUtils.toJson(wxMpPayCallback));
				Deposit deposit = depositService.findBySn(sn);
				depositService.success(deposit.getId(), null);
			} else {
				throw new BizException(BizCode.ERROR, "支付失败, 状态码错误");
			}
			wxOrderNotifyRes.setReturnCode("SUCCESS");
		} catch (Throwable throwable) {
			logger.warn("pay error:" + wxOrderNotifyReqStr, throwable);
			wxOrderNotifyRes.setReturnCode("FAIL");
			wxOrderNotifyRes.setReturnMsg("错误");
		}
		return wxOrderNotifyRes;
	}

	@XmlRootElement(name = "xml")
	public static class WxOrderNotifyRes {

		private String returnCode; // SUCCESS/FAIL SUCCESS表示商户接收通知成功并校验成功
		private String returnMsg; // 返回信息，如非空，为错误原因

		@XmlElement(name = "return_code")
		public String getReturnCode() {
			return returnCode;
		}

		public void setReturnCode(String returnCode) {
			this.returnCode = returnCode;
		}

		@XmlElement(name = "return_msg")
		public String getReturnMsg() {
			return returnMsg;
		}

		public void setReturnMsg(String returnMsg) {
			this.returnMsg = returnMsg;
		}

	}
}
