package com.zy.mobile.controller;

import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.support.cache.CacheSupport;
import com.zy.common.support.sms.SmsSupport;
import com.zy.entity.sys.ShortMessage;
import com.zy.entity.usr.User;
import com.zy.model.Constants;
import com.zy.service.ShortMessageService;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;

import static com.zy.common.util.ValidateUtils.PHONE;
import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.model.Constants.*;

@RequestMapping
@Controller
public class FindPasswordController {

	final Logger logger = LoggerFactory.getLogger(FindPasswordController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private SmsSupport smsSupport;

	@Autowired
	private CacheSupport cacheSupport;

	@Autowired
	private ShortMessageService shortMessageService;

	@RequestMapping(value = "/findPassword", method = RequestMethod.GET)
	public String findPassword() {
		return "findPassword1";
	}

	@RequestMapping(value = "/findPassword", method = RequestMethod.POST)
	public String findPassword(HttpSession session, RedirectAttributes redirectAttributes, @RequestParam String captcha, @RequestParam String phone,
			@RequestParam String smsCode) {

		String cachedCaptcha = (String) session.getAttribute(SESSION_ATTRIBUTE_CAPTCHA);
		session.removeAttribute(SESSION_ATTRIBUTE_CAPTCHA);
		if (captcha == null || !captcha.equalsIgnoreCase(cachedCaptcha)) {
			redirectAttributes.addFlashAttribute("phone", phone);
			redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("图形验证码错误"));
			return "redirect:/findPassword";
		}

		String cachedSmsCode = (String) session.getAttribute(SESSION_ATTRIBUTE_FIND_PASSWORD_SMS);
		if (smsCode == null || !smsCode.equalsIgnoreCase(cachedSmsCode)) {
			redirectAttributes.addFlashAttribute("phone", phone);
			redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("手机验证码错误"));
			return "redirect:/findPassword";
		}

		User user = userService.findByPhone(phone);
		if (user == null) {
			redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("该手机号不存在"));
			return "redirect:/findPassword";
		}
		session.setAttribute(SESSION_ATTRIBUTE_FIND_PASSWORD_USER_ID, user.getId());
		return "redirect:/findPassword/modify";
	}

	@RequestMapping(value = "/findPassword/modify", method = RequestMethod.GET)
	public String modify(HttpSession session, RedirectAttributes redirectAttributes) {
		Long userId = (Long) session.getAttribute(SESSION_ATTRIBUTE_FIND_PASSWORD_USER_ID);
		if (userId == null) {
			redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("找回密码超时, 请重试"));
			return "redirect:/findPassword";
		}

		return "findPassword2";
	}

	@RequestMapping(value = "/findPassword/modify", method = RequestMethod.POST)
	public String modify(@RequestParam String password, HttpSession session, RedirectAttributes redirectAttributes, Device device) {

		validate(password, v -> v.length() >= 6, "密码长度要大于6位");

		Long userId = (Long) session.getAttribute(SESSION_ATTRIBUTE_FIND_PASSWORD_USER_ID);
		if (userId == null) {
			redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("找回密码超时, 请重试"));
			return "redirect:/findPassword";
		}
		userService.modifyPassword(userId, password);
		session.removeAttribute(SESSION_ATTRIBUTE_FIND_PASSWORD_USER_ID);
		if (device.isMobile()) {
			redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("找回密码成功, 请用新密码登陆"));
			return "redirect:/login";
		} else {
			return "findPassword3";
		}
	}

	@RequestMapping(value = "/findPassword/sendSmsCode", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> sendSmsCode(@RequestParam String captcha, HttpSession session, @RequestParam String phone) {
		String cachedCaptcha = (String) session.getAttribute(SESSION_ATTRIBUTE_CAPTCHA);
		if (captcha == null || !captcha.equalsIgnoreCase(cachedCaptcha)) {
			session.removeAttribute(SESSION_ATTRIBUTE_CAPTCHA);
			return ResultBuilder.error("验证码错误");
		}

		validate(phone, PHONE, "错误的手机格式");
		Date date = (Date) cacheSupport.get(CACHE_NAME_FIND_PASSWORD_LAST_SEND_TIME, phone);
		if (date != null && DateUtils.addMinutes(date, 2).after(new Date())) {
			session.removeAttribute(SESSION_ATTRIBUTE_CAPTCHA);
			return ResultBuilder.error("两次发送间隔为120秒");
		}
		if (date == null) {
			/* check phone at first time */
			User user = userService.findByPhone(phone);
			if (user == null) {
				session.removeAttribute(SESSION_ATTRIBUTE_CAPTCHA);
				return ResultBuilder.error("该手机号不存在");
			}
		}
		String smsCode = RandomStringUtils.randomNumeric(6);
		String message = "您正在找回密码, 短信验证码为" + smsCode;
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
		session.setAttribute(SESSION_ATTRIBUTE_FIND_PASSWORD_SMS, smsCode);
		cacheSupport.set(CACHE_NAME_FIND_PASSWORD_LAST_SEND_TIME, phone, new Date(), 600);
		return ResultBuilder.ok("短信发送成功");
	}

}
