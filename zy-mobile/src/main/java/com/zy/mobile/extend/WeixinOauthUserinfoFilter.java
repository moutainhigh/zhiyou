package com.zy.mobile.extend;

import com.zy.common.model.result.ResultBuilder;
import com.zy.common.util.JsonUtils;
import com.zy.common.util.WebUtils;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.util.GcUtils;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import static com.zy.model.Constants.SESSION_ATTRIBUTE_REDIRECT_URL;


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
			String redirectUrl = GcUtils.resolveRedirectUrl(httpServletRequest);
			session.setAttribute(SESSION_ATTRIBUTE_REDIRECT_URL, redirectUrl);
			String oauthUrl = wxMpService.oauth2buildAuthorizationUrl(redirectUrl, WxConsts.OAUTH2_SCOPE_USER_INFO, Constants.WEIXIN_STATE_USERINFO);

			if (WebUtils.isAjax(httpServletRequest)) {
				OutputStream os = httpServletResponse.getOutputStream();
				httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				httpServletResponse.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
				os.write(JsonUtils.toJson(new ResultBuilder<>().code(BizCode.UNAUTHENTICATED).message("need oauth").data(oauthUrl).build()).getBytes(
						Charset.forName("UTF-8")));
				return;
			} else {

				logger.info("send to userinfo auth: " + oauthUrl);
				httpServletResponse.sendRedirect(oauthUrl);
				return;
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
