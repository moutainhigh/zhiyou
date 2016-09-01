package com.zy.common.support.weixinpay;

import java.util.Map;

import com.zy.common.support.weixinpay.req.WxOrderNotifyReq;
import com.zy.common.support.weixinpay.res.WxOrderRes;
import com.zy.common.util.XmlUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.zy.common.support.weixinpay.req.WxOrderReq;


public class WeixinPayClient {

	public static final String URL_CREATE_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	public String appId;
	public String mchId;
	public String key;
	
	private CloseableHttpClient httpClient = HttpClients.createDefault();

	public static final Logger logger = LoggerFactory.getLogger(WeixinPayClient.class);
	
	public WeixinPayClient(String appId, String mchId, String key) {
		super();
		this.appId = appId;
		this.mchId = mchId;
		this.key = key;
	}

	public WxOrderRes execute(WxOrderReq wxOrderReq) {
		WxOrderRes wxOrderRes = null;
		CloseableHttpResponse response = null;
		try {
			String result = null;
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(9000).
					setConnectTimeout(9000).build();
			HttpPost httpPost = new HttpPost(URL_CREATE_ORDER);
			httpPost.setConfig(requestConfig);
			wxOrderReq.setAppId(appId);
			wxOrderReq.setMchId(mchId);
			Map<String, String> mapToSign = WeixinPayUtils.asMap(wxOrderReq);
			logger.info("map to sign: "+mapToSign);
			String reqSign = WeixinPayUtils.generateSign(mapToSign, key);
			wxOrderReq.setSign(reqSign);
			String reqXml = WeixinPayUtils.toXml(wxOrderReq);
			logger.info("reqXml:" + reqXml);
			
			StringEntity entity = new StringEntity(reqXml, ContentType.create("application/xml", "utf-8"));
			httpPost.setEntity(entity);
			long t1 = System.currentTimeMillis();
			response = httpClient.execute(httpPost);
			long t2 = System.currentTimeMillis();
			logger.info("httpClient.execute()耗时" + (t2-t1));
			Assert.isTrue(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK, "微信服务端错误");
			logger.info("status:" + response.getStatusLine().getStatusCode());
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
			logger.info("wx create order result:" + result);
			wxOrderRes = XmlUtils.fromXml(result, WxOrderRes.class);
			if("SUCCESS".equals(wxOrderRes.getReturnCode())) {
				Map<String, String> map = WeixinPayUtils.asMap(wxOrderRes);
				String resSign = map.get("sign");
				Assert.hasText(resSign, "签名验证失败:签名为空");
				String generatedSign = WeixinPayUtils.generateSign(map, key);
				Assert.isTrue(resSign.equals(generatedSign), "签名验证失败:签名不一致");
			}
		} catch (Exception e) {
			logger.error("error:", e);
			throw new RuntimeException(e);
		}
		return wxOrderRes;
	}
	
	public void validateSign(WxOrderNotifyReq wxOrderNotifyReq) {
		Map<String, String> map = WeixinPayUtils.asMap(wxOrderNotifyReq);
		String sign = map.get("sign");
		Assert.hasText(sign, "签名验证失败:签名为空");
		String generatedSign = WeixinPayUtils.generateSign(map, key);
		Assert.isTrue(sign.equals(generatedSign), "签名验证失败:签名不一致:" + sign + ":" + generatedSign);
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}
