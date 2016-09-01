package com.zy.mobile.extend;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.zy.common.util.CookieUtils;
import com.zy.entity.usr.User;
import com.zy.model.Constants;
import com.zy.service.UserService;

public class InvitationFilter implements Filter {

	final Logger logger = LoggerFactory.getLogger(InvitationFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// nothing to do
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String __u = request.getParameter("__u");
		if(StringUtils.isNotBlank(__u)) {
			CookieUtils.add(response, Constants.COOKIE_NAME_INVITATION, __u, 60*60*24*30, Constants.DOMAIN_COOKIE);
		} else {
			__u = CookieUtils.get(request, Constants.COOKIE_NAME_INVITATION);
		}
		
		Long inviterId = null;
		if(StringUtils.isNotBlank(__u)) {
			try {
				inviterId = Long.valueOf(__u);
			} catch (Exception e) {
				logger.warn("__u参数错误：" + __u);
			}
			
		}
		
		if(inviterId != null) {
			WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
			UserService userService = wac.getBean(UserService.class);
			User inviter = userService.findOne(inviterId);
			request.setAttribute("__inviter", inviter);
		}
		
		chain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {
		// nothing to do
	}

}
