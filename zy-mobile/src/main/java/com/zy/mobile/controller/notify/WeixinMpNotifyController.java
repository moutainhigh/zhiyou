package com.zy.mobile.controller.notify;

import com.zy.common.support.cache.CacheSupport;
import com.zy.common.util.CookieUtils;
import com.zy.common.util.DateUtil;
import com.zy.entity.usr.User;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.PrincipalBuilder;
import com.zy.model.dto.AgentRegisterDto;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import static com.zy.common.util.ValidateUtils.NOT_BLANK;
import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.model.Constants.*;


@RequestMapping
@Controller
public class WeixinMpNotifyController {

	final Logger logger = LoggerFactory.getLogger(WeixinMpNotifyController.class);

	@Value("${weixinMp.appKey}")
	private String appKey;

	@Value("${weixinMp.appSecret}")
	private String appSecret;

	@Autowired
	private WxMpService wxMpService;

	@Autowired
	private UserService userService;

	@Autowired
	private CacheSupport cacheSupport;

	@RequestMapping("/notify/weixinMp")
	public String notifyWeixin(String code, String state, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException, WxErrorException {

		Principal principal = (Principal) session.getAttribute(SESSION_ATTRIBUTE_PRINCIPAL);
		if (principal != null || !Constants.WEIXIN_STATE_USERINFO.equals(state)) {
			// 重定向
			String redirectUrl = (String) session.getAttribute(SESSION_ATTRIBUTE_REDIRECT_URL);
			Long principalId = principal.getUserId();
			String ip = request.getRemoteAddr();
			String method = redirectUrl;
			String dateStr = DateUtil.formatDate(new Date());
			String msg = principalId+"-----------"+ip+"-----------"+method+"-----------"+dateStr+"-----------"+code;
			WriteStringToFile2(msg);
			if (StringUtils.isBlank(redirectUrl)) {
				return "redirect:/u";
			} else {
				return "redirect:" + redirectUrl;
			}
		} else {
			logger.info("code: " + code);
			validate(code, NOT_BLANK, "code is blank");

			WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
			String openId = wxMpOAuth2AccessToken.getOpenId();
			validate(openId, NOT_BLANK, "open id is blank");

			User user = userService.findByOpenId(openId);
			if (user == null) {
				WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
				AgentRegisterDto agentRegisterDto = new AgentRegisterDto();

				String avatar = wxMpUser.getHeadImgUrl();
				if (StringUtils.isBlank(avatar)) {
					avatar = Constants.SETTING_DEFAULT_AVATAR;
				}else if(avatar.indexOf("http")==-1){
					avatar = Constants.SETTING_DEFAULT_AVATAR;

				}
				agentRegisterDto.setAvatar(avatar);
				agentRegisterDto.setOpenId(wxMpUser.getOpenId());
				agentRegisterDto.setUnionId(wxMpUser.getUnionId());
				agentRegisterDto.setNickname(wxMpUser.getNickname());

				session.setAttribute(SESSION_ATTRIBUTE_AGENT_REGISTER_DTO, agentRegisterDto);
				return "redirect:/register";

			} else {

				Long userId = user.getId();
				String tgt = GcUtils.generateTgt();
				int expire = 60 * 60 * 24 * 7;
				CookieUtils.add(response, Constants.COOKIE_NAME_MOBILE_TOKEN, tgt, expire, Constants.DOMAIN_MOBILE);
				cacheSupport.set(Constants.CACHE_NAME_TGT, tgt, userId, expire);
				session.setAttribute(SESSION_ATTRIBUTE_PRINCIPAL, PrincipalBuilder.build(userId, tgt));
				logger.info("login success, tgt:" + tgt);
				//userService.modifyLastLoginTime(userId, principal.getUserId()); //已经在非空判断的else里面了 肯定会空指针异常
				userService.modifyLastLoginTime(userId, userId);

				String redirectUrl = (String) session.getAttribute(SESSION_ATTRIBUTE_REDIRECT_URL);
				if (StringUtils.isBlank(redirectUrl)) {
					redirectUrl = "/";
				}

				Long principalId = userId;
				String ip = request.getRemoteAddr();
				String method = redirectUrl;
				String dateStr = DateUtil.formatDate(new Date());
				String msg = principalId+"-----------"+ip+"-----------"+method+"-----------"+dateStr+"-----------"+code;
				WriteStringToFile2(msg);

				return "redirect:" + redirectUrl;

			}


		}

	}


	public static void WriteStringToFile2(String msg) {
		try {
			String filePath = "/data/logs/txt/WeixinLogin.txt";
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
