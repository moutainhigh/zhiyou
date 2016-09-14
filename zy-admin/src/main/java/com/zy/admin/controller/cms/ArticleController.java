package com.zy.admin.controller.cms;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.model.Constants.ALIYUN_BUCKET_NAME_IMAGE;
import static com.zy.model.Constants.ALIYUN_URL_IMAGE;
import io.gd.generator.api.query.Direction;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.query.Page;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.common.support.AliyunOssSupport;
import com.zy.entity.cms.Article;
import com.zy.entity.cms.ArticleCategory;
import com.zy.model.query.ArticleQueryModel;
import com.zy.service.ArticleCategoryService;
import com.zy.service.ArticleService;

@Controller
@RequestMapping("/article")
public class ArticleController {

	final Logger logger = LoggerFactory.getLogger(ArticleController.class);

	@Autowired
	private ArticleService articleService;

	@Autowired
	private ArticleCategoryService articleCategoryService;
	
	@Autowired
	private AliyunOssSupport aliyunOssSupport;

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		List<ArticleCategory> articleCategories = articleCategoryService.findAllVisiable(false);
		model.addAttribute("articleCategories", articleCategories);
		return "cms/articleList";
	}

	@RequiresPermissions("article:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<Article> list(ArticleQueryModel articleQueryModel) {
		articleQueryModel.setOrderBy("createdTime");
		articleQueryModel.setDirection(Direction.DESC);
		Page<Article> page = articleService.findPage(articleQueryModel);
		return new Grid<Article>(page);
	}

	@RequiresPermissions("article:edit")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Model model, RedirectAttributes redirectAttributes) {
		List<ArticleCategory> articleCategories = articleCategoryService.findAllVisiable(false);
		if(articleCategories.isEmpty()) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error("请先添加文章类别, 自动跳转"));
			return "/articleCategory/create";
		}
		model.addAttribute("articleCategories", articleCategories);
		return "cms/articleCreate";
	}

	@RequiresPermissions("article:edit")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(Article article, RedirectAttributes redirectAttributes) {

		Date date = new Date();
		article.setCreatedTime(date);
		article.setVisitCount(0L);
		article.setIsReleased(false);
		validate(article);
		articleService.create(article);
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("文章保存成功"));
		return "redirect:/article";
	}

	@RequestMapping(value = {"/update"}, method = RequestMethod.GET)
	public String update(Model model, Long id) {
		validate(id, NOT_NULL, "article id is null");

		Article article = articleService.findOne(id);
		validate(article, NOT_NULL, "article id " + id + " is not found");

		List<ArticleCategory> articleCategories = articleCategoryService.findAllVisiable(false);
		model.addAttribute("articleCategories", articleCategories);
		
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
		validate(article, "articleCategoryId", "title", "brief", "content", "releasedTime", "author");

		articleService.merge(article, "articleCategoryId", "title", "brief", "content", "releasedTime", "author");
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("文章保存成功"));
		return "redirect:/article";
	}

	@RequiresPermissions("article:edit")
	@RequestMapping(value = "/releaseAjax", method = RequestMethod.POST)
	@ResponseBody
	public boolean releaseAjax(@RequestParam Long id, @RequestParam boolean isRelease) {
		Article article = articleService.findOne(id);
		validate(article, NOT_NULL, "article not found, id is " + id);

		Article articleForMerge = new Article();
		articleForMerge.setIsReleased(isRelease);
		articleForMerge.setId(id);
		articleService.merge(articleForMerge, "isReleased");
		return true;
	}

	@RequiresPermissions("article:edit")
	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	public String uploadImage(MultipartFile file, HttpServletRequest request, Model model, String callback) throws IOException {
		model.addAttribute("callback", callback);
		try {
			String imageUrl = aliyunOssSupport.putPublicObject(ALIYUN_BUCKET_NAME_IMAGE, "article/", file, ALIYUN_URL_IMAGE);
			model.addAttribute("result", imageUrl);
		} catch (Exception e) {
			model.addAttribute("result", e.getMessage());
		}
		return "cms/imageCallback";
	}
}
