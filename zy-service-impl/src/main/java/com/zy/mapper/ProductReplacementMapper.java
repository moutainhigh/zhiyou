package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.mal.ProductReplacement;
import com.zy.model.query.ProductReplacementQueryModel;


public interface ProductReplacementMapper {

	int insert(ProductReplacement productReplacement);

	int update(ProductReplacement productReplacement);

	int merge(@Param("productReplacement") ProductReplacement productReplacement, @Param("fields")String... fields);

	int delete(Long id);

	ProductReplacement findOne(Long id);

	List<ProductReplacement> findAll(ProductReplacementQueryModel productReplacementQueryModel);

	long count(ProductReplacementQueryModel productReplacementQueryModel);

}