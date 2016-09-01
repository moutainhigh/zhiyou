package com.zy.admin.extend;

import java.util.Set;

import javax.annotation.PostConstruct;

import com.zy.admin.model.AdminPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.Md5CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.zy.common.support.cache.CacheSupport;
import com.zy.entity.adm.Admin;
import com.zy.entity.usr.User;
import com.zy.model.Constants;
import com.zy.service.AdminService;
import com.zy.service.UserService;

/**
 * 系统安全认证实现类
 * 
 * @author 薛定谔的猫
 * @version 2013-06-07
 */
@SuppressWarnings("deprecation")
public class AuthorizingRealm extends org.apache.shiro.realm.AuthorizingRealm  {

	
	@Autowired
	protected CacheSupport cacheSupport;
	
	@Autowired
	@Lazy
	protected AdminService adminService;
	
	@Autowired
	@Lazy
	protected UserService userService;
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordCaptchaToken token = (UsernamePasswordCaptchaToken) authcToken;
		String username = token.getUsername();
		if(username == null){
			throw new UnknownAccountException();
		}
		
		String captcha = token.getCaptcha();
		Integer failureTimes = cacheSupport.get(Constants.CACHE_NAME_LOGIN_FAILURE_TIMES, username);
		if(failureTimes != null  && failureTimes >= Constants.SETTING_LOGIN_FAILURE_VALIDATE_TIMES) {
			Session session = SecurityUtils.getSubject().getSession();
			String cachedCaptcha = (String) session.getAttribute(Constants.SESSION_ATTRIBUTE_CAPTCHA);
			if (captcha == null || !captcha.equalsIgnoreCase(cachedCaptcha)) {
				throw new IncorrectCaptchaException();
			}
		}
		
		User user = userService.findByPhone(username);
		if(user == null) {
			throw new UnknownAccountException();
		}
		Long userId = user.getId();
		Admin admin = adminService.findByUserId(userId);
		if(admin == null) {
			throw new UnknownAccountException();
		}
		Long adminId = admin.getId();
		AdminPrincipal principal = new AdminPrincipal(adminId, userId, username, admin.getSiteId());
		return new SimpleAuthenticationInfo(principal, user.getPassword(), getName());
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		AdminPrincipal principal = (AdminPrincipal) super.getAvailablePrincipal(principals);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Admin admin = adminService.findByUserId(principal.getUserId());
		if(admin != null && admin.getSiteId() != null){
			info.addRole("siteAdmin");
		} else if(admin != null && admin.getSiteId() == null){
			Set<String> permissions = adminService.findPermissions(principal.getId());
			if(permissions != null) {
				for (String permission : permissions) {
					if (StringUtils.isNotBlank(permission)) {
						info.addStringPermission(permission);
					}
				}
			}
		}
		return info;
	}

	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new Md5CredentialsMatcher();
		setCredentialsMatcher(matcher);
	}

}
