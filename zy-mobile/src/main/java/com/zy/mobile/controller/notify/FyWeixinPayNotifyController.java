package com.zy.mobile.controller.notify;

import com.zy.common.exception.BizException;
import com.zy.common.support.fuiou.req.FuiouWeixinPayNotifyReq;
import com.zy.common.support.fuiou.res.FuiouWeixinPayNotifyRes;
import com.zy.common.util.BeanUtils;
import com.zy.common.util.JsonUtils;
import com.zy.entity.fnc.Deposit;
import com.zy.entity.fnc.Payment;
import com.zy.model.BizCode;
import com.zy.service.DepositService;
import com.zy.service.PaymentService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/notify/fyWeixinPay")
public class FyWeixinPayNotifyController {

	Logger logger = LoggerFactory.getLogger(FyWeixinPayNotifyController.class);

	@Autowired
	private DepositService depositService;

	@Autowired
	private PaymentService paymentService;

	@RequestMapping
	@ResponseBody
	public FuiouWeixinPayNotifyRes payNotify(FuiouWeixinPayNotifyReq fuiouWeixinPayNotifyReq, HttpServletResponse response) {
		logger.info("enter fy weixin pay notify controller");
		logger.info("fuiouWeixinPayNotifyReq: " + JsonUtils.toJson(fuiouWeixinPayNotifyReq));
		FuiouWeixinPayNotifyRes fuiouWeixinPayNotifyRes = new FuiouWeixinPayNotifyRes();
		fuiouWeixinPayNotifyRes.setResult("FAIL");
		try {
			String sign = fuiouWeixinPayNotifyReq.getSignMsg();
			logger.info("sign:" + sign);
			if (StringUtils.isBlank(sign)) {
				throw new BizException(BizCode.ERROR, "签名验证失败:签名为空");
			}
			Map<String, String> mapForSign = BeanUtils.toMap(fuiouWeixinPayNotifyReq);
			mapForSign.remove("signMsg");
			boolean checkSignResult = checkSign(mapForSign, sign);
			if (!checkSignResult) {
				throw new BizException(BizCode.ERROR, "签名验证失败:签名不一致");
			}
			String tradeStatus = fuiouWeixinPayNotifyReq.getTradeStatus();
			if ("1".equals(tradeStatus)) {
				String sn = fuiouWeixinPayNotifyReq.getOutOrderNum();
				logger.info("OutOrderNum sn :" + sn);
				if (sn != null && sn.startsWith("ZF")){
					Payment payment = paymentService.findBySn(sn);
					/*String remark = fuiouWeixinPayNotifyReq.getRemark();
					if (StringUtils.isBlank(remark) || !remark.equalsIgnoreCase(payment.getRemark())) {
						throw new BizException(BizCode.ERROR, "备注信息验证失败：备注信息不一致");
					}*/
					paymentService.success(payment.getId(), payment.getOuterSn());
				}
			} else {
				throw new BizException(BizCode.ERROR, "支付失败, 状态码错误");
			}
			fuiouWeixinPayNotifyRes.setResult("SUCCESS");
		} catch (Throwable throwable) {
			fuiouWeixinPayNotifyRes.setResult("FAIL");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		return fuiouWeixinPayNotifyRes;
	}

	private boolean checkSign(Map<String, String> map, String sign) {
		if (map == null || map.isEmpty()) {
			return false;
		}
		Map<String, String> signMap = new TreeMap<>(
				new Comparator<String>() {
					public int compare(String str1, String str2) {
						return str1.compareTo(str2);
					}
				}
		);
		signMap.putAll(map);

		StringBuffer stringBuffer = new StringBuffer();
		for (String key : signMap.keySet()) {
			String value = signMap.get(key);
			if (StringUtils.isNotEmpty(value)) {
				stringBuffer.append("&" + key + "=" + value);
			}
		}
		if (stringBuffer.length() > 0) {
			stringBuffer.deleteCharAt(0);
		}
		String signString = stringBuffer.toString();
		logger.info("mapForSign:" + signString);
		logger.info("sign string:" + DigestUtils.md5Hex(signString).toUpperCase());
		return DigestUtils.md5Hex(signString).toUpperCase().equals(sign);
	}

}
