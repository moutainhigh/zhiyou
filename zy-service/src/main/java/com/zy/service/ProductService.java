package com.gc.service;

import java.util.List;

import com.zy.common.model.query.Page;
import com.gc.entity.mal.Product;
import com.gc.model.query.ProductQueryModel;

public interface ProductService {

	void delete(Long id);
	Product findOne(Long id);
	void create(Product product);
	Page<Product> findPage(ProductQueryModel productQueryModel);
	List<Product> findAll(ProductQueryModel productQueryModel);
	Product modify(Product product);
	void on(Long id, Boolean isOn);
	
}
