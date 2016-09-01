package com.zy.common.support.alipay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public class AlipayDirect {

	public static final String PAYMENT_URL = "https://mapi.alipay.com/gateway.do?_input_charset=UTF-8";// 支付请求URL
	public String bargainorId;
	public String bargainorKey;

	public AlipayDirect(String bargainorId, String bargainorKey) {
		this.bargainorId = bargainorId;
		this.bargainorKey = bargainorKey;
	}

	public String getPaymentUrl() {
		return PAYMENT_URL;
	}

	public String getOuterSn(HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		String outTradeNo = request.getParameter("trade_no");
		if (StringUtils.isEmpty(outTradeNo)) {
			return null;
		}
		return outTradeNo;
	}

	public String getPaymentSn(HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		String outTradeNo = request.getParameter("out_trade_no");
		if (StringUtils.isEmpty(outTradeNo)) {
			return null;
		}
		return outTradeNo;
	}

	public BigDecimal getPaymentAmount(HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		String totalFee = request.getParameter("total_fee");
		if (StringUtils.isEmpty(totalFee)) {
			return null;
		}
		return new BigDecimal(totalFee);
	}

	public boolean isPaySuccess(HttpServletRequest request) {
		if (request == null) {
			return false;
		}
		String tradeStatus = request.getParameter("trade_status");
		if (StringUtils.equals(tradeStatus, "TRADE_FINISHED") || StringUtils.equals(tradeStatus, "TRADE_SUCCESS")) {
			return true;
		} else {
			return false;
		}
	}

	public Map<String, String> getParameterMap(String paymentSn, BigDecimal paymentAmount, String title, String merchantName, String productUrl,
			String notifyUrl, String returnUrl) {
		String _input_charset = "UTF-8";// 字符集编码格式（UTF-8、GBK）
		String body = paymentSn;// 订单描述
		String defaultbank = "";// 默认选择银行（当paymethod为bankPay时有效）
		String extra_common_param = merchantName;// 商户数据
		String notify_url = notifyUrl;// 消息通知URL
		String out_trade_no = paymentSn;// 支付编号
		String partner = bargainorId;// 合作身份者ID
		String payment_type = "1";// 支付类型（固定值：1）
		String paymethod = "directPay";// 默认支付方式（bankPay：网银、cartoon：卡通、directPay：余额、CASH：网点支付）
		String return_url = returnUrl;// 回调处理URL
		String seller_id = bargainorId;// 商家ID
		String service = "create_direct_pay_by_user";// 接口类型（create_direct_pay_by_user：即时交易）
		String show_url = productUrl; // 商品显示URL
		String sign_type = "MD5";// 签名加密方式（MD5）
		String subject = title;// 订单的名称、标题、关键字等
		String total_fee = paymentAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		;// 总金额（单位：元）
		String key = bargainorKey;// 密钥

		// 生成签名
		Map<String, String> signMap = new LinkedHashMap<String, String>();
		signMap.put("_input_charset", _input_charset);
		signMap.put("body", body);
		signMap.put("defaultbank", defaultbank);
		signMap.put("extra_common_param", extra_common_param);
		signMap.put("notify_url", notify_url);
		signMap.put("out_trade_no", out_trade_no);
		signMap.put("partner", partner);
		signMap.put("payment_type", payment_type);
		signMap.put("paymethod", paymethod);
		signMap.put("return_url", return_url);
		signMap.put("seller_id", seller_id);
		signMap.put("service", service);
		signMap.put("show_url", show_url);
		signMap.put("subject", subject);
		signMap.put("total_fee", total_fee);
		String sign = DigestUtils.md5Hex(getParameterString(signMap) + key);

		// 参数处理
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("body", paymentSn);
		parameterMap.put("defaultbank", defaultbank);
		parameterMap.put("extra_common_param", extra_common_param);
		parameterMap.put("notify_url", notify_url);
		parameterMap.put("out_trade_no", out_trade_no);
		parameterMap.put("partner", partner);
		parameterMap.put("payment_type", payment_type);
		parameterMap.put("paymethod", paymethod);
		parameterMap.put("return_url", return_url);
		parameterMap.put("seller_id", seller_id);
		parameterMap.put("service", service);
		parameterMap.put("show_url", show_url);
		parameterMap.put("sign_type", sign_type);
		parameterMap.put("subject", subject);
		parameterMap.put("total_fee", total_fee);
		parameterMap.put("sign", sign);

		return parameterMap;
	}

	public boolean verifySign(HttpServletRequest request) {
		// 获取参数
		String bankSeqNo = request.getParameter("bank_seq_no");
		String body = request.getParameter("body");
		String buyer_email = request.getParameter("buyer_email");
		String buyer_id = request.getParameter("buyer_id");
		String discount = request.getParameter("discount");
		String exterface = request.getParameter("exterface");
		String extra_common = request.getParameter("extra_common");
		String extra_common_param = request.getParameter("extra_common_param");
		String gmt_close = request.getParameter("gmt_close");
		String gmt_create = request.getParameter("gmt_create");
		String gmt_payment = request.getParameter("gmt_payment");
		String is_total_fee_adjust = request.getParameter("is_total_fee_adjust");
		String is_success = request.getParameter("is_success");
		String notify_id = request.getParameter("notify_id");
		String notify_time = request.getParameter("notify_time");
		String notify_type = request.getParameter("notify_type");
		String out_trade_no = request.getParameter("out_trade_no");
		String payment_type = request.getParameter("payment_type");
		String price = request.getParameter("price");
		String quantity = request.getParameter("quantity");
		String seller_email = request.getParameter("seller_email");
		String seller_id = request.getParameter("seller_id");
		String subject = request.getParameter("subject");
		String total_fee = request.getParameter("total_fee");
		String trade_no = request.getParameter("trade_no");
		String trade_status = request.getParameter("trade_status");
		String use_coupon = request.getParameter("use_coupon");
		String sign = request.getParameter("sign");

		// 验证签名
		Map<String, String> parameterMap = new LinkedHashMap<String, String>();
		parameterMap.put("bank_seq_no", bankSeqNo);
		parameterMap.put("body", body);
		parameterMap.put("buyer_email", buyer_email);
		parameterMap.put("buyer_id", buyer_id);
		parameterMap.put("discount", discount);
		parameterMap.put("exterface", exterface);
		parameterMap.put("extra_common", extra_common);
		parameterMap.put("extra_common_param", extra_common_param);
		parameterMap.put("gmt_close", gmt_close);
		parameterMap.put("gmt_create", gmt_create);
		parameterMap.put("gmt_payment", gmt_payment);
		parameterMap.put("is_total_fee_adjust", is_total_fee_adjust);
		parameterMap.put("is_success", is_success);
		parameterMap.put("notify_id", notify_id);
		parameterMap.put("notify_time", notify_time);
		parameterMap.put("notify_type", notify_type);
		parameterMap.put("out_trade_no", out_trade_no);
		parameterMap.put("payment_type", payment_type);
		parameterMap.put("price", price);
		parameterMap.put("quantity", quantity);
		parameterMap.put("seller_email", seller_email);
		parameterMap.put("seller_id", seller_id);
		parameterMap.put("subject", subject);
		parameterMap.put("total_fee", total_fee);
		parameterMap.put("trade_no", trade_no);
		parameterMap.put("trade_status", trade_status);
		parameterMap.put("use_coupon", use_coupon);
		String forSign = getParameterString(parameterMap) + bargainorKey;
		System.out.println("forSign:" + forSign);
		if (StringUtils.equals(sign, DigestUtils.md5Hex(forSign))) {
			return true;
		} else {
			return false;
		}
	}

	public String getPayNotifyMessage(String paymentSn) {
		return "success";
	}

	private String getParameterString(Map<String, String> parameterMap) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String key : parameterMap.keySet()) {
			String value = parameterMap.get(key);
			if (StringUtils.isNotEmpty(value)) {
				stringBuffer.append("&" + key + "=" + value);
			}
		}
		if (stringBuffer.length() > 0) {
			stringBuffer.deleteCharAt(0);
		}
		return stringBuffer.toString();
	}

	public String getBargainorId() {
		return bargainorId;
	}

	public String getBargainorKey() {
		return bargainorKey;
	}

	public void setBargainorId(String bargainorId) {
		this.bargainorId = bargainorId;
	}

	public void setBargainorKey(String bargainorKey) {
		this.bargainorKey = bargainorKey;
	}

}