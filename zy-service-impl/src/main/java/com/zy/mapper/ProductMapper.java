package com.gc.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.gc.entity.mal.Product;
import com.gc.model.query.ProductQueryModel;


public interface ProductMapper {

	int insert(Product product);

	int update(Product product);

	int merge(@Param("product") Product product, @Param("fields")String... fields);

	int delete(Long id);

	Product findOne(Long id);

	List<Product> findAll(ProductQueryModel productQueryModel);

	long count(ProductQueryModel productQueryModel);

}