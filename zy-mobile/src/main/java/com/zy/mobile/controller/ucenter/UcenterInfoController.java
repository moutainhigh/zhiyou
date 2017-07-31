package com.zy.mobile.controller.ucenter;

import com.zy.common.model.result.ResultBuilder;
import com.zy.common.util.DateUtil;
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
import java.util.stream.Collectors;


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
		List<UserInfo> infos = userInfoService.findAll(UserInfoQueryModel.builder().confirmStatusEQ(ConfirmStatus.已通过).realFlag(1).idCardNumberLK(userInfo.getIdCardNumber()).build());
		UserInfo persistence = userInfoService.findByUserIdandFlage(principal.getUserId());
		try {
			int age = DateUtil.getAge(userInfo.getIdCardNumber());
			if(age < 18){
				model.addAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("未满18周岁，不能成为服务商！"));
				model.addAttribute("userInfo", userInfoComponent.buildVo(userInfo));
				model.addAttribute("jobs", jobService.findAll());
				model.addAttribute("tags", getTags());
				return "ucenter/user/userInfoCreate";
			}
			userInfo.setAge(age);
			if(infos == null || infos.isEmpty()){
				userInfo.setRealFlag(1);
				if(persistence != null){
					userInfo.setId(persistence.getId());
					userInfoService.modify(userInfo);
				}else {
					userInfo.setUserId(principal.getUserId());
					userInfoService.create(userInfo);
				}
			}else {
				model.addAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("身份证已经被认证过，请核对身份证信息"));
				model.addAttribute("userInfo", userInfoComponent.buildVo(userInfo));
				model.addAttribute("jobs", jobService.findAll());
				model.addAttribute("tags", getTags());
				return "ucenter/user/userInfoCreate";
			}
		} catch (Exception e) {
			model.addAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			model.addAttribute("userInfo", userInfo);
			return "ucenter/user/userInfoCreate";
		}
		redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("保存成功"));
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
		UserInfo persistence = userInfoService.findByUserIdandFlage(principal.getUserId());
		List<UserInfo> infos = userInfoService.findAll(UserInfoQueryModel.builder().confirmStatusEQ(ConfirmStatus.已通过).realFlag(1).idCardNumberLK(userInfo.getIdCardNumber()).build());
		if(persistence == null) {
			return "ucenter/user/userInfoCreate";
		}
		if(persistence.getConfirmStatus() == ConfirmStatus.已通过) {
			return "redirect:/u/userInfo";
		}
		int age = DateUtil.getAge(userInfo.getIdCardNumber());
		if(age < 18){
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("未满18周岁，不能成为服务商！"));
			return "redirect:/u/userInfo/edit";
		}
		userInfo.setAge(age);
		userInfo.setId(persistence.getId());
		userInfo.setRealFlag(1);
		if(infos == null || infos.isEmpty()){
			try {
			userInfoService.modify(userInfo);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("修改成功"));
			return "redirect:/u/userInfo";
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
				return "redirect:/u/userInfo/edit";
			}
		}else{
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("身份证已经被认证过，请核对身份证信息"));
			return "redirect:/u/userInfo/edit";
		}
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
