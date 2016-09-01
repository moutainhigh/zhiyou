package com.zy.service;

import java.util.List;

import com.zy.common.model.query.Page;
import com.zy.entity.cms.Article;
import com.zy.model.query.ArticleQueryModel;

public interface ArticleService {

	void delete(Long id);
	Article findOne(Long id);
	Article create(Article article);
	Article update(Article article);
	Article merge(Article article, String... fields);
	
	Page<Article> findPage(ArticleQueryModel articleQueryModel);
	List<Article> findAll(ArticleQueryModel articleQueryModel);
	
}
