package com.zy.admin.controller.usr;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.result.ResultBuilder;
import com.zy.entity.usr.Tag;
import com.zy.service.TagService;

@RequestMapping("/tag")
@Controller
public class TagController {

	@Autowired
	private TagService tagService;
	
	@RequiresPermissions("tag:view")
	@RequestMapping
	public String list(Model model) {
		List<Tag> list = tagService.findAll();
		Map<String, List<Tag>> tagMap = new HashMap<String, List<Tag>>();
		for(Tag tag : list) {
			String tagTypeKey = tag.getTagType();
			List<Tag> tags = tagMap.get(tagTypeKey);
			if(tags == null) {
				tags = new ArrayList<Tag>();
			}
			tags.add(tag);
			tagMap.put(tagTypeKey, tags);
		}
		model.addAttribute("tagMap", tagMap);
		return "usr/tagList";
	}
	
	@RequiresPermissions("tag:edit")
	@RequestMapping(value = "/create", method = GET)
	public String create() {
		return "usr/tagCreate";
	}
	
	@RequiresPermissions("tag:edit")
	@RequestMapping(value = "/create", method = POST)
	public String create(Tag tag, RedirectAttributes redirectAttributes) {
		try {
			tagService.create(tag);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error(e.getMessage()));
			return "redirect:/tagCreate";
		}
		return "redirect:/tag";
	}
	
	@RequiresPermissions("tag:edit")
	@RequestMapping(value = "/delete/{id}", method = GET)
	public String delete(@PathVariable Long id) {
		tagService.delete(id);
		return "redirect:/tag";
	}
}
