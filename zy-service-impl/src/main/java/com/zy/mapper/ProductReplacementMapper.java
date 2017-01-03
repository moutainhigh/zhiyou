package com.zy.mapper;


import com.zy.entity.mal.ProductReplacement;
import com.zy.model.query.ProductReplacementQueryModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ProductReplacementMapper {

	int insert(ProductReplacement productReplacement);

	int update(ProductReplacement productReplacement);

	int merge(@Param("productReplacement") ProductReplacement productReplacement, @Param("fields")String... fields);

	int delete(Long id);

	ProductReplacement findOne(Long id);

	List<ProductReplacement> findAll(ProductReplacementQueryModel productReplacementQueryModel);

	long count(ProductReplacementQueryModel productReplacementQueryModel);

}