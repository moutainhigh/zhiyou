package com.zy.common.support.weixinpay;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlElement;

import com.zy.common.util.Digests;
import com.zy.common.util.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WeixinPayUtils {

	public static final Logger logger = LoggerFactory.getLogger(WeixinPayUtils.class);
	
	public static Map<String, String> asMap(Object wxObj) {
		Method[] methods = wxObj.getClass().getDeclaredMethods();
		Map<String, String> map = new TreeMap<>();
		for(Method method : methods) {
			String methodName = method.getName();
			if(methodName.startsWith("get")) {
				XmlElement xmlElement = method.getAnnotation(XmlElement.class);
				if(xmlElement == null) {
					throw new IllegalArgumentException( wxObj.getClass().getName() + ":" + methodName + ",定义错误,@XmlElement缺失");
				}
				Object value;
				try {
					value = method.invoke(wxObj);
					if (value instanceof Date)
						value = new DateAdapter().marshal((Date)value);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				if(value != null && !value.toString().isEmpty()) {
					map.put(xmlElement.name(), value.toString());
				}
			}	
		}
		return map;
	}
	
	public static String generateSign(Map<String, String> map, String key) {
		StringBuffer sb = new StringBuffer();
		for(Map.Entry<String, String> entry : map.entrySet()) {
			if(entry.getKey().equals("sign")) continue;
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		sb.append("key=").append(key);
		String forSign = sb.toString();
		return hash(forSign);
	}
	
	private static String hash(String forSign) {
		logger.info("forSign:" + forSign);
		return Digests.md5Hex(forSign.getBytes(Charset.forName("UTF-8"))).toUpperCase();
	}
	
	public static String toXml(Object wxObj) {
		return XmlUtils.toXml(wxObj);
	}
	
}
