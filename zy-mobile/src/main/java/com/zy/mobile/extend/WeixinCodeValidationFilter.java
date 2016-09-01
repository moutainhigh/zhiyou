package com.zy.mobile.extend;

import static com.zy.common.util.ValidateUtils.NOT_BLANK;
import static com.zy.common.util.ValidateUtils.validate;

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

import com.zy.common.support.cache.CacheSupport;
import com.zy.common.util.CookieUtils;
import com.gc.entity.usr.User;
import com.gc.entity.usr.WeixinUser;
import com.gc.model.Constants;
import com.gc.model.Principal;
import com.gc.model.PrincipalBuilder;
import com.gc.service.UserService;
import com.gc.service.WeixinUserService;
import com.zy.util.GcUtils;

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
		WeixinUserService weixinUserService = wac.getBean(WeixinUserService.class);
		CacheSupport cacheSupport = wac.getBean(CacheSupport.class);
		String state = httpServletRequest.getParameter("state");

		Principal principal = (Principal) session.getAttribute(Constants.SESSION_ATTRIBUTE_PRINCIPAL);

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

				WeixinUser weixinUser = weixinUserService.findByOpenId(openId);
				User user = null;
				if (weixinUser == null) {
					WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
					String avatar = wxMpUser.getHeadImgUrl();
					if (StringUtils.isBlank(avatar)) {
						avatar = Constants.SETTING_DEFAULT_AVATAR;
					}
					weixinUser = new WeixinUser();
					weixinUser.setAvatar(avatar);
					weixinUser.setOpenId(wxMpUser.getOpenId());
					weixinUser.setUnionId(wxMpUser.getUnionId());
					weixinUser.setNickname(wxMpUser.getNickname());
					weixinUser.setUserId(null);

					User inviter = (User) request.getAttribute("__inviter");
					Long inviterId = null;
					if (inviter != null) {
						inviterId = inviter.getId();
					}
					String registerIp = GcUtils.getHost();
					logger.info("register buyer");
					user = userService.registerBuyer(weixinUser, registerIp, inviterId);
					weixinUser = weixinUserService.findByUserId(user.getId());
				} else {
					user = userService.findByOpenId(openId);
				}
				Long userId = user.getId();
				String tgt = GcUtils.generateTgt();
				int expire = 60 * 60 * 24 * 7;
				CookieUtils.add(httpServletResponse, Constants.COOKIE_NAME_MOBILE_TOKEN, tgt, expire, Constants.DOMAIN_MOBILE);
				cacheSupport.set(Constants.CACHE_NAME_TGT, tgt, userId, expire);
				session.setAttribute(Constants.SESSION_ATTRIBUTE_PRINCIPAL, PrincipalBuilder.build(userId, tgt));
				logger.info("login success, tgt:" + tgt);
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
