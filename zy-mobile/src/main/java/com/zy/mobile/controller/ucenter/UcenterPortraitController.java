package com.zy.mobile.controller.ucenter;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.result.ResultBuilder;
import com.zy.component.PortraitComponent;
import com.zy.entity.usr.Portrait;
import com.zy.entity.usr.Tag;
import com.zy.model.Principal;
import com.zy.service.JobService;
import com.zy.service.PortraitService;
import com.zy.service.TagService;

@RequestMapping("/u/portrait")
@Controller
public class UcenterPortraitController {

	@Autowired
	private PortraitService portraitService;

	@Autowired
	private PortraitComponent portraitComponent;

	@Autowired
	private TagService tagService;

	@Autowired
	private JobService jobService;

	@RequestMapping()
	public String index(Principal principal, Model model) {
		Portrait portrait = this.portraitService.findByUserId(principal.getUserId());
		if (portrait == null)
			return "redirect:/u/portrait/create";
		model.addAttribute("portrait", portraitComponent.buildVo(portrait));
		return "ucenter/user/portraitDetail";
	}

	@RequestMapping(value = "create", method = GET)
	public String create(Model model) {
		model.addAttribute("jobs", this.jobService.findAll());
		model.addAttribute("tags", getTags());
		return "ucenter/user/portraitCreate";
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

	@RequestMapping(value = "/create", method = POST)
	public String create(Portrait portrait, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		portrait.setUserId(principal.getUserId());
		portrait.setJobId(portrait.getJobId());
		try {
			portraitService.create(portrait);
		} catch (Exception e) {
			model.addAttribute("portrait", portrait);
			model.addAttribute(ResultBuilder.error(e.getMessage()));
			return create(model);
		}
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("创建自我画像成功"));
		return "redirect:/u/portrait";
	}

	@RequestMapping(value = "/edit", method = GET)
	public String edit(Principal principal, Model model) {
		Portrait portrait = portraitService.findByUserId(principal.getUserId());
		if (portrait == null) {
			return "redirect:/u/portrait/create";
		}
		model.addAttribute("jobs", this.jobService.findAll());
		model.addAttribute("tags", getTags());
		model.addAttribute("portrait", portraitComponent.buildVo(portrait));
		return "ucenter/user/portraitEdit";
	}

	@RequestMapping(value = "/edit", method = POST)
	public String edit(Portrait portrait, Principal principal, Model model, RedirectAttributes redirectAttributes) {

		Portrait persistence = portraitService.findByUserId(principal.getUserId());
		if (persistence == null) {
			return "redirect:/u/portrait/create";
		}

		try {
			portrait.setId(persistence.getId());
			portraitService.update(portrait);
		} catch (Exception e) {
			model.addAttribute(ResultBuilder.error(e.getMessage()));
			return edit(principal, model);
		}
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("更新自我画像成功"));
		return "redirect:/u/portrait";
	}

}
