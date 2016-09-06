package com.zy.common.support.sms;

import com.zy.common.util.Encodes;
import com.zy.common.util.JsonUtils;
import org.apache.commons.lang.StringUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LuosimaoSmsSupport implements SmsSupport {

	/*
	错误码 错误描述 解决方案
-10 验证信息失败 检查api key是否正确，编码格式是否正确
-20 短信余额不足 进入个人中心购买充值
-30 短信内容为空 检查调用传入参数：message
-31 短信内容存在敏感词 修改短信内容，更换词语
-32 短信内容缺少签名信息 短信内容末尾增加签名信息，格式为：【公司名称】
-40 错误的手机号 检查手机号是否正确
-41 号码在黑名单中 号码因频繁发送或其他原因暂停发送，请联系客服确认
-42 验证码类短信发送频率过快 前台增加60秒获取限制
-50 请求发送IP不在白名单内 查看触发短信IP白名单的设置
*/

	private final String URL = "http://sms-api.luosimao.com/v1/send.json";

	private final String apiKey;

	private final CloseableHttpClient hc;

	public LuosimaoSmsSupport(String apiKey) {
		Objects.requireNonNull(apiKey, "apiKey must not be null");
		this.apiKey = apiKey;
		this.hc = HttpClients.createDefault();
	}

	public LuosimaoSmsSupport(String apiKey, CloseableHttpClient closeableHttpClient) {
		Objects.requireNonNull(apiKey, "closeableHttpClient must not be null");
		this.apiKey = apiKey;
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
		LuosimaoSmsResult luosimaoSmsResult = null;

		HttpPost post = new HttpPost(URL);
		String authorization = "Basic " + Encodes.encodeBase64(("api:" + apiKey).getBytes(Charset.forName("UTF-8")));
		post.addHeader("Authorization", authorization);
		List<NameValuePair> nvps = new ArrayList<>();
		nvps.add(new BasicNameValuePair("mobile", phone));
		nvps.add(new BasicNameValuePair("message", message + "【" + sign + "】"));
		post.setEntity(new UrlEncodedFormEntity(nvps, Charset.forName("UTF-8")));
		try (CloseableHttpResponse resp = hc.execute(post)) {
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = resp.getEntity();
				String r = EntityUtils.toString(entity, "utf8");
				luosimaoSmsResult = JsonUtils.fromJson(r, LuosimaoSmsResult.class);
				int error = luosimaoSmsResult.getError();
				String msg = luosimaoSmsResult.getMsg();
				String hit = luosimaoSmsResult.getHit();
				if (error == 0) {
					smsResult.setSuccess(true);
				} else {
					smsResult.setSuccess(false);
					smsResult.setMessage("错误码" + error + ",原因" + luosimaoSmsResult.getMsg() + (StringUtils.isNotBlank(hit) ? ",敏感词" + hit : ""));
				}
			} else {
				throw new Exception("http状态码异常" + resp.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			smsResult.setSuccess(false);
			smsResult.setMessage(e.getMessage());
		}
		return smsResult;
	}

	public static class LuosimaoSmsResult {
		private int error;
		private String msg;
		private String batch_id;
		private String hit;

		public int getError() {
			return error;
		}

		public void setError(int error) {
			this.error = error;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public String getBatch_id() {
			return batch_id;
		}

		public void setBatch_id(String batch_id) {
			this.batch_id = batch_id;
		}

		public String getHit() {
			return hit;
		}

		public void setHit(String hit) {
			this.hit = hit;
		}
	}

}





	
	
	
