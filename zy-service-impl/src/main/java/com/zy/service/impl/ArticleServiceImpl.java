package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.cms.Article;
import com.zy.mapper.ArticleMapper;
import com.zy.model.query.ArticleQueryModel;
import com.zy.service.ArticleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import java.util.List;

import static com.zy.common.util.ValidateUtils.validate;

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
	public Article merge(@NotNull Article article, @NotNull
			  String... fields) {
		articleMapper.merge(article, fields);
		return article;
	}


}
