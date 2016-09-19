package com.zy.service.impl;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.zy.common.model.query.Page;
import com.zy.entity.cms.Article;
import com.zy.mapper.ArticleMapper;
import com.zy.model.query.ArticleQueryModel;
import com.zy.service.ArticleService;

@Service
@Validated
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleMapper articleMapper;
	
	@Override
	public void delete(@NotNull Long id) {
		articleMapper.delete(id);		
	}

	@Override
	public Article findOne(@NotNull Long id) {
		return articleMapper.findOne(id);
	}

	@Override
	public Article create(@NotNull Article article) {
		article.setCreatedTime(new Date());
		article.setVisitCount(0L);
		article.setIsReleased(false);
		validate(article);
		articleMapper.insert(article);
		return article;
	}

	@Override
	public Article update(@NotNull Article article) {
		articleMapper.update(article);
		return article;
	}

	@Override
	public Page<Article> findPage(@NotNull ArticleQueryModel articleQueryModel) {
		if(articleQueryModel.getPageNumber() == null)
			articleQueryModel.setPageNumber(0);
		if(articleQueryModel.getPageSize() == null)
			articleQueryModel.setPageSize(20);
		long total = articleMapper.count(articleQueryModel);
		List<Article> data = articleMapper.findAll(articleQueryModel);
		Page<Article> page = new Page<>();
		page.setPageNumber(articleQueryModel.getPageNumber());
		page.setPageSize(articleQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public List<Article> findAll(@NotNull ArticleQueryModel articleQueryModel) {
		return articleMapper.findAll(articleQueryModel);
	}

	@Override
	public Article modify(@NotNull Article article) {
		Long id = article.getId();
		Article persistence = articleMapper.findOne(id);
		validate(persistence, NOT_NULL, "article id " + id + " not found");
		
		persistence.setAuthor(article.getAuthor());
		persistence.setBrief(article.getBrief());
		persistence.setContent(article.getContent());
		persistence.setImage(article.getImage());
		persistence.setTitle(article.getTitle());
		persistence.setReleasedTime(article.getReleasedTime());
		articleMapper.update(persistence);
		return null;
	}

	@Override
	public void release(@NotNull Long id, boolean isReleased) {
		Article article = articleMapper.findOne(id);
		validate(article, NOT_NULL, "article id " + id + " not found");
		Article articleForMerge = new Article();
		articleForMerge.setId(id);
		articleForMerge.setIsReleased(isReleased);
		
		articleMapper.merge(articleForMerge, "isReleased");
	}

	@Override
	public void visit(@NotNull Long articleId) {
		checkAndFindArticle(articleId);
		articleMapper.view(articleId);
	}
	
	private Article checkAndFindArticle(Long articleId) {
		validate(articleId, NOT_NULL, "article id is null");
		Article article = articleMapper.findOne(articleId);
		validate(article, NOT_NULL, "article id " + articleId + " is not found");
		return article;
	}
}
