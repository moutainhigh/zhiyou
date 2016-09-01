package com.zy.admin.extend;

import javax.servlet.http.HttpServletRequest;

public class CommonsMultipartResolver extends org.springframework.web.multipart.commons.CommonsMultipartResolver {

	@Override
	public boolean isMultipart(HttpServletRequest request) {
		if("/editor".equals(request.getRequestURI()) && "uploadsnapscreen".equals(request.getParameter("action"))) {
			return false;
		}
		return super.isMultipart(request);
	}
	
}
