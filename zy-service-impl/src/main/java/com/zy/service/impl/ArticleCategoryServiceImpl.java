package com.gc.service.impl;

import com.gc.entity.cms.ArticleCategory;
import com.gc.mapper.ArticleCategoryMapper;
import com.gc.model.query.ArticleCategoryQueryModel;
import com.gc.service.ArticleCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import java.util.List;

import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class ArticleCategoryServiceImpl implements ArticleCategoryService {

	@Autowired
	private ArticleCategoryMapper articleCategoryMapper;
	
	@Override
	public void delete(@NotNull Long id) {
		articleCategoryMapper.delete(id);	
	}

	@Override
	public ArticleCategory findOne(@NotNull Long id) {
		return articleCategoryMapper.findOne(id);
	}

	@Override
	public ArticleCategory create(@NotNull ArticleCategory articleCategory) {
		validate(articleCategory);
		articleCategoryMapper.insert(articleCategory);
		return articleCategory;
	}

	@Override
	public ArticleCategory update(@NotNull ArticleCategory articleCategory) {
		articleCategoryMapper.update(articleCategory);
		return articleCategory;
	}

	@Override
	public List<ArticleCategory> findAll(
			@NotNull ArticleCategoryQueryModel articleCategoryQueryModel) {
		return articleCategoryMapper.findAll(articleCategoryQueryModel);
	}

	@Override
	public ArticleCategory merge(@NotNull ArticleCategory articleCategory,
			@NotNull String... fields) {
		articleCategoryMapper.merge(articleCategory, fields);
		return articleCategory;
	}

	@Override
	public List<ArticleCategory> findAllVisiable(@NotNull Boolean isParentIdNull) {
		return articleCategoryMapper.findAllVisiable(isParentIdNull);
	}


}
