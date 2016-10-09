package com.zy.mobile.extend;


import com.zy.common.model.result.ResultBuilder;
import com.zy.common.util.JsonUtils;
import com.zy.common.util.WebUtils;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.util.GcUtils;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
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

import static com.zy.model.Constants.*;


public class AuthenticationFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// nothing to do
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession();
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());

		Principal principal = (Principal) session.getAttribute(SESSION_ATTRIBUTE_PRINCIPAL);
		WxMpService wxMpService  = wac.getBean(WxMpService.class);
		
		if (principal == null) {
			String redirectUrl = GcUtils.resolveRedirectUrl(request);
			session.setAttribute(SESSION_ATTRIBUTE_REDIRECT_URL, redirectUrl);
			String oauthUrl = wxMpService.oauth2buildAuthorizationUrl(WEIXIN_MP_LOGIN_NOTIFY, WxConsts.OAUTH2_SCOPE_USER_INFO, Constants.WEIXIN_STATE_USERINFO);

			if (WebUtils.isAjax(request)) {
				OutputStream os = response.getOutputStream();
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
				os.write(JsonUtils.toJson(new ResultBuilder<>().code(BizCode.UNAUTHENTICATED).message("need oauth").data(oauthUrl).build()).getBytes(
						Charset.forName("UTF-8")));
				return;
			} else {
				String loginUrl = request.getContextPath() + "/login";
				response.sendRedirect(loginUrl);
			}
		} else {
			chain.doFilter(request, response);
		}
		
	}

	@Override
	public void destroy() {
		// nothing to do
	}

}
