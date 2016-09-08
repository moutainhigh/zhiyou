package com.zy.mobile.controller.ucenter;

import com.zy.common.exception.ValidationException;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.support.AliyunOssSupport;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.Appearance;
import com.zy.entity.usr.Portrait;
import com.zy.entity.usr.User;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.query.BankCardQueryModel;
import com.zy.service.AddressService;
import com.zy.service.AppearanceService;
import com.zy.service.BankCardService;
import com.zy.service.PortraitService;
import com.zy.service.UserService;
import com.zy.util.GcUtils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;

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
	private AddressService addressService;

	@Autowired
	private BankCardService bankCardService;
	
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
        	model.addAttribute("isCompletedAppearance", appearance.getConfirmStatus() == ConfirmStatus.已通过);
        }
        model.addAttribute("hasBankCard", bankCardService.count(BankCardQueryModel.builder().userIdEQ(userId).build()) > 0);
        
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
    
}
