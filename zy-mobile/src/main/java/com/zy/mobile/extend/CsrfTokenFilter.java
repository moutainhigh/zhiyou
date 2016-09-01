package com.zy.mobile.extend;

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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.model.Constants;
import com.zy.model.Principal;

public class CsrfTokenFilter implements Filter {

	Logger logger = LoggerFactory.getLogger(CsrfTokenFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// nothing to do
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession();

		Principal principal = (Principal) session.getAttribute(Constants.SESSION_ATTRIBUTE_PRINCIPAL);

		if (principal != null && request.getMethod().equalsIgnoreCase("POST")) {
			String csrfToken = request.getParameter("csrfToken");
			if (StringUtils.isBlank(csrfToken)) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "csrf token is miss");
			} else if (!principal.getCsrfToken().equals(csrfToken)) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "csrf token is wrong");
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// nothing to do
	}

}
