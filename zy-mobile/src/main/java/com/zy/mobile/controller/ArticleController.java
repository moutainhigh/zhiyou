package com.zy.mobile.controller;

import io.gd.generator.api.query.Direction;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.ArticleComponent;
import com.zy.entity.cms.Article;
import com.zy.model.query.ArticleQueryModel;
import com.zy.service.ArticleService;


@RequestMapping("/article")
@Controller
public class ArticleController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private ArticleComponent articleComponent;
		
	@RequestMapping
	public String list(Model model) {
		ArticleQueryModel articleQueryModel = new ArticleQueryModel();
		articleQueryModel.setIsReleasedEQ(true);
		articleQueryModel.setDirection(Direction.DESC);
		articleQueryModel.setOrderBy("releasedTime");
		articleQueryModel.setPageNumber(0);
		articleQueryModel.setPageSize(10);
		Page<Article> page = articleService.findPage(articleQueryModel);
		model.addAttribute("page", PageBuilder.copyAndConvert(page, articleComponent::buildListVo));
		model.addAttribute("timeLT", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		return "article/articleList";
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Result<?> ajax(Model model, Date timeLT, @RequestParam(required = true) Integer pageNumber) {
		if (timeLT == null) {
			timeLT = new Date();
		}
		ArticleQueryModel articleQueryModel = new ArticleQueryModel();
		articleQueryModel.setIsReleasedEQ(true);
		articleQueryModel.setDirection(Direction.DESC);
		articleQueryModel.setOrderBy("releasedTime");
		articleQueryModel.setPageNumber(pageNumber);
		articleQueryModel.setPageSize(10);
		Page<Article> page = articleService.findPage(articleQueryModel);
		
		Map<String, Object> map = new HashMap<>();
		map.put("page", PageBuilder.copyAndConvert(page, articleComponent::buildListVo));
		map.put("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));
			
		return ResultBuilder.result(map);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable Long id, Long inviterId, Model model) {
		Article article = articleService.findOne(id);
		//浏览+1
		articleService.visit(id);
		model.addAttribute("article", articleComponent.buildDetailVo(article));
		return "article/articleDetail";
	}

}
