package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.cms.Article;
import com.zy.model.query.ArticleQueryModel;


public interface ArticleMapper {

	int insert(Article article);

	int update(Article article);

	int merge(@Param("article") Article article, @Param("fields")String... fields);

	int delete(Article id);

	Article findOne(Long id);

	List<Article> findAll(ArticleQueryModel articleQueryModel);

	long count(ArticleQueryModel articleQueryModel);

	void view(Long articleId);

}