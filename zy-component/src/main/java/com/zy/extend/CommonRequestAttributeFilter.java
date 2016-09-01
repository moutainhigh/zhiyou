package com.zy.extend;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.zy.model.Constants.*;

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
