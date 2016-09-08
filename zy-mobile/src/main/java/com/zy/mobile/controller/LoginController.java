package com.zy.mobile.controller;

import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.support.cache.CacheSupport;
import com.zy.common.support.sms.SmsSupport;
import com.zy.common.util.ValidateUtils;
import com.zy.entity.sys.ShortMessage;
import com.zy.entity.usr.User;
import com.zy.model.Constants;
import com.zy.model.PhoneAndSmsCode;
import com.zy.model.dto.AgentRegisterDto;
import com.zy.service.BannerService;
import com.zy.service.ShortMessageService;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.model.Constants.*;

@RequestMapping
@Controller
public class LoginController {

	final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private SmsSupport smsSupport;

	@Autowired
	private ShortMessageService shortMessageService;

	@Autowired
	private BannerService bannerService;

	@Autowired
	private UserService userService;

	@Autowired
	private CacheSupport cacheSupport;
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(Model model, HttpSession session) {

		AgentRegisterDto agentRegisterDto = (AgentRegisterDto) session.getAttribute(SESSION_ATTRIBUTE_AGENT_REGISTER_DTO);
		if (agentRegisterDto == null) {
			model.addAttribute("isNew", true);
		}

		model.addAttribute("avatar", agentRegisterDto.getAvatar());
		model.addAttribute("nickname", agentRegisterDto.getNickname());
		
		return "register";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(Model model, HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes, @RequestParam String smsCode,
	                       @RequestParam String phone) {

		AgentRegisterDto agentRegisterDto = (AgentRegisterDto) session.getAttribute(SESSION_ATTRIBUTE_AGENT_REGISTER_DTO);
		if (agentRegisterDto == null) {
			model.addAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("注册已超时, 请刷新重试"));
			return register(model, session);
		}

		User user = userService.findByPhone(phone);
		if (user != null && StringUtils.isNotBlank(user.getOpenId())) {
			model.addAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("该手机已经被其他微信绑定, 请先解绑"));
			return register(model, session);
		}

		PhoneAndSmsCode phoneAndSmsCode = (PhoneAndSmsCode) session.getAttribute(SESSION_ATTRIBUTE_BIND_PHONE_SMS);
		if (phoneAndSmsCode == null || !smsCode.equalsIgnoreCase(phoneAndSmsCode.getSmsCode()) || !phone.equalsIgnoreCase(phoneAndSmsCode.getPhone())) {
			model.addAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("短信校验码错误"));
			return register(model, session);
		}

		Long inviterId = (Long) request.getAttribute(REQUEST_ATTRIBUTE_INVITER_ID);
		if (inviterId != null) {
			User inviter = userService.findOne(inviterId);
			if (inviter != null) {
				agentRegisterDto.setInviterId(inviterId);
			}
		}

		agentRegisterDto.setPhone(phone);
		agentRegisterDto.setRegisterIp(GcUtils.getHost());

		userService.registerAgent(agentRegisterDto);

		session.removeAttribute(SESSION_ATTRIBUTE_BIND_PHONE_SMS);

		String redirectUrl = (String) session.getAttribute(SESSION_ATTRIBUTE_REDIRECT_URL);
		if (StringUtils.isBlank(redirectUrl)) {
			redirectUrl = "/";
		}
		redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, "恭喜您, 注册成功");
		return "redirect:" + redirectUrl;
	}

	@RequestMapping(value = "/register/sendSmsCode", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> sendSmsCode(HttpSession session, String phone, String captcha) {
		validate(phone, ValidateUtils.PHONE, "错误的手机格式");
		User user = userService.findByPhone(phone);
		if (user != null && StringUtils.isNotBlank(user.getOpenId())) {
			return ResultBuilder.error("该手机已经被其他微信绑定, 请先解绑");
		}

		Date date = (Date) cacheSupport.get(CACHE_NAME_BIND_PHONE_SMS_LAST_SEND_TIME, phone);
		if (date != null && DateUtils.addMinutes(date, 2).after(new Date())) {
			return ResultBuilder.error("两次发送间隔为120秒");
		}

		String cachedCaptcha = (String) session.getAttribute(SESSION_ATTRIBUTE_CAPTCHA);
		if (captcha == null || !captcha.equalsIgnoreCase(cachedCaptcha)) {
			try {
				session.removeAttribute(SESSION_ATTRIBUTE_CAPTCHA);
			} catch (Exception e) {
			}
			return ResultBuilder.error("图形验证码错误");
		}

		String smsCode = RandomStringUtils.randomNumeric(6);
		String message = "您的短信验证码为" + smsCode;
		SmsSupport.SmsResult smsResult = smsSupport.send(phone, message, Constants.SETTING_SYS_NAME);
		if (!smsResult.isSuccess()) {
			return ResultBuilder.error("短信发送失败,错误代码" + smsResult.getMessage());
		}
		try {
			ShortMessage shortMessage = new ShortMessage();
			shortMessage.setIp(GcUtils.getHost());
			shortMessage.setPhone(phone);
			shortMessage.setContent(message);
			shortMessageService.create(shortMessage);
		} catch (Exception e) {
			logger.error("保存失败", e);
		}
		session.setAttribute(SESSION_ATTRIBUTE_BIND_PHONE_SMS, new PhoneAndSmsCode(phone, smsCode));
		cacheSupport.set(CACHE_NAME_BIND_PHONE_SMS_LAST_SEND_TIME, phone, new Date(), 600);
		return ResultBuilder.ok("短信发送成功");
	}
	

	
}
