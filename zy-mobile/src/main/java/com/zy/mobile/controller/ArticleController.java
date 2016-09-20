package com.zy.mobile.controller;

import io.gd.generator.api.query.Direction;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zy.common.model.query.Page;
import com.zy.component.ArticleComponent;
import com.zy.entity.cms.Article;
import com.zy.model.query.ArticleQueryModel;
import com.zy.service.ArticleService;
import com.zy.vo.ArticleListVo;


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
		Page<Article> page = articleService.findPage(articleQueryModel);
		List<ArticleListVo> list = page.getData().stream().map(v -> {
			return articleComponent.buildListVo(v);
			}).collect(Collectors.toList());
		model.addAttribute("articles", list);
		return "article/articleList";
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
