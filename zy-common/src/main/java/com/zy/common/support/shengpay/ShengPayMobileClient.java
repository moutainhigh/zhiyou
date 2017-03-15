package com.zy.common.support.shengpay;

import com.zy.common.util.Digests;
import com.zy.common.util.Encodes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created by Administrator on 2017/3/9.
 */
@Slf4j
public class ShengPayMobileClient {

	private String merchantId;
	private String key;

	public ShengPayMobileClient(String merchantId, String key) {
		this.merchantId = merchantId;
		this.key = key;
	}

	public boolean checkPayNotify(PayNotifyMobile payNotify) {

		// 支付通知签名原始串是：Origin = Name +|+ Version +|+ Charset +|+ TraceNo +|+ MsgSender +|+ SendTime +|+
		// InstCode +|+ OrderNo +|+ OrderAmount +|+ TransNo +|+ TransAmount +|+ TransStatus
		// +|+ TransType +|+ TransTime +|+ MerchantNo +|+ ErrorCode +|+ ErrorMsg +|+ Ext1 +|+
		// SignType +|;

		String s = "|";
		StringBuilder forSign = new StringBuilder();
		forSign.append(payNotify.getName());
		forSign.append(s);
		forSign.append(payNotify.getVersion());
		forSign.append(s);
		forSign.append(payNotify.getCharset());
		forSign.append(s);
		forSign.append(payNotify.getTraceNo());
		forSign.append(s);
		forSign.append(payNotify.getMsgSender());
		forSign.append(s);
		forSign.append(payNotify.getSendTime());
		forSign.append(s);
		forSign.append(payNotify.getInstCode());
		forSign.append(s);
		forSign.append(payNotify.getOrderNo());
		forSign.append(s);
		forSign.append(payNotify.getOrderAmount());
		forSign.append(s);
		forSign.append(payNotify.getTransNo());
		forSign.append(s);
		forSign.append(payNotify.getTransAmount());
		forSign.append(s);
		forSign.append(payNotify.getTransStatus());
		forSign.append(s);
		forSign.append(payNotify.getTransType());
		forSign.append(s);
		forSign.append(payNotify.getTransTime());
		forSign.append(s);
		forSign.append(payNotify.getMerchantNo());
		forSign.append(s);
		forSign.append(payNotify.getErrorCode());
		forSign.append(s);
		forSign.append(payNotify.getErrorMsg());
		forSign.append(s);
		forSign.append(payNotify.getExt1());
		forSign.append(s);
		forSign.append(payNotify.getSignType());
		forSign.append(s);

		forSign.append(key);
		log.info("sheng pay notify for sign: " + forSign.toString());
		String signed = Encodes.encodeHex(Digests.md5(forSign.toString().getBytes(Charset.forName("UTF-8")))).toUpperCase();
		log.info("sheng pay notify sign: " + signed + "; pay nofity signMsg:" + payNotify.getSignMsg());
		if (signed.equals(payNotify.getSignMsg())) {
			return true;
		} else {
			return false;
		}

	}

	public boolean isSuccess(PayNotifyMobile payNotify) {
		return "01".equals(payNotify.getTransStatus());
	}

	public PayCreateMobile getPayCreateUrl(Date requestTime, Long outMemberId, Date outMemberRegistTime, String outMemberRegistIP
			, String outMemberVerifyStatus, String outMemberName, String outMemberMobile, Long merchantOrderNo, String productName
			, BigDecimal amount, String pageUrl, String notifyUrl, String userIP) {

		PayCreateMobile payCreate = new PayCreateMobile();
		payCreate.setMerchantNo(merchantId);
		payCreate.setRequestTime(DateFormatUtils.format(requestTime, "yyyyMMddHHmmss"));
		payCreate.setOutMemberId(String.valueOf(outMemberId));
		payCreate.setOutMemberRegistTime(DateFormatUtils.format(outMemberRegistTime, "yyyyMMddHHmmss"));
		payCreate.setOutMemberRegistIP(outMemberRegistIP);
		payCreate.setOutMemberVerifyStatus(outMemberVerifyStatus);
		payCreate.setOutMemberName(outMemberName);
		payCreate.setOutMemberMobile(outMemberMobile);
		payCreate.setMerchantOrderNo(String.valueOf(merchantOrderNo));
		payCreate.setProductName(productName);
		payCreate.setAmount(amount.setScale(2, BigDecimal.ROUND_UNNECESSARY).toString());
		payCreate.setPageUrl(pageUrl);
		payCreate.setNotifyUrl(notifyUrl);
		payCreate.setUserIP(userIP);

		payCreate.setSignMsg(getPayCreateSign(payCreate));

		//return URL_PAY + "&" + payCreate.toMap().entrySet().stream().map(v -> v.getKey() + "=" + Encodes.urlEncode(v.getValue())).reduce((u, v) -> u + "&" + v).get();
		return payCreate;
	}

	private String getPayCreateSign(PayCreateMobile payCreate) {

		// Origin ＝ merchantNo+|+ charset(忽略)+|+ requestTime+|+ outMemberId+|+
		// outMemberRegistTime+|+ outMemberRegistIP+|+ outMemberVerifyStatus+|+
		// outMemberName+|+ outMemberMobile+|+ merchantOrderNo+|+ productName+|+
		// productDesc(忽略)+|+ currency+|+ amount+|+ pageUrl+|+ notifyUrl+|+ userIP+|+
		//bankCardType(忽略)+|+ bankCode(忽略)+|+ exts(忽略)+|+signType+|；

		String seperator = "|";
		StringBuilder forSign = new StringBuilder();
		forSign.append(payCreate.getMerchantNo());
		forSign.append(seperator);
		forSign.append(payCreate.getCharset());
		forSign.append(seperator);
		forSign.append(payCreate.getRequestTime());
		forSign.append(seperator);
		forSign.append(payCreate.getOutMemberId());
		forSign.append(seperator);
		forSign.append(payCreate.getOutMemberRegistTime());
		forSign.append(seperator);
		forSign.append(payCreate.getOutMemberRegistIP());
		forSign.append(seperator);
		forSign.append(payCreate.getOutMemberVerifyStatus());
		forSign.append(seperator);
		forSign.append(payCreate.getOutMemberName());
		forSign.append(seperator);
		forSign.append(payCreate.getOutMemberMobile());
		forSign.append(seperator);
		forSign.append(payCreate.getMerchantOrderNo());
		forSign.append(seperator);
		forSign.append(payCreate.getProductName());
		forSign.append(seperator);
		forSign.append(payCreate.getCurrency());
		forSign.append(seperator);
		forSign.append(payCreate.getAmount());
		forSign.append(seperator);
		forSign.append(payCreate.getPageUrl());
		forSign.append(seperator);
		forSign.append(payCreate.getNotifyUrl());
		forSign.append(seperator);
		forSign.append(payCreate.getUserIP());
		forSign.append(seperator);
//		forSign.append(payCreate.getExts());
//		forSign.append(seperator);
		forSign.append(payCreate.getSignType());
		forSign.append(seperator);

		forSign.append(key);
		return Encodes.encodeHex(Digests.md5(forSign.toString().getBytes(Charset.forName("UTF-8")))).toUpperCase();
	}

}
