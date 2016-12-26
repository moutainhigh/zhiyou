package com.zy.common.support.shengpay;

import com.zy.common.util.Digests;
import com.zy.common.util.Encodes;
import org.apache.commons.lang.time.DateFormatUtils;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/26.
 */
public class ShengPayClient {

	public static final String URL_PAY = "https://mas.shengpay.com/web-acquire-channel/cashier.htm";

	private String merchantId;
	private String key;

	public ShengPayClient(String merchantId, String key) {
		this.merchantId = merchantId;
		this.key = key;
	}

	public boolean checkPayNotify(PayNotify payNotify) {
		return true;
	}

	public boolean isSuccess(PayNotify payNotify) {
		return "01".equals(payNotify.getTransStatus());
	}

	public String getPayCreateUrl(String productName, String orderNo, BigDecimal orderAmount, Date orderTime, String buyerIp, String pageUrl, String notifyUrl, String backUrl) {

		PayCreate payCreate = new PayCreate();
		payCreate.setProductName(productName);
		payCreate.setOrderNo(orderNo);
		payCreate.setBuyerIp(buyerIp);
		payCreate.setOrderAmount(orderAmount.setScale(2, BigDecimal.ROUND_UNNECESSARY).toString());
		payCreate.setPageUrl(pageUrl);
		payCreate.setOrderTime(DateFormatUtils.format(orderTime, "yyyyMMddHHmmss"));
		payCreate.setNotifyUrl(notifyUrl);
		payCreate.setBackUrl(backUrl);

		payCreate.setMsgSender(merchantId);
		payCreate.setSignMsg(getPayCreateSign(payCreate));

		return URL_PAY + "?" + payCreate.toMap().entrySet().stream().map(v -> v.getKey() + "=" + Encodes.urlEncode(v.getValue())).reduce((u, v) -> u + "&" + v).get();

	}

	private String getPayCreateSign(PayCreate payCreate) {

		// Origin＝Name + | + Version + | + Charset + | + MsgSender
		// + | + SendTime(忽略) + | + OrderNo + | + OrderAmount + |
		// + OrderTime + | + Currency(忽略) + | + PayType + | + PayChannel(忽略)
		// + | + InstCode(忽略) + | + PageUrl + | + BackUrl + | + NotifyUrl
		// + | + ProductName + | + BuyerContact(忽略) + | + BuyerIp + | +Ext1(忽略) + | + SignType + |；

		String seperator = "|";
		StringBuilder forSign = new StringBuilder();
		forSign.append(payCreate.getName());
		forSign.append(seperator);
		forSign.append(payCreate.getVersion());
		forSign.append(seperator);
		forSign.append(payCreate.getCharset());
		forSign.append(seperator);
		forSign.append(payCreate.getMsgSender());
		forSign.append(seperator);
		forSign.append(payCreate.getOrderNo());
		forSign.append(seperator);
		forSign.append(payCreate.getOrderAmount());
		forSign.append(seperator);
		forSign.append(payCreate.getOrderTime());
		forSign.append(seperator);
		forSign.append(payCreate.getPayType());
		forSign.append(seperator);
		forSign.append(payCreate.getPageUrl());
		forSign.append(seperator);
		forSign.append(payCreate.getBackUrl());
		forSign.append(seperator);
		forSign.append(payCreate.getNotifyUrl());
		forSign.append(seperator);
		forSign.append(payCreate.getProductName());
		forSign.append(seperator);
		forSign.append(payCreate.getBuyerIp());
		forSign.append(seperator);
		forSign.append(payCreate.getSignType());
		forSign.append(seperator);

		forSign.append(key);

		return Encodes.encodeHex(Digests.md5(forSign.toString().getBytes(Charset.forName("UTF-8")))).toUpperCase();
	}
}
