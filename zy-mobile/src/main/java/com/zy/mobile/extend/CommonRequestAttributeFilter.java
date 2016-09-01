package com.zy.mobile.extend;

import static com.gc.model.Constants.ALIYUN_URL_STATIC;
import static com.gc.model.Constants.SETTING_SYS_NAME;
import static com.gc.model.Constants.URL_MOBILE;
import static com.gc.model.Constants.URL_PASSPORT;
import static com.gc.model.Constants.URL_PC;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class CommonRequestAttributeFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		request.setAttribute("stccdn", ALIYUN_URL_STATIC);
		request.setAttribute("urlpassport", URL_PASSPORT);
		request.setAttribute("urlpc", URL_PC);
		request.setAttribute("urlmobile", URL_MOBILE);
		request.setAttribute("sys", SETTING_SYS_NAME);
		filterChain.doFilter(request, response);
	}

}
