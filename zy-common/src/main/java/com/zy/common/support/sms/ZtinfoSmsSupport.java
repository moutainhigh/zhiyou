package com.zy.common.support.sms;

import com.zy.common.util.Digests;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ZtinfoSmsSupport implements SmsSupport {

	private static final String URL = "http://www.ztsms.cn:8800/sendNSms.do";

	private static final String PRODUCT_ID_CAPTCHA = "676767";
	private static final String PRODUCT_ID_NOTICE = "887362";
	private static final String PRODUCT_ID_AD = "435227";


	private final String username;

	private final String password;

	private final CloseableHttpClient hc;

	public ZtinfoSmsSupport(String username, String password) {
		Objects.requireNonNull(username, "username must not be null");
		Objects.requireNonNull(username, "password must not be null");
		this.username = username;
		this.password = password;
		this.hc = HttpClients.createDefault();
	}

	public ZtinfoSmsSupport(String username, String password, CloseableHttpClient closeableHttpClient) {
		Objects.requireNonNull(username, "username must not be null");
		Objects.requireNonNull(username, "password must not be null");
		this.username = username;
		this.password = password;
		this.hc = closeableHttpClient;
	}

	public void close() {
		try {
			hc.close();
		} catch (Exception e) {
			// ignore
		}
	}

	@Override
	public SmsResult send(String phone, String message, String sign) {
		SmsResult smsResult = new SmsResult();

		String strtime = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
		String pass = Digests.md5Hex((Digests.md5Hex(password.getBytes(Charset.forName("UTF-8")))+strtime).getBytes(Charset.forName("UTF-8")));

		HttpPost post = new HttpPost(URL);
		List<NameValuePair> nvps = new ArrayList<>();
		nvps.add(new BasicNameValuePair("username", username));
		nvps.add(new BasicNameValuePair("tkey", strtime));
		nvps.add(new BasicNameValuePair("password", pass));
		nvps.add(new BasicNameValuePair("productid", PRODUCT_ID_CAPTCHA));
		nvps.add(new BasicNameValuePair("mobile", phone));
		nvps.add(new BasicNameValuePair("content", message + "【" + sign + "】"));
		nvps.add(new BasicNameValuePair("xh", ""));

		post.setEntity(new UrlEncodedFormEntity(nvps, Charset.forName("UTF-8")));
		try (CloseableHttpResponse resp = hc.execute(post)) {
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = resp.getEntity();
				String r = EntityUtils.toString(entity, "utf8");

			} else {
				throw new Exception("http状态码异常" + resp.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			smsResult.setSuccess(false);
			smsResult.setMessage(e.getMessage());
		}
		return smsResult;
	}



}





	
	
	
