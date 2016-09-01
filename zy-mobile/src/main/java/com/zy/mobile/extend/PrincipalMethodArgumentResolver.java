package com.zy.mobile.extend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.zy.common.exception.UnauthenticatedException;
import com.zy.model.Principal;
import com.zy.util.GcUtils;

public class PrincipalMethodArgumentResolver implements HandlerMethodArgumentResolver {

	Logger logger = LoggerFactory.getLogger(PrincipalMethodArgumentResolver.class);
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return Principal.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
			throws Exception {
		Principal principal = GcUtils.getPrincipal();
		if (principal == null) {
			throw new UnauthenticatedException();
		}
		
		return principal;
	}

}
