package com.zy.admin.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.zy.common.exception.BizException;
import com.zy.common.exception.ValidationException;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.util.JsonUtils;
import com.zy.common.util.WebUtils;
import com.zy.model.BizCode;
import com.zy.model.Constants;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

	private Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);
	
	@ModelAttribute
	void pre(HttpServletRequest request) {
		logger.debug("REQUEST URI = " + request.getRequestURI());
		request.setAttribute("stccdn", Constants.ALIYUN_URL_STATIC);
	}
	
	/*@ExceptionHandler(UnauthenticatedException.class)
	public String handleUnauthenticatedException(UnauthenticatedException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if(WebUtils.isAjax(request)) {
			OutputStream os = response.getOutputStream();
			response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			os.write(JsonUtils.toJson(ResponseBuilder.error("请先登录")).getBytes(Charset.forName("UTF-8")));
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return null;
		} else {
			return "redirect:/login";
		}
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public String handleUnauthorizedException(UnauthorizedException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if(WebUtils.isAjax(request)) {
			OutputStream os = response.getOutputStream();
			response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			os.write(JsonUtils.toJson(ResponseBuilder.error("您没有权限")).getBytes(Charset.forName("UTF-8")));
			return null;
		} else {
			return "redirect:/403";
		}
	}*/
	
	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String handleValidationException(ValidationException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.error("参数异常IP ADDR = " + request.getRemoteHost() +  "REQUEST URI =" + request.getRequestURI() + "REQUEST REFERER = " + request.getHeader(HttpHeaders.REFERER)  , e);
		if(WebUtils.isAjax(request)) {
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
		if(WebUtils.isAjax(request)) {
			OutputStream os = response.getOutputStream();
			response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			os.write(JsonUtils.toJson(new ResultBuilder<>().code(e.getCode()).message(e.getMessage()).data(e.getData()).build()).getBytes(Charset.forName("UTF-8")));
			return null;
		} else {
			return "error/500";
		}
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.error("其他异常IP ADDR = " + request.getRemoteHost() +  "REQUEST URI =" + request.getRequestURI() + "REQUEST REFERER = " + request.getHeader(HttpHeaders.REFERER)  , e);
		if(WebUtils.isAjax(request)) {
			OutputStream os = response.getOutputStream();
			response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			os.write(JsonUtils.toJson(new ResultBuilder<>().code(BizCode.ERROR).message(e.getMessage()).build()).getBytes(Charset.forName("UTF-8")));
			return null;
		} else {
			return "error/500";
		}
	}
	
}
