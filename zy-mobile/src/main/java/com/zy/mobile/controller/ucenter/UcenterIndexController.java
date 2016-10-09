package com.zy.mobile.controller.ucenter;

import static java.util.Objects.isNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.exception.ValidationException;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.support.AliyunOssSupport;
import com.zy.component.UserComponent;
import com.zy.entity.fnc.Account;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.query.MessageQueryModel;
import com.zy.model.query.OrderQueryModel;
import com.zy.service.AccountService;
import com.zy.service.AddressService;
import com.zy.service.MessageService;
import com.zy.service.OrderService;
import com.zy.service.UserInfoService;
import com.zy.service.UserService;
import com.zy.util.GcUtils;

@RequestMapping("/u")
@Controller
public class UcenterIndexController {
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private AddressService addressService;
	
	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private UserComponent userComponent;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
    private AliyunOssSupport aliyunOssSupport;
	
	@RequestMapping
	public String index(Principal principal, Model model) {
		Long userId = principal.getUserId();
		
		User user = userService.findOne(userId);
		model.addAttribute("user", user);
		
		List<Account> accounts = accountService.findByUserId(userId);
		for (Account account : accounts) {
			switch (account.getCurrencyType()) {
			case 现金:
				model.addAttribute("money", account.getAmount());
				break;
			case 金币:
				model.addAttribute("coin", account.getAmount());			
				break;
			case 积分:
				model.addAttribute("point", account.getAmount().setScale(0));
				break;
			default:
				break;
			}
		}
		
		model.addAttribute("userAvatarSmall", GcUtils.getThumbnail(user.getAvatar(), 240, 240));
		
		MessageQueryModel messageQueryModel = new MessageQueryModel();
		messageQueryModel.setUserIdEQ(userId);
		messageQueryModel.setIsReadEQ(false);
		model.addAttribute("unreadMessageCount", messageService.count(messageQueryModel));
		
		model.addAttribute("orderInConut", orderService.count(OrderQueryModel.builder().userIdEQ(userId).build()));
		model.addAttribute("orderOutConut", orderService.count(OrderQueryModel.builder().sellerIdEQ(userId).build()));
		return "ucenter/index";
	}
	

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String info(Principal principal, Model model) {
        Long userId = principal.getUserId();
        User user = userService.findOne(userId);
        UserInfo userInfo = userInfoService.findByUserId(userId);
        boolean isUserInfoCompleted = true;
        if (isNull(userInfo)) {
        	isUserInfoCompleted = false;
        } else {
        	isUserInfoCompleted = userInfo.getConfirmStatus() == ConfirmStatus.已通过;
        }
        model.addAttribute("hasAddress", !addressService.findByUserId(userId).isEmpty());
        model.addAttribute("isUserInfoCompleted", isUserInfoCompleted);
        model.addAttribute("user", userComponent.buildListVo(user));
        
        return "ucenter/user/info";
    }
    
    @RequestMapping(value = "/avatar", method = RequestMethod.GET)
    public String avatar(Principal principal, Model model) {
        User user = userService.findOne(principal.getUserId());
        model.addAttribute("userAvatarSmall", GcUtils.getThumbnail(user.getAvatar(), 240, 240));
        return "ucenter/user/userAvatar";
    }

    @RequestMapping(value = "/avatar", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> avatar(MultipartFile file, RedirectAttributes redirectAttributes, Principal principal, HttpServletResponse response, HttpSession session) throws IOException {
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
    public String nickname(Principal principal, Model model) {
        User user = userService.findOne(principal.getUserId());
        model.addAttribute("user", user);
        return "ucenter/user/userNickname";
    }

    @RequestMapping(value = "/nickname", method = RequestMethod.POST)
    public String nickname(@RequestParam String nickname, Model model, RedirectAttributes redirectAttributes) {
        Principal principal = GcUtils.getPrincipal();
        try {
            userService.modifyNickname(principal.getUserId(), nickname);
        } catch (Exception e) {
        	model.addAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("昵称修改失败，原因" + e.getMessage()));
            return "ucenter/user/userNickname";
        }
        redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("修改成功"));
        return "redirect:/u/info";
    }
	
}
