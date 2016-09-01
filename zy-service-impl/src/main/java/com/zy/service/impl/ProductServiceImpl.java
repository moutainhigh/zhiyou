package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.mal.Product;
import com.zy.mapper.ProductMapper;
import com.zy.model.query.ProductQueryModel;
import com.zy.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductMapper productMapper;

	
	@Override
	public void delete(@NotNull Long id) {
		productMapper.delete(id);		
	}

	@Override
	public Product findOne(@NotNull Long id) {
		return productMapper.findOne(id);
	}

	@Override
	public void create(@NotNull Product product) {
		product.setCreatedTime(new Date());
		validate(product);
		productMapper.insert(product);
	}

	@Override
	public Product modify(@NotNull Product product) {
		productMapper.merge(product, "title", "detail", "price", "marketPrice", "skuCode", "stockQuantity", "lockedCount", 
				"image1", "image2", "image3", "image4", "image5", "image6");
		return product;
	}
	
	@Override
	public void on(@NotNull Long id, @NotNull Boolean isOn) {
		Product product = productMapper.findOne(id);
		validate(product, NOT_NULL, "product is null");
		
		product.setIsOn(isOn);
		productMapper.update(product);
	}

	@Override
	public Page<Product> findPage(@NotNull ProductQueryModel productQueryModel) {
		if(productQueryModel.getPageNumber() == null)
			productQueryModel.setPageNumber(0);
		if(productQueryModel.getPageSize() == null)
			productQueryModel.setPageSize(20);
		long total = productMapper.count(productQueryModel);
		List<Product> data = productMapper.findAll(productQueryModel);
		Page<Product> page = new Page<>();
		page.setPageNumber(productQueryModel.getPageNumber());
		page.setPageSize(productQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public List<Product> findAll(@NotNull ProductQueryModel productQueryModel) {
		return productMapper.findAll(productQueryModel);
	}

}
