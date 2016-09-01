package com.zy.mobile.controller;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

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
import com.gc.model.BizCode;
import com.gc.model.Constants;
import com.gc.model.Principal;
import com.zy.util.GcUtils;

import me.chanjar.weixin.common.api.WxConsts;
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
		if (principal != null) {

		}

		String requestUrl = request.getServletPath().toString();
		if (requestUrl.startsWith("/u")) {

		}

	}

	@ExceptionHandler(UnauthenticatedException.class)
	public String handleUnauthenticatedException(UnauthenticatedException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("UnauthenticatedException");
		if (WebUtils.isAjax(request)) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return null;
		} else {
			String url = wxMpService.oauth2buildAuthorizationUrl(GcUtils.resolveRedirectUrl(request), WxConsts.OAUTH2_SCOPE_USER_INFO,
					Constants.WEIXIN_STATE_USERINFO);
			logger.info("send to userinfo auth: " + url);
			response.sendRedirect(url);
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
