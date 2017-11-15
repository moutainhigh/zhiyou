package com.zy.mobile.extend;

import static com.zy.model.Constants.COOKIE_NAME_MOBILE_TOKEN;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zy.common.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.zy.common.support.cache.CacheSupport;
import com.zy.common.util.CookieUtils;
import com.zy.entity.usr.User;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.PrincipalBuilder;
import com.zy.service.UserService;

public class MobileTokenFilter implements Filter {

	Logger logger = LoggerFactory.getLogger(MobileTokenFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// nothing to do
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpSession session = request.getSession();

		String tgt = CookieUtils.get(request, Constants.COOKIE_NAME_MOBILE_TOKEN);
		Principal principal = (Principal) session.getAttribute(Constants.SESSION_ATTRIBUTE_PRINCIPAL1);
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		CacheSupport cacheSupport = wac.getBean(CacheSupport.class);
		UserService userService = wac.getBean(UserService.class);

		if (principal != null) {
			// DO NOTHING
			Long  principalId = principal.getUserId();
			String ip = request.getRemoteAddr();
			String method = request.getScheme()+"://"+ request.getServerName()+request.getRequestURI()+"?"+request.getQueryString();
			String dateStr = DateUtil.formatDate(new Date());
			String msg = principalId+"-----------"+ip+"-----------"+method+"-----------"+dateStr+"-----------"+tgt;
			WriteStringToFile2(msg);

		} else {
			Long  principalId = -1L;
			String ip = request.getRemoteAddr();
			String method = request.getScheme()+"://"+ request.getServerName()+request.getRequestURI()+"?"+request.getQueryString();
			String dateStr = DateUtil.formatDate(new Date());
			String msg = principalId+"-----------"+ip+"-----------"+method+"-----------"+dateStr+"-----------"+tgt;
			WriteStringToFile2(msg);
			if (StringUtils.isNotBlank(tgt)) {
				Long userId = cacheSupport.get(Constants.CACHE_NAME_TGT, tgt);
				if (userId != null) {
					User user = userService.findOne(userId);
					if (user == null) {
						CookieUtils.remove(request, response, COOKIE_NAME_MOBILE_TOKEN);
					} else {
						principal = PrincipalBuilder.build(userId, tgt);
						session.setAttribute(Constants.SESSION_ATTRIBUTE_PRINCIPAL1, principal);
						Long  userIdstr = userId;
						String msg1 = "我是在这初始化的"+userIdstr+"-----------"+ip+"-----------"+method+"-----------"+dateStr+"-----------"+tgt;
						WriteStringToFile2(msg);
					}
				} else {
					CookieUtils.remove(request, response, COOKIE_NAME_MOBILE_TOKEN);
				}
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// nothing to do
	}


	public static void WriteStringToFile2(String msg) {
		try {
			String filePath = "/data/logs/txt/123.txt";
			FileWriter fw = new FileWriter(filePath, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append(msg);
			bw.write("\r\n ");// 往已有的文件上添加字符串
         /*   bw.write("def\r\n ");*/
			bw.close();
			fw.close();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}




}
