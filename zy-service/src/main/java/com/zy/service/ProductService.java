package com.zy.service;

import java.util.List;

import com.zy.common.model.query.Page;
import com.zy.entity.mal.Product;
import com.zy.model.query.ProductQueryModel;

public interface ProductService {

	void delete(Long id);
	Product findOne(Long id);
	void create(Product product);
	Page<Product> findPage(ProductQueryModel productQueryModel);
	List<Product> findAll(ProductQueryModel productQueryModel);
	Product modify(Product product);
	Product modifyPrice(Product product);
	void release(Long id, Boolean isRelease);
	
}
