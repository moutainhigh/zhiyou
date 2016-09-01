package com.zy.service;

import com.zy.entity.cms.ArticleCategory;
import com.zy.model.query.ArticleCategoryQueryModel;

import java.util.List;

public interface ArticleCategoryService {

	void delete(Long id);
	ArticleCategory findOne(Long id);
	ArticleCategory create(ArticleCategory articleCategory);
	ArticleCategory update(ArticleCategory articleCategory);
	ArticleCategory merge(ArticleCategory articleCategory, String... fields);
	
	List<ArticleCategory> findAllVisiable(Boolean isParentIdNull);
	List<ArticleCategory> findAll(ArticleCategoryQueryModel articleCategoryQueryModel);
	
}
