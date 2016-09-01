package com.gc.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.gc.entity.cms.ArticleCategory;
import com.gc.model.query.ArticleCategoryQueryModel;


public interface ArticleCategoryMapper {

	int insert(ArticleCategory articleCategory);

	int update(ArticleCategory articleCategory);

	int merge(@Param("articleCategory") ArticleCategory articleCategory, @Param("fields")String... fields);

	int delete(Long id);

	ArticleCategory findOne(Long id);

	List<ArticleCategory> findAll(ArticleCategoryQueryModel articleCategoryQueryModel);

	long count(ArticleCategoryQueryModel articleCategoryQueryModel);

	List<ArticleCategory> findAllVisiable(boolean isParentIdNull);

}