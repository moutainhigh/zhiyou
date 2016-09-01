package com.zy.mobile.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.support.cache.CacheSupport;
import com.zy.common.util.CookieUtils;
import com.gc.model.Constants;

@Controller
@RequestMapping
public class LogoutController {

	Logger logger = LoggerFactory.getLogger(LogoutController.class);
	
	@Autowired
	private CacheSupport cacheSupport;
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpSession session, RedirectAttributes redirectAttributes, HttpServletResponse response) {
		String value = CookieUtils.getAndRemove(request, response, Constants.COOKIE_NAME_MOBILE_TOKEN);
		
		if(value != null) {
			cacheSupport.delete(Constants.CACHE_NAME_TGT, value);
		}
		try {
			session.invalidate();
		} catch (Exception e) {
			// DO NOTHING
		}
		return "redirect:/";
	}
}
