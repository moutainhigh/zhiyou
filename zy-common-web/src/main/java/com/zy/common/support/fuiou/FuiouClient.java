package com.zy.common.support.fuiou;

import com.zy.common.support.fuiou.req.FuiouWeixinPayReq;
import com.zy.common.support.fuiou.res.FuiouWeixinPayRes;
import com.zy.common.util.JsonUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FuiouClient {

	public static final Logger logger = LoggerFactory.getLogger(FuiouClient.class);

	public static final String PAYMENT_URL = "http://121.40.64.120:9090/ZFTiming/api/fyTrade/WxTKPay";// 支付请求URL
	public final String fyMerchantCD;
	private final CloseableHttpClient closeableHttpClient;

	public FuiouClient(String fyMerchantCD, CloseableHttpClient closeableHttpClient) {
		super();
		this.fyMerchantCD = fyMerchantCD;
		this.closeableHttpClient = closeableHttpClient;
	}

	public FuiouWeixinPayRes getWeixinPayInfo(FuiouWeixinPayReq fuiouWeixinPayReq) {
		FuiouWeixinPayRes fuiouWeixinPayRes = null;
		CloseableHttpResponse response = null;
		logger.error("fyMerchantCD:" + fyMerchantCD);
		fuiouWeixinPayReq.setFyMerchantCD(fyMerchantCD);
		fuiouWeixinPayReq.setSignMsg(getSign(fuiouWeixinPayReq));
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(9000)
					.setConnectTimeout(9000).build();
			HttpPost httpPost = new HttpPost(PAYMENT_URL);
			httpPost.setConfig(requestConfig);
			List<NameValuePair> nameValuePairs = new ArrayList<>();
			nameValuePairs.add(new BasicNameValuePair("fyMerchantCD", fuiouWeixinPayReq.getFyMerchantCD()));
			nameValuePairs.add(new BasicNameValuePair("outOrderNum", fuiouWeixinPayReq.getOutOrderNum()));
			nameValuePairs.add(new BasicNameValuePair("term_id", fuiouWeixinPayReq.getTerm_id()));
			nameValuePairs.add(new BasicNameValuePair("tranAmt", fuiouWeixinPayReq.getTranAmt()));
			nameValuePairs.add(new BasicNameValuePair("SignMsg", fuiouWeixinPayReq.getSignMsg()));
			nameValuePairs.add(new BasicNameValuePair("mchCreateIp", fuiouWeixinPayReq.getMchCreateIp()));
			nameValuePairs.add(new BasicNameValuePair("phone", fuiouWeixinPayReq.getPhone()));
			nameValuePairs.add(new BasicNameValuePair("openid", fuiouWeixinPayReq.getOpenid()));
			nameValuePairs.add(new BasicNameValuePair("notifyUrl", fuiouWeixinPayReq.getNotifyUrl()));
			nameValuePairs.add(new BasicNameValuePair("remark", fuiouWeixinPayReq.getRemark()));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, Charset.forName("UTF-8")));
			long t1 = System.currentTimeMillis();
			response = closeableHttpClient.execute(httpPost);
			long t2 = System.currentTimeMillis();
			logger.info("httpClient.execute()耗时" + (t2-t1));

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity resultEntity = response.getEntity();
				String result = EntityUtils.toString(resultEntity, "UTF-8");
				if (StringUtils.isNotBlank(result)) {
					fuiouWeixinPayRes = JsonUtils.fromJson(result, FuiouWeixinPayRes.class);
				}
			} else {
				throw new Exception("http状态码异常" + response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			logger.error("error:", e);
			throw new RuntimeException(e);
		}
		return fuiouWeixinPayRes;
	}

	private String getSign(FuiouWeixinPayReq fuiouWeixinPayReq) {
		Map<String, String> signMap = new LinkedHashMap<>();
		signMap.put("fyMerchantCD", fuiouWeixinPayReq.getFyMerchantCD());
		signMap.put("mchCreateIp", fuiouWeixinPayReq.getMchCreateIp());
		signMap.put("notifyUrl", fuiouWeixinPayReq.getNotifyUrl());
		signMap.put("openid", fuiouWeixinPayReq.getOpenid());
		signMap.put("outOrderNum", fuiouWeixinPayReq.getOutOrderNum());
		signMap.put("phone", fuiouWeixinPayReq.getPhone());
		signMap.put("remark", fuiouWeixinPayReq.getRemark());
		signMap.put("term_id", fuiouWeixinPayReq.getTerm_id());
		signMap.put("tranAmt", fuiouWeixinPayReq.getTranAmt());

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
		System.out.println(signString);
		return DigestUtils.md5Hex(signString).toUpperCase();
	}
	
}
