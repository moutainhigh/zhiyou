package com.zy.mobile.controller.ucenter;

import com.zy.common.exception.ValidationException;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.support.AliyunOssSupport;
import com.zy.common.support.cache.CacheSupport;
import com.zy.common.support.sms.SmsSupport;
import com.zy.common.util.ValidateUtils;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.sys.ShortMessage;
import com.zy.entity.usr.Appearance;
import com.zy.entity.usr.Portrait;
import com.zy.entity.usr.User;
import com.zy.model.Constants;
import com.zy.model.PhoneAndSmsCode;
import com.zy.model.Principal;
import com.zy.service.*;
import com.zy.util.GcUtils;
import org.apache.commons.io.FilenameUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.model.Constants.*;
import static java.util.Objects.isNull;


@RequestMapping("/u")
@Controller
public class UcenterInfoController {
	
	Logger logger = LoggerFactory.getLogger(UcenterInfoController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PortraitService portraitService;

    @Autowired
    private AliyunOssSupport aliyunOssSupport;
    
    @Autowired
	private SmsSupport smsSupport;

	@Autowired
	private ShortMessageService shortMessageService;

	@Autowired
	private CacheSupport cacheSupport;

	@Autowired
	private AddressService addressService;

	@Autowired
	private AppearanceService appearanceService;
	
    @RequestMapping(value = "/userInfo", method = RequestMethod.GET)
    public String userInfo(Principal principal, Model model) {
        Long userId = principal.getUserId();
        User user = userService.findOne(userId);
        Portrait portrait = portraitService.findByUserId(userId);
        boolean isCompletePortrait = true;
        if (isNull(portrait))
            isCompletePortrait = false;
        model.addAttribute("hasAddress", !addressService.findByUserId(userId).isEmpty());
        model.addAttribute("isCompletedPortrait", isCompletePortrait);
        model.addAttribute("user", user);
        model.addAttribute("userAvatarSmall", GcUtils.getThumbnail(user.getAvatar(), 240, 240));
        
        Appearance appearance = appearanceService.findByUserId(userId);
        if(!isNull(appearance)) {
        	model.addAttribute("isCompletedAppearance", appearance.getConfirmStatus() == ConfirmStatus.审核通过);
        } 
        
        return "ucenter/user/userInfo";
    }


    @RequestMapping(value = "/avatar", method = RequestMethod.GET)
    private String avatar(Principal principal, Model model) {
        User user = userService.findOne(principal.getUserId());
        model.addAttribute("userAvatarSmall", GcUtils.getThumbnail(user.getAvatar(), 240, 240));
        return "ucenter/user/userAvatar";
    }

    @RequestMapping(value = "/avatar", method = RequestMethod.POST)
    @ResponseBody
    private Result<String> avatar(MultipartFile file, RedirectAttributes redirectAttributes, Principal principal, HttpServletResponse response, HttpSession session) throws IOException {
        try {
            if (file == null || file.isEmpty()) {
                throw new ValidationException("图片不能为空");
            }
            long fileSize = file.getSize();
            String originalFilename = file.getOriginalFilename();
            String ext = StringUtils.lowerCase(FilenameUtils.getExtension(originalFilename));
            String[] allowedExts = new String[]{"jpg", "png", "jpeg", "gif", "webp"};
            List<String> exts = new ArrayList<String>();
            for (String str : allowedExts) {
                exts.add(str);
            }
            if (!exts.contains(ext)) {
                throw new ValidationException("图片后缀错误，必须为" + StringUtils.join(allowedExts, ","));
            }
            if (fileSize > 4 * 1024 * 1024) {
                throw new ValidationException("图片大小不能超过20MB");
            }
            String imageUrl = aliyunOssSupport.putPublicObject(Constants.ALIYUN_BUCKET_NAME_IMAGE, "avatar/", file, Constants.ALIYUN_URL_IMAGE);
            userService.modifyAvatar(principal.getUserId(), imageUrl);
            return ResultBuilder.result(GcUtils.getThumbnail(imageUrl, 240, 240));
        } catch (Exception e) {
            return ResultBuilder.error("头像上传失败，原因" + e.getMessage());
        }
    }

    @RequestMapping(value = "/nickname", method = RequestMethod.GET)
    private String nickname(Principal principal, Model model) {
        User user = userService.findOne(principal.getUserId());
        model.addAttribute("user", user);
        return "ucenter/user/userNickname";
    }

    @RequestMapping(value = "/nickname", method = RequestMethod.POST)
    private String nickname(@RequestParam String nickname) {
        Principal principal = GcUtils.getPrincipal();
        try {
            userService.modifyNickname(principal.getUserId(), nickname);
        } catch (Exception e) {
            return "ucenter/user/userNickname";
        }
        return "redirect:/u/userInfo";
    }
    
	@RequestMapping(value = "/bindPhone", method = RequestMethod.GET)
	public String bindPhone() {
		return "ucenter/user/userPhone";
	}

	@RequestMapping(value = "/sendBindPhoneSmsCode", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> sendSmsCode(HttpSession session, String phone, String captcha) {
		validate(phone, ValidateUtils.PHONE, "错误的手机格式");
		if (userService.findByPhone(phone) != null) {
			return ResultBuilder.error("该手机已经被绑定");
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

	@RequestMapping(value = "/bindPhone", method = RequestMethod.POST)
	public String bindPhone(Principal principal, Model model, RedirectAttributes redirectAttributes, HttpSession session, @RequestParam String smsCode,
			@RequestParam String phone) {

		if (userService.findByPhone(phone) != null) {
			model.addAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("该手机已被绑定"));
			return bindPhone();
		}
		PhoneAndSmsCode phoneAndSmsCode = (PhoneAndSmsCode) session.getAttribute(SESSION_ATTRIBUTE_BIND_PHONE_SMS);
		if (phoneAndSmsCode == null || !smsCode.equalsIgnoreCase(phoneAndSmsCode.getSmsCode()) || !phone.equalsIgnoreCase(phoneAndSmsCode.getPhone())) {
			model.addAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("短信校验码错误"));
			return bindPhone();
		}
		userService.bindPhone(principal.getUserId(), phone);
		session.removeAttribute(SESSION_ATTRIBUTE_BIND_PHONE_SMS);
		redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("绑定手机成功"));
		return "redirect:/u/userInfo";
	}
}
