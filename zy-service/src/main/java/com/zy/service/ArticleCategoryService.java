package com.gc.service;

import com.gc.entity.cms.ArticleCategory;
import com.gc.model.query.ArticleCategoryQueryModel;

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
