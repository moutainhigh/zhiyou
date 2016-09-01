package com.zy.admin.extend;

import com.zy.admin.model.AdminPrincipal;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.zy.common.exception.UnauthenticatedException;


public class AdminPrincipalMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return AdminPrincipal.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
	        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Subject subject = SecurityUtils.getSubject();
		if(subject == null) {
			throw new UnauthenticatedException();
		}
		AdminPrincipal principal = (AdminPrincipal) subject.getPrincipal();
		if(principal == null) {
			throw new UnauthenticatedException();
		}
		return principal;
	}

}
