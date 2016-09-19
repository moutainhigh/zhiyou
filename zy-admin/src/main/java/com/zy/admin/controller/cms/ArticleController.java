package com.zy.admin.controller.cms;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;
import io.gd.generator.api.query.Direction;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.common.support.AliyunOssSupport;
import com.zy.component.ArticleComponent;
import com.zy.entity.cms.Article;
import com.zy.model.Constants;
import com.zy.model.query.ArticleQueryModel;
import com.zy.service.ArticleService;
import com.zy.vo.ArticleAdminVo;

@Controller
@RequestMapping("/article")
public class ArticleController {

	final Logger logger = LoggerFactory.getLogger(ArticleController.class);

	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private ArticleComponent articleComponent;
	
	@Autowired
	private AliyunOssSupport aliyunOssSupport;

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "cms/articleList";
	}

	@RequiresPermissions("article:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<ArticleAdminVo> list(ArticleQueryModel articleQueryModel) {
		articleQueryModel.setOrderBy("createdTime");
		articleQueryModel.setDirection(Direction.DESC);
		Page<Article> page = articleService.findPage(articleQueryModel);
		return new Grid<ArticleAdminVo>(PageBuilder.copyAndConvert(page, new ArrayList<ArticleAdminVo>()));
	}

	@RequiresPermissions("article:edit")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Model model, RedirectAttributes redirectAttributes) {
		
		return "cms/articleCreate";
	}

	@RequiresPermissions("article:edit")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(Article article, Model model, RedirectAttributes redirectAttributes) {

		try {
			articleService.create(article);
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("新闻保存成功"));
			return "redirect:/article";
		} catch (Exception e) {
			model.addAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			model.addAttribute("article", article);
			return "cms/articleCreate";
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(Model model, @RequestParam Long id) {
		validate(id, NOT_NULL, "article id is null");

		Article article = articleService.findOne(id);
		validate(article, NOT_NULL, "article id " + id + " is not found");

		String content = article.getContent();
		content = StringUtils.replaceEach(content, new String[] {"'", "\r\n", "\r", "\n"}, new String[] {"\\'", "", "", ""});
		article.setContent(content);
		model.addAttribute("article", article);
		return "cms/articleUpdate";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Article article, RedirectAttributes redirectAttributes) {
		Long id = article.getId();
		validate(id, NOT_NULL, "article id is null");

		try {
			articleService.modify(article);
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("新闻编辑成功"));
			return "redirect:/article";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return "redirect:/article/update?id=" + article.getId();
		}
		
	}

	@RequiresPermissions("article:edit")
	@RequestMapping(value = "/release", method = RequestMethod.POST)
	@ResponseBody
	public boolean release(@RequestParam Long id, @RequestParam boolean isRelease) {
		Article article = articleService.findOne(id);
		validate(article, NOT_NULL, "article not found, id is " + id);

		articleService.release(id, isRelease);
		return true;
	}


}
