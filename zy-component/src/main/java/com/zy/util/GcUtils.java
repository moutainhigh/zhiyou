package com.zy.util;

import static com.zy.model.Constants.ALIYUN_URL_IMAGE;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zy.common.util.Encodes;
import com.zy.common.util.Identities;
import com.zy.model.Constants;
import com.zy.model.Principal;

public class GcUtils {
	
	public static String getHost() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getRemoteAddr();
	}
	
	public static Principal getPrincipal() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		Principal principal = session != null ? (Principal) session.getAttribute(Constants.SESSION_ATTRIBUTE_PRINCIPAL) : null;
		return principal;
	}
	
	public static String resolveRedirectUrl(HttpServletRequest request) {
		String method = request.getMethod();
		String queryString = request.getQueryString();
		String redirectUrl = null;
		if (method.equalsIgnoreCase("GET")) {
			redirectUrl = request.getRequestURL() + (queryString == null ? "" : "?" + queryString);
		} else {
			String referer = request.getHeader("Referer");
			if(StringUtils.isNotBlank(referer)) {
				redirectUrl = referer;
			}
		}
		return redirectUrl;
	}
	
	public static String resolvePassportUrl(HttpServletRequest request) {
		String redirectUrl = resolveRedirectUrl(request);
		if(StringUtils.isBlank(redirectUrl)) {
			return Constants.URL_PASSPORT + "/login?redirect=" + Constants.URL_PC + "/u";
		} else {
			return Constants.URL_PASSPORT + "/login?redirect=" + Encodes.urlEncode(redirectUrl);
		}
	}
	
	public static String getTimeLabel(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		String result = null;
		if(hourOfDay >= 0 && hourOfDay < 6) {
			result = "凌晨";
		} else if(hourOfDay >= 6 && hourOfDay < 12) {
			result = "早上";
		} else if(hourOfDay >= 12 && hourOfDay < 18) {
			result = "下午";
		} else if(hourOfDay >= 18 && hourOfDay < 24) {
			result = "晚上";
		}
		return result;
	}
	
	public static String getDateLabel(Date date, String formatterPattern){
		String fmtDateString = "";
		if(date == null){
			return fmtDateString;
		}
		if(formatterPattern == null){
			formatterPattern = "yyyy-MM-dd";
		}
		Long subTime = new Date().getTime() - date.getTime();
//		long days = subTime / (1000 * 60 * 60 * 24);
        long hours = subTime / (1000 * 60 * 60);
        long minutes = subTime / (1000 * 60);
        if(minutes == 0){
        	fmtDateString = "刚刚";
        } else if(minutes < 60 && minutes > 0){
			fmtDateString = minutes + "分钟前";
		} else if(minutes < 24*60 && minutes > 60){
			fmtDateString = hours + "小时前";
		} else if(minutes <= 1*60*24 && minutes > 60*24){
			fmtDateString = "昨天" + DateFormatUtils.format(date, "HH:mm");
		} else{
			fmtDateString = DateFormatUtils.format(date, formatterPattern);
		}
		return fmtDateString;
	}
	
	public static String getDateLabel(Date date){
		return getDateLabel(date, null);
	}
	
	public static String getBig(String url) {
		return getThumbnail(url, 640, 640);
	}
	
	public static String getThumbnail(String url) {
		return getThumbnail(url, 160, 160);
	}
	
	public static String getThumbnail(String url, int width, int height) {
		return getThumbnail(url, width, height, true, true);
	}
	
	/*
	 * isCut: 是否裁剪
	 * shortFirst: 短边优先
	 * /
	 */
	private static String getThumbnail(String url, int width, int height, boolean isCut, boolean shortFirst) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		if (url.startsWith(ALIYUN_URL_IMAGE)) {
			return url + "@" + height + "h_" + width + "w" + (shortFirst ? "_1e" : "") + (isCut ? "_1c" : "_0c") + ".jpg";
		} else {
			return url;
		}
	}
	
	public static String formatDuration(Long duration) {
		if(duration == null || duration <= 0) {
			return "00:00";
		}
		int hours = (int)(duration / 3600);
		int minutes = (int)(duration / 60 % 60);
		int seconds = (int)(duration % 60);
		
		return (hours > 0 ? (hours < 10 ? "0" : "") + hours + "小时" : "") 
				+ (minutes > 0 ? (minutes < 10 ? "0" : "") + minutes + "分" : "00分") 
				+ (seconds > 0 ? (seconds < 10 ? "0" : "") + seconds + "秒"  : "00秒");
	}

	public static String overlay(String str, int prefixLen, int suffixLen) {
		int totalLen = str.length();
		if (prefixLen + suffixLen >= totalLen) {
			return str;
		} else {
			int padLen = totalLen - prefixLen - suffixLen;
			return StringUtils.overlay(str, StringUtils.leftPad("", padLen, "*"), prefixLen, totalLen - suffixLen);
		}

	}
	
	public static String generateTgt() {
		return "tgt-" + Identities.uuid();
	}
	
}
