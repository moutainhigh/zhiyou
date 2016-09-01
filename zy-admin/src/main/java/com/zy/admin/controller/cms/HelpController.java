package com.zy.admin.controller.cms;

import com.gc.entity.cms.Help;
import com.gc.entity.cms.HelpCategory;
import com.gc.entity.usr.User.UserType;
import com.gc.model.query.HelpCategoryQueryModel;
import com.gc.service.HelpCategoryService;
import com.gc.service.HelpService;
import com.zy.common.model.query.Page;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.common.support.cache.CacheSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.zy.model.Constants.CACHE_NAME_HELP;
import static com.zy.model.Constants.CACHE_NAME_HELP_CATEGORY;
import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@RequestMapping()
@Controller
public class HelpController {

	@Autowired
	private HelpService helpService;
	
	@Autowired
	private HelpCategoryService helpCategoryService;
	
	@Autowired
	protected CacheSupport cacheSupport;
	
	@RequestMapping(value = "/helpCategory", method = RequestMethod.GET)
	public String list() {
		return "cms/helpCategoryList";
	}
	
	@RequestMapping(value = "/helpCategory", method = RequestMethod.POST)
	@ResponseBody
	public Grid<HelpCategory> listAjax(HelpCategoryQueryModel helpCategoryQueryModel) {
		List<HelpCategory> list= helpCategoryService.findAll(helpCategoryQueryModel);
		Page<HelpCategory> page = new Page<>();
		page.setData(list);
		page.setPageNumber(0);
		page.setTotal(Long.valueOf(list.size()));
		page.setPageSize(100);
		return new Grid<HelpCategory>(page);
	}
	
	@RequestMapping(value = "/helpCategory/create", method = RequestMethod.GET)
	public String create() {
		return "cms/helpCategoryCreate";
	}
	
	@RequestMapping(value = "/helpCategory/create", method = RequestMethod.POST)
	public String create(HelpCategory helpCategory, Model model, RedirectAttributes redirectAttributes) {
		
		try {
			helpCategoryService.create(helpCategory);
			cacheDelete(CACHE_NAME_HELP_CATEGORY, UserType.代理.toString());
		} catch (Exception e) {
			model.addAttribute("helpCategory", helpCategory);
			model.addAttribute(e.getMessage());
			return "cms/helpCategoryCreate";
		}
		 
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("保存成功"));
		return "redirect:/helpCategory";
	}
	
	@RequestMapping(value = "/helpCategory/update/{helpCategoryId}", method = RequestMethod.GET)
	public String update(@PathVariable Long helpCategoryId, Model model) {
		validate(helpCategoryId, NOT_NULL, "help category id is null");
		HelpCategory helpCategory = helpCategoryService.findOne(helpCategoryId);
		model.addAttribute("helpCategory", helpCategory);
		return "cms/helpCategoryUpdate";
	}
	
	@RequestMapping(value = "/helpCategory/update", method = RequestMethod.POST)
	public String update(HelpCategory helpCategory, Model model, RedirectAttributes redirectAttributes) {
		Long helpCategoryId = helpCategory.getId();
		validate(helpCategoryId, NOT_NULL, "help category id is null");
		try {
			helpCategoryService.update(helpCategory);
			cacheDelete(CACHE_NAME_HELP_CATEGORY, UserType.代理.toString());
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error(e.getMessage()));
			return "redirect:/helpCategory/update/" + helpCategoryId;
		}
		return "redirect:/helpCategory";
	}
	
	@RequestMapping(value = "/help/{helpCategoryId}", method = RequestMethod.GET)
	public String list(@PathVariable Long helpCategoryId, Model model) {
		HelpCategory helpCategory = helpCategoryService.findOne(helpCategoryId);
		model.addAttribute("helpCategory", helpCategory);
		 return "cms/helpList";
	}
	
	@RequestMapping(value = "/help", method = RequestMethod.POST)
	@ResponseBody
	public Grid<Help> listAjax(Long helpCategoryId) {
		List<Help> list= helpService.findByHelpCategoryId(helpCategoryId);
		Page<Help> page = new Page<>();
		page.setData(list);
		page.setPageNumber(0);
		page.setTotal(Long.valueOf(list.size()));
		page.setPageSize(100);
		return new Grid<Help>(page);
	}
	
	@RequestMapping(value = "/help/create/{helpCategoryId}", method = RequestMethod.GET)
	public String createHelp(@PathVariable Long helpCategoryId, Model model) {
		model.addAttribute("helpCategoryId", helpCategoryId);
		 return "cms/helpCreate";
	}
	
	@RequestMapping(value = "/help/create", method = RequestMethod.POST)
	public String createHelp(Help help, Model model, RedirectAttributes redirectAttributes) {
		Long helpCategoryId = help.getHelpCategoryId();
		validate(helpCategoryId, NOT_NULL, "help category id is null");
		try {
			helpService.create(help);
			cacheDelete(CACHE_NAME_HELP, helpCategoryId + "");
		} catch (Exception e) {
			model.addAttribute("help", help);
			model.addAttribute(ResultBuilder.error(e.getMessage()));
			return "cms/helpCreate";
		}
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("保存成功"));
		return "redirect:/help/"+ helpCategoryId;
	}
	
	@RequestMapping(value = "/help/update/{helpId}", method = RequestMethod.GET)
	public String updateHelp(@PathVariable Long helpId, Model model) {
		Help help = helpService.findOne(helpId);
		validate(help, NOT_NULL, "help is null");
		model.addAttribute("help", help);
		return "cms/helpUpdate";
	}
	
	@RequestMapping(value = "/help/update", method = RequestMethod.POST)
	public String updateHelp(Help help, RedirectAttributes redirectAttributes) {
		Long helpId = help.getId();
		validate(help, NOT_NULL, "help id is null");
		try {
			helpService.update(help);
			Help ph = helpService.findOne(helpId);
			cacheDelete(CACHE_NAME_HELP, ph.getHelpCategoryId() + "");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error(e.getMessage()));
			return "redirect:/help/update/" + helpId;
		}
		return "redirect:/help/" + help.getHelpCategoryId();
	}
	
	private void cacheDelete(String cacheName, String key) {
		cacheSupport.delete(cacheName, key);
	}
}
