package com.zy.mobile.extend;

import com.zy.common.support.cache.CacheSupport;
import com.zy.common.util.CookieUtils;
import com.zy.entity.usr.User;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.PrincipalBuilder;
import com.zy.model.dto.AgentRegisterDto;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.zy.common.util.ValidateUtils.NOT_BLANK;
import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.model.Constants.SESSION_ATTRIBUTE_AGENT_REGISTER_DTO;
import static com.zy.model.Constants.SESSION_ATTRIBUTE_PRINCIPAL;
import static com.zy.model.Constants.SESSION_ATTRIBUTE_REDIRECT_URL;

public class WeixinCodeValidationFilter implements Filter {

	Logger logger = LoggerFactory.getLogger(WeixinCodeValidationFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		logger.debug("进入validateCode处理, Url=" + httpServletRequest.getRequestURL() + ",Query String=" + httpServletRequest.getQueryString());
		HttpSession session = httpServletRequest.getSession();
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
		WxMpConfigStorage wxMpConfigStorage = wac.getBean(WxMpConfigStorage.class);
		WxMpService wxMpService = new WxMpServiceImpl();
		wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
		UserService userService = wac.getBean(UserService.class);
		CacheSupport cacheSupport = wac.getBean(CacheSupport.class);
		String state = httpServletRequest.getParameter("state");

		Principal principal = (Principal) session.getAttribute(SESSION_ATTRIBUTE_PRINCIPAL);

		if (principal != null || !Constants.WEIXIN_STATE_USERINFO.equals(state)) {
			chain.doFilter(httpServletRequest, httpServletResponse);
		} else if (Constants.WEIXIN_STATE_USERINFO.equals(state)) {
			String code = httpServletRequest.getParameter("code");
			logger.info("code: " + code);
			validate(code, NOT_BLANK, "code is blank");

			try {
				WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
				String openId = wxMpOAuth2AccessToken.getOpenId();
				validate(openId, NOT_BLANK, "open id is blank");

				User user = userService.findByOpenId(openId);
				if (user == null) {
					WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
					AgentRegisterDto agentRegisterDto = new AgentRegisterDto();

					String avatar = wxMpUser.getHeadImgUrl();
					if (StringUtils.isBlank(avatar)) {
						avatar = Constants.SETTING_DEFAULT_AVATAR;
					}
					agentRegisterDto.setAvatar(avatar);
					agentRegisterDto.setOpenId(wxMpUser.getOpenId());
					agentRegisterDto.setNickname(wxMpUser.getNickname());

					session.setAttribute(SESSION_ATTRIBUTE_AGENT_REGISTER_DTO, agentRegisterDto);
					session.setAttribute(SESSION_ATTRIBUTE_REDIRECT_URL, GcUtils.resolveRedirectUrl(httpServletRequest));
					httpServletResponse.sendRedirect(httpServletRequest.getServletPath() + "/register"); // 重定向到注册页面
					return;

				} else {
					Long userId = user.getId();
					String tgt = GcUtils.generateTgt();
					int expire = 60 * 60 * 24 * 7;
					CookieUtils.add(httpServletResponse, Constants.COOKIE_NAME_MOBILE_TOKEN, tgt, expire, Constants.DOMAIN_MOBILE);
					cacheSupport.set(Constants.CACHE_NAME_TGT, tgt, userId, expire);
					session.setAttribute(SESSION_ATTRIBUTE_PRINCIPAL, PrincipalBuilder.build(userId, tgt));
					logger.info("login success, tgt:" + tgt);
				}

			} catch (Exception e) {
				logger.error("weixinCodeValidationFilter error", e);
				throw new RuntimeException(e);
			}
			chain.doFilter(httpServletRequest, httpServletResponse);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

}
