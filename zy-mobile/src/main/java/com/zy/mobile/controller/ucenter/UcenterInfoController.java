package com.zy.mobile.controller.ucenter;

import com.zy.common.model.result.ResultBuilder;
import com.zy.component.UserInfoComponent;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.Tag;
import com.zy.entity.usr.UserInfo;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.query.UserInfoQueryModel;
import com.zy.service.JobService;
import com.zy.service.TagService;
import com.zy.service.UserInfoService;
import com.zy.vo.UserInfoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequestMapping("/u/userInfo")
@Controller
public class UcenterInfoController {
	
	Logger logger = LoggerFactory.getLogger(UcenterInfoController.class);

	@Autowired
	private UserInfoService userInfoService;    
	
	@Autowired
	private UserInfoComponent userInfoComponent;

	@Autowired
	private TagService tagService;

	@Autowired
	private JobService jobService;

	@RequestMapping()
	public String detail(Principal principal, Model model) {
		UserInfo userInfo = userInfoService.findByUserId(principal.getUserId());
		if(userInfo == null) {
			return "redirect:/u/userInfo/create";
		}
		model.addAttribute("userInfo", userInfoComponent.buildVo(userInfo));
		return "ucenter/user/userInfo";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Principal principal, Model model) {
		UserInfo userInfo = userInfoService.findByUserId(principal.getUserId());
		if(userInfo != null) {
			return "redirect:/u/userInfo";
		}else{
			UserInfoQueryModel userInfoQueryModel = new UserInfoQueryModel();
			userInfoQueryModel.setUserIdEQ(principal.getUserId());
			userInfoQueryModel.setRealFlag(0);
			List<UserInfo> userInfos = userInfoService.findAll(userInfoQueryModel);
			if(userInfos.size() == 1){
				model.addAttribute("userInfo",userInfoComponent.buildVo(userInfos.get(0)));
			}
		}
		model.addAttribute("jobs", jobService.findAll());
		model.addAttribute("tags", getTags());
		return "ucenter/user/userInfoCreate";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(UserInfo userInfo, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		userInfo.setUserId(principal.getUserId());
		try {
			UserInfo info = userInfoService.findByIdCardNumber(userInfo.getIdCardNumber());
			if(null != info){
				model.addAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("身份证已经被认证过，请核对信息"));
				model.addAttribute("userInfo", userInfoComponent.buildVo(info));
				model.addAttribute("jobs", jobService.findAll());
				model.addAttribute("tags", getTags());
				return "ucenter/user/userInfoCreate";
			}
			userInfo.setRealFlag(1);
			userInfoService.create(userInfo);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("保存成功"));
		} catch (Exception e) {
			model.addAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			model.addAttribute("userInfo", userInfo);
			return "ucenter/user/userInfoCreate";
		}
		return "redirect:/u/userInfo";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Principal principal, Model model) {
		UserInfo userInfo = userInfoService.findByUserId(principal.getUserId());
		if(userInfo == null) {
			return "ucenter/user/userInfoCreate";
		}
		model.addAttribute("jobs", jobService.findAll());
		model.addAttribute("tags", getTags());
		model.addAttribute("userInfo", userInfoComponent.buildVo(userInfo));
		return "ucenter/user/userInfoEdit";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(UserInfo userInfo, Principal principal, RedirectAttributes redirectAttributes) {
		UserInfo persistence = userInfoService.findByUserId(principal.getUserId());
		if(persistence == null) {
			return "ucenter/user/userInfoCreate";
		}
		if(persistence.getConfirmStatus() == ConfirmStatus.已通过) {
			return "redirect:/u/userInfo";
		}
		userInfo.setId(persistence.getId());
		try {
			userInfoService.modify(userInfo);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("修改成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return "redirect:/u/userInfo/edit";
		}
		return "redirect:/u/userInfo";
	}
	
	private Map<String, List<Tag>> getTags() {
		Map<String, List<Tag>> tags = new HashMap<>();
		this.tagService.findAll().parallelStream().forEach(tag -> {
			if (!tags.containsKey(tag.getTagType())) {
				tags.put(tag.getTagType(), new ArrayList<>());
			}
			tags.get(tag.getTagType()).add(tag);
		});
		return tags;
	}
}
