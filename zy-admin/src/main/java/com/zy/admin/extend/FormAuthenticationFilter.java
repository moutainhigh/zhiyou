package com.zy.admin.extend;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.zy.common.support.cache.CacheSupport;
import com.zy.model.Constants;

public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

	private static final Logger log = LoggerFactory.getLogger(FormAuthenticationFilter.class);
	
	public static final String DEFAULT_CAPTCHA_PARAM = "captcha";
	
	private String captchaParam = DEFAULT_CAPTCHA_PARAM;
	
	@Autowired
	private CacheSupport cacheSupport;

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}

	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		String username = getUsername(request);
		String password = getPassword(request);
		boolean rememberMe = isRememberMe(request);
		String host = getHost(request);
		String captcha = getCaptcha(request);
		return new UsernamePasswordCaptchaToken(StringEscapeUtils.escapeHtml4(username), StringEscapeUtils.escapeHtml4(password), rememberMe, host, captcha);
	}

	@Override
	@Transactional
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		String username = ((UsernamePasswordCaptchaToken)token).getUsername();
		try {
			cacheSupport.delete(Constants.CACHE_NAME_LOGIN_FAILURE_TIMES, username);
		} catch (Exception e) {
			// do notiong
		}
		String returnUrl = "/";
		WebUtils.redirectToSavedRequest(request, response, returnUrl);
		return false;
	}
	
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		UsernamePasswordCaptchaToken usernamePasswordCaptchaToken = (UsernamePasswordCaptchaToken)token;
		String username = usernamePasswordCaptchaToken.getUsername();
		Integer failureTimes = cacheSupport.get(Constants.CACHE_NAME_LOGIN_FAILURE_TIMES, username);
		if (failureTimes == null) {
			failureTimes = 0;
		}
		cacheSupport.set(Constants.CACHE_NAME_LOGIN_FAILURE_TIMES, username, ++failureTimes);
		
		// 判断最大登陆失败次数 如果大于预设值则显示验证码
		String failureInfo = "";

		if (e instanceof IncorrectCaptchaException) {
			failureInfo = "验证码错误";
		} else {
			failureInfo = "账号或者密码错误";
		}
		request.setAttribute("failureInfo", failureInfo);
		request.setAttribute(DEFAULT_USERNAME_PARAM, username);

		Integer loginFailureValidateTimes = Constants.SETTING_LOGIN_FAILURE_VALIDATE_TIMES;
		if (failureTimes != null && failureTimes > loginFailureValidateTimes) {
			request.setAttribute("hasCaptcha", true);
		}
		
		return super.onLoginFailure(token, e, request, response);
	}
	
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest localHttpServletRequest = (HttpServletRequest) request;
		HttpServletResponse localHttpServletResponse = (HttpServletResponse) response;
		String str = localHttpServletRequest.getHeader("X-Requested-With");
		if ((str != null) && (str.equalsIgnoreCase("XMLHttpRequest"))) {
			localHttpServletResponse.sendError(401);
			return false;
		}
		if (isLoginRequest(request, response)) {
			if (isLoginSubmission(request, response)) {
				if (log.isTraceEnabled()) {
					log.trace("Login submission detected.  Attempting to execute login.");
				}
				return executeLogin(request, response);
			} else {
				if (log.isTraceEnabled()) {
					log.trace("Login page view.");
				}
				// allow them to see the login page ;)
				return true;
			}
		} else {
			if (log.isTraceEnabled()) {
				log.trace("Attempting to access a path which requires authentication.  Forwarding to the "
				    + "Authentication url [" + getLoginUrl() + "]");
			}
			redirectToLogin(request, response);
			return false;
		}
	}
	

	public String getCaptchaParam() {
		return captchaParam;
	}

	public void setCaptchaParam(String captchaParam) {
		this.captchaParam = captchaParam;
	}
	

}