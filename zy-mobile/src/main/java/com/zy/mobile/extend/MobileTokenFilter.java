package com.zy.mobile.extend;

import static com.zy.model.Constants.COOKIE_NAME_MOBILE_TOKEN;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.zy.common.support.cache.CacheSupport;
import com.zy.common.util.CookieUtils;
import com.zy.entity.usr.User;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.PrincipalBuilder;
import com.zy.service.UserService;

public class MobileTokenFilter implements Filter {

	Logger logger = LoggerFactory.getLogger(MobileTokenFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// nothing to do
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession();

		String tgt = CookieUtils.get(request, Constants.COOKIE_NAME_MOBILE_TOKEN);
		Principal principal = (Principal) session.getAttribute(Constants.SESSION_ATTRIBUTE_PRINCIPAL);
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		CacheSupport cacheSupport = wac.getBean(CacheSupport.class);
		UserService userService = wac.getBean(UserService.class);

		if (principal != null) {
			// DO NOTHING
		} else {
			if (StringUtils.isNotBlank(tgt)) {
				Long userId = cacheSupport.get(Constants.CACHE_NAME_TGT, tgt);
				if (userId != null) {
					User user = userService.findOne(userId);
					if (user == null) {
						CookieUtils.remove(request, response, COOKIE_NAME_MOBILE_TOKEN);
					} else {
						principal = PrincipalBuilder.build(userId, tgt);
						session.setAttribute(Constants.SESSION_ATTRIBUTE_PRINCIPAL, principal);
					}
				} else {
					CookieUtils.remove(request, response, COOKIE_NAME_MOBILE_TOKEN);
				}
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// nothing to do
	}

}
