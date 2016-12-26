package com.zy.common.support.shengpay;

import com.zy.common.util.Digests;
import com.zy.common.util.Encodes;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.http.impl.client.CloseableHttpClient;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/26.
 */
public class ShengPayClient {


	public static void main(String[] args) {
		ShengPayClient shengPayClient = new ShengPayClient();
		System.out.println(shengPayClient.genPayCreateHtml("积分充值v22", "test001", new BigDecimal("1.00"), new Date(), "127.0.0.1", "http://zt.net", "http://zt.net", "http://zt.net"));
	}



	public static final String URL_PAY = "https://mas.shengpay.com/web-acquire-channel/cashier.htm";

	private String merchantId = "540505";
	private String key = "support4html5test";

	private CloseableHttpClient httpClient;
	public ShengPayClient() {

	}

	public ShengPayClient(String merchantId, String key, CloseableHttpClient httpClient) {
		this.merchantId = merchantId;
		this.key = key;
		this.httpClient = httpClient;
	}

	private String genPayCreateHtml(String productName, String orderNo, BigDecimal orderAmount, Date orderTime, String buyerIp, String pageUrl, String notifyUrl, String backUrl) {

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
		payCreate.setSignMsg(genPayCreateSign(payCreate));

		return URL_PAY + "?" + payCreate.toMap().entrySet().stream().map(v -> v.getKey() + "=" + Encodes.urlEncode(v.getValue())).reduce((u, v) -> u + "&" + v).get();

	}

	private String genPayCreateSign(PayCreate payCreate) {

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
