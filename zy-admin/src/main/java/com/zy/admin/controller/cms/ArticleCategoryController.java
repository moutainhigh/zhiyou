package com.zy.admin.controller.cms;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.query.Page;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.gc.entity.cms.ArticleCategory;
import com.gc.model.query.ArticleCategoryQueryModel;
import com.gc.service.ArticleCategoryService;

@RequestMapping("/articleCategory")
@Controller
public class ArticleCategoryController {

	final Logger logger = LoggerFactory.getLogger(ArticleCategoryController.class);

	@Autowired
	private ArticleCategoryService articleCategoryService;
	

	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "cms/articleCategoryList";
	}

	@RequiresPermissions("article:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<ArticleCategory> list(ArticleCategoryQueryModel articleCategoryQueryModel) {
		
		Page<ArticleCategory> page = new Page<>();
		page.setData(new ArrayList<>());
		page.setPageNumber(0);
		page.setTotal(0L);
		page.setPageSize(20);
		
		articleCategoryQueryModel.setIsVisiableEQ(true);
		List<ArticleCategory> articleCategories = articleCategoryService.findAll(articleCategoryQueryModel);
		if(!articleCategories.isEmpty()) {
			page.setData(articleCategories);
			page.setTotal(Long.valueOf(articleCategories.size()));
		}
		return new Grid<ArticleCategory>(page);
	}

	@RequiresPermissions("article:edit")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Model model) {
		List<ArticleCategory> articleCategories = articleCategoryService.findAllVisiable(true);
		if(articleCategories != null) {
			model.addAttribute("articleCategories", articleCategories);
		}
		return "cms/articleCategoryCreate";
	}

	@RequiresPermissions("article:edit")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(ArticleCategory articleCategory, RedirectAttributes redirectAttributes) {
		articleCategory.setCreatedTime(new Date());
		validate(articleCategory);
		articleCategoryService.create(articleCategory);
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("保存成功"));
		return "redirect:/articleCategory";
	}

	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(Model model, Long id) {
		validate(id, NOT_NULL, "articleCategory id is null");

		ArticleCategory articleCategory = articleCategoryService.findOne(id);
		validate(articleCategory, NOT_NULL, "articleCategory id " + id + " is not found");
		model.addAttribute("articleCategory", articleCategory);
		
		List<ArticleCategory> articleCategories = articleCategoryService.findAllVisiable(true);
		if(articleCategories != null) {
			model.addAttribute("articleCategories", articleCategories);
		}
		
		return "cms/articleCategoryUpdate";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(ArticleCategory articleCategory, RedirectAttributes redirectAttributes) {

		validate(articleCategory, "id","name","indexNumber","isVisiable");

		articleCategoryService.merge(articleCategory, "parentId","name","indexNumber","isVisiable");
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("保存成功"));
		return "redirect:/articleCategory";
	}

}
