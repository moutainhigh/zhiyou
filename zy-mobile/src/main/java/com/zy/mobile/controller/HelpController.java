package com.zy.mobile.controller;

import com.zy.common.support.cache.CacheSupport;
import com.zy.entity.cms.Help;
import com.zy.entity.cms.HelpCategory;
import com.zy.entity.usr.User.UserType;
import com.zy.model.query.HelpCategoryQueryModel;
import com.zy.service.HelpCategoryService;
import com.zy.service.HelpService;
import io.gd.generator.api.query.Direction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.zy.model.Constants.CACHE_NAME_HELP;
import static com.zy.model.Constants.CACHE_NAME_HELP_CATEGORY;

@RequestMapping("/help")
@Controller
public class HelpController {

	@Autowired
	private HelpCategoryService helpCategoryService;
	
	@Autowired
	private HelpService helpService;
	
	@Autowired
	protected CacheSupport cacheSupport;
	
	@RequestMapping
	public String index(Model model) {
		String sk = UserType.代理.toString();
		List<HelpCategory> helpCategories = cacheSupport.get(CACHE_NAME_HELP_CATEGORY, sk);
		if(helpCategories == null) {
			HelpCategoryQueryModel helpCategoryQueryModel = new HelpCategoryQueryModel();
			helpCategoryQueryModel.setUserTypeEQ(UserType.代理);
			helpCategoryQueryModel.setOrderBy("indexNumber");
			helpCategoryQueryModel.setDirection(Direction.ASC);
			helpCategories = helpCategoryService.findAll(helpCategoryQueryModel);
		}
		cacheSupport.set(CACHE_NAME_HELP_CATEGORY, sk, helpCategories);
		model.addAttribute("helpCategories", helpCategories);
		return "service/service";
	}
	
	@RequestMapping("/{code}")
	public String help(@PathVariable String code, Model model) {
		
		HelpCategoryQueryModel helpCategoryQueryModel = new HelpCategoryQueryModel();
		helpCategoryQueryModel.setCodeEQ(code);
		List<HelpCategory> helpCategories = helpCategoryService.findAll(helpCategoryQueryModel);
		if (helpCategories.isEmpty()) {
			return "redirect:/help";
		}
		
		HelpCategory helpCategory = helpCategories.get(0);
		model.addAttribute("helpCategoryName", helpCategory.getName());
		String helpCategoryId = helpCategory.getId() + "";
		List<Help> helps = cacheSupport.get(CACHE_NAME_HELP, helpCategoryId);
		if(helps == null) {
			helps = helpService.findByHelpCategoryId(helpCategory.getId());
		}
		cacheSupport.set(CACHE_NAME_HELP, helpCategoryId, helps);
		model.addAttribute("helps", helps);
		return "service/question";
	}
	
}
