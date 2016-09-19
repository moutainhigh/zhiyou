package com.zy.mobile.controller.ucenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.result.ResultBuilder;
import com.zy.component.AppearanceComponent;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.Appearance;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.service.AppearanceService;

@RequestMapping("/u/appearance")
@Controller
public class UcenterAppearanceController {

	@Autowired
	private AppearanceService appearanceService;
	
	@Autowired
	private AppearanceComponent appearanceComponent;
	
	@RequestMapping()
	public String detail(Principal principal, Model model) {
		Appearance appearance = appearanceService.findByUserId(principal.getUserId());
		if(appearance == null) {
			return "redirect:/u/appearance/create";
		}
		model.addAttribute("appearance", appearanceComponent.buildVo(appearance));
		return "ucenter/user/appearanceDetail";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Principal principal) {
		Appearance appearance = appearanceService.findByUserId(principal.getUserId());
		if(appearance != null) {
			return "redirect:/u/appearance";
		}
		return "ucenter/user/appearanceCreate";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(Appearance appearance, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		appearance.setUserId(principal.getUserId());
		try {
			appearanceService.create(appearance);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("保存成功"));
		} catch (Exception e) {
			model.addAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			model.addAttribute("appearance", appearance);
			return "ucenter/user/appearanceCreate";
		}
		return "redirect:/u/appearance";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Principal principal, Model model) {
		Appearance appearance = appearanceService.findByUserId(principal.getUserId());
		if(appearance == null) {
			return "ucenter/user/appearanceCreate";
		}
		model.addAttribute("appearance", appearanceComponent.buildVo(appearance));
		return "ucenter/user/appearanceEdit";
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(Appearance appearance, Principal principal, RedirectAttributes redirectAttributes) {
		Appearance persistence = appearanceService.findByUserId(principal.getUserId());
		if(persistence == null) {
			return "ucenter/user/appearanceCreate";
		}
		if(persistence.getConfirmStatus() == ConfirmStatus.已通过) {
			return "redirect:/u/appearance";
		}
		appearance.setId(persistence.getId());
		try {
			appearanceService.update(appearance);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("保存成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return "redirect:/u/appearance/edit";
		}
		return "redirect:/u/appearance";
	}
}
