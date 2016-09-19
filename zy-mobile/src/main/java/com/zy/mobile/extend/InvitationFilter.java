package com.zy.mobile.extend;

import com.zy.common.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.zy.model.Constants.*;

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
			CookieUtils.add(response, COOKIE_NAME_INVITATION, __u, 60*60*24*30, DOMAIN_MOBILE);
		} else {
			__u = CookieUtils.get(request, COOKIE_NAME_INVITATION);
		}

		if(StringUtils.isNotBlank(__u)) {
			try {
				Long inviterId = Long.valueOf(__u);
				request.setAttribute(REQUEST_ATTRIBUTE_INVITER_ID, inviterId);
			} catch (Exception e) {
				CookieUtils.removeWithDomain(request, response, COOKIE_NAME_INVITATION, DOMAIN_MOBILE);
			}
			
		}

		chain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {
		// nothing to do
	}

}
