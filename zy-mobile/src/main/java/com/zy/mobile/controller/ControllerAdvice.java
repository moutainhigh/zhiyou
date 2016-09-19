package com.zy.mobile.controller;

import static com.zy.model.Constants.SESSION_ATTRIBUTE_REDIRECT_URL;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.zy.common.exception.BizException;
import com.zy.common.exception.UnauthenticatedException;
import com.zy.common.exception.UnauthorizedException;
import com.zy.common.exception.ValidationException;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.util.JsonUtils;
import com.zy.common.util.WebUtils;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.util.GcUtils;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.mp.api.WxMpService;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

	private Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);

	@Autowired
	private WxMpService wxMpService;

	@ModelAttribute
	void pre(HttpServletRequest request, Model model) {
		logger.debug("REQUEST getServletPath = " + request.getServletPath());

		Principal principal = GcUtils.getPrincipal();
		String requestUrl = request.getServletPath().toString();
		if (requestUrl.startsWith("/activity")) {
			String url = request.getRequestURL().toString();
			String queryStr = request.getQueryString();
			/*if (principal != null) {
				Long userId = principal.getUserId();
				if (queryStr != null){
					url += "?" + queryStr + "&__u=" + userId;
				} else {
					url += "?__u=" + userId;
				}
			} else {
				if (queryStr != null){
					url += "?" + queryStr;
				}
			}*/
			if (queryStr != null){
				url += "?" + queryStr;
			} else {
				url += "?" + "a=1&b=2";
			}
			
			model.addAttribute("url", url);
			model.addAttribute("weixinJsModel", getWeixinJsModel(url));
		}
	}

	private Map<String, String> getWeixinJsModel(String url) {
		WxJsapiSignature wxJsapiSignature;
		Map<String, String> params = new HashMap<>();
		try {
			wxJsapiSignature = wxMpService.createJsapiSignature(url);
			params.put("appId", wxJsapiSignature.getAppid());
			params.put("timestamp", wxJsapiSignature.getTimestamp() + "");
			params.put("nonceStr", wxJsapiSignature.getNoncestr());
			params.put("signature", wxJsapiSignature.getSignature());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return params;
	}
	
	@ExceptionHandler(UnauthenticatedException.class)
	public String handleUnauthenticatedException(UnauthenticatedException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("UnauthenticatedException");

		String redirectUrl = GcUtils.resolveRedirectUrl(request);
		request.getSession().setAttribute(SESSION_ATTRIBUTE_REDIRECT_URL, redirectUrl);
		String oauthUrl = wxMpService.oauth2buildAuthorizationUrl(redirectUrl, WxConsts.OAUTH2_SCOPE_USER_INFO,
				Constants.WEIXIN_STATE_USERINFO);
		if (WebUtils.isAjax(request)) {
			OutputStream os = response.getOutputStream();
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			os.write(JsonUtils.toJson(new ResultBuilder<>().code(BizCode.UNAUTHENTICATED).message("need oauth").data(oauthUrl).build()).getBytes(
					Charset.forName("UTF-8")));
			return null;
		} else {
			logger.info("send to userinfo auth: " + oauthUrl);
			response.sendRedirect(oauthUrl);
			return null;
		}
	}

	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public String handleUnauthorizedException(UnauthorizedException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.error("无权限", e);
		if (WebUtils.isAjax(request)) {
			return null;
		} else {
			return "error/403";
		}
	}

	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleValidationException(ValidationException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.error(
				"参数异常IP ADDR = " + request.getRemoteHost() + "REQUEST URI =" + request.getRequestURI() + "REQUEST REFERER = "
						+ request.getHeader(HttpHeaders.REFERER), e);
		if (WebUtils.isAjax(request)) {
			OutputStream os = response.getOutputStream();
			response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			os.write(JsonUtils.toJson(new ResultBuilder<>().code(BizCode.BAD_REQUEST).message(e.getMessage()).build()).getBytes(Charset.forName("UTF-8")));
			return null;
		} else {
			request.setAttribute("errorMsg", "请求参数错误," + e.getMessage());
			return "error/400";
		}
	}

	@ExceptionHandler(BizException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleBizException(BizException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.error("业务异常", e);
		if (WebUtils.isAjax(request)) {
			OutputStream os = response.getOutputStream();
			response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			os.write(JsonUtils.toJson(new ResultBuilder<>().code(e.getCode()).message(e.getMessage()).data(e.getData()).build()).getBytes(
					Charset.forName("UTF-8")));
			return null;
		} else {
			return "error/500";
		}
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.error(
				"其他异常IP ADDR = " + request.getRemoteHost() + "REQUEST URI =" + request.getRequestURI() + "REQUEST REFERER = "
						+ request.getHeader(HttpHeaders.REFERER), e);
		if (WebUtils.isAjax(request)) {
			OutputStream os = response.getOutputStream();
			response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			os.write(JsonUtils.toJson(new ResultBuilder<>().code(BizCode.ERROR).message(e.getMessage()).build()).getBytes(Charset.forName("UTF-8")));
			return null;
		} else {
			return "error/500";
		}
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(StringUtils.isEmpty(text) ? null : StringEscapeUtils.escapeXml(text));
			}

			/*
			 * @Override public String getAsText() { Object value = getValue();
			 * return value != null ? value.toString() : ""; }
			 */
		});
	}

}
