package com.zy.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {
	
	private static final String DEFAULT_CHARSET = "UTF-8";

	public static void add(HttpServletResponse response, String name, String value, int maxAge) {
		add(response, name, value, maxAge, null, true);
	}
	
	public static void add(HttpServletResponse response, String name, String value, int maxAge, String domain) {
		add(response, name, value, maxAge, domain, true);
	}

	private static void add(HttpServletResponse response, String name, String value, int maxAge, String domain, boolean httpOnly) {
		Cookie cookie = new Cookie(name, null);
		cookie.setPath("/");
		cookie.setMaxAge(maxAge);
		cookie.setHttpOnly(httpOnly);
		if(domain != null) {
			cookie.setDomain(domain);
		}
		if(value != null) {
			try {
					cookie.setValue(URLEncoder.encode(value, DEFAULT_CHARSET));
			} catch (UnsupportedEncodingException e) {
				// ignore
			}
		}
		response.addCookie(cookie);
	}
	
	public static String get(HttpServletRequest request, String name) {
		return getAndRemove(request, null, name, false, null);
	}

	public static String getAndRemove(HttpServletRequest request, HttpServletResponse response, String name) {
		return getAndRemove(request, response, name, true, null);
	}
	
	public static String getAndRemoveWithDomain(HttpServletRequest request, HttpServletResponse response, String name, String domain) {
		return getAndRemove(request, response, name, true, domain);
	}
	
	public static void remove(HttpServletRequest request, HttpServletResponse response, String name) {
		getAndRemove(request, response, name, true, null);
	}
	
	public static void removeWithDomain(HttpServletRequest request, HttpServletResponse response, String name, String domain) {
		getAndRemove(request, response, name, true, domain);
	}
	
	private static String getAndRemove(HttpServletRequest request, HttpServletResponse response, String name, boolean isRemove, String removeDomain) {
		String value = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					try {
						value = URLDecoder.decode(cookie.getValue(), DEFAULT_CHARSET);
					} catch (UnsupportedEncodingException e) {
						// ignore
					}
					if (isRemove) {
						cookie.setMaxAge(0);
						if(removeDomain != null) {
							cookie.setDomain(removeDomain);
						}
						response.addCookie(cookie);
					}
				}
			}
		}
		return value;
	}


}
