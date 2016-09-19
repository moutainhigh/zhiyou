package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.mal.Product;
import com.zy.entity.usr.User;
import com.zy.model.query.ProductQueryModel;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

	void delete(Long id);
	Product findOne(Long id);
	void create(Product product);
	Page<Product> findPage(ProductQueryModel productQueryModel);
	List<Product> findAll(ProductQueryModel productQueryModel);
	Product modify(Product product);
	Product modifyPrice(Product product);
	void on(Long id, Boolean isOn);

	BigDecimal getPrice(Long productId, User.UserRank userRank, long quantity);

}
