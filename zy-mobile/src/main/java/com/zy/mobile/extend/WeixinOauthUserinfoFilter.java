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
import javax.servlet.http.HttpSession;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.zy.common.util.WebUtils;
import com.gc.model.Constants;
import com.gc.model.Principal;
import com.zy.util.GcUtils;


public class WeixinOauthUserinfoFilter implements Filter {

	Logger logger = LoggerFactory.getLogger(WeixinOauthUserinfoFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// nothing to do
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		
		HttpSession session = httpServletRequest.getSession();
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
		WxMpConfigStorage wxMpConfigStorage = wac.getBean(WxMpConfigStorage.class);
		WxMpService wxMpService = new WxMpServiceImpl();
		wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
		
		Principal principal = (Principal) session.getAttribute(Constants.SESSION_ATTRIBUTE_PRINCIPAL);

		if (principal == null) {
			if (WebUtils.isAjax(httpServletRequest)) {
				httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED); // 401
			} else {
				String url = wxMpService.oauth2buildAuthorizationUrl(GcUtils.resolveRedirectUrl(httpServletRequest), WxConsts.OAUTH2_SCOPE_USER_INFO, Constants.WEIXIN_STATE_USERINFO);
				logger.info("send to userinfo auth: " + url);
				httpServletResponse.sendRedirect(url);
	
			}
		} else {
			chain.doFilter(httpServletRequest, response);
		}
	}

	@Override
	public void destroy() {
		// nothing to do
	}

}
