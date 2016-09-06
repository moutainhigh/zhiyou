package com.zy.service.impl;

import static com.zy.common.util.ValidateUtils.NOT_BLANK;
import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.zy.common.model.query.Page;
import com.zy.component.MalComponent;
import com.zy.entity.mal.Product;
import com.zy.entity.mal.Product.ProductPriceType;
import com.zy.entity.usr.User;
import com.zy.mapper.ProductMapper;
import com.zy.model.query.ProductQueryModel;
import com.zy.service.ProductService;

@Service
@Validated
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private MalComponent malComponent;

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
		product.setIsOn(false);
		validate(product);
		productMapper.insert(product);
	}

	@Override
	public Product modify(@NotNull Product product) {
		findAndValidate(product.getId());
		validate(product);
		productMapper.merge(product, "title", "detail", "productPriceType", "price", "priceScript", "marketPrice", "skuCode",
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
	public BigDecimal getPrice(Long productId, User.UserRank userRank, long quantity) {
		return malComponent.getPrice(productId, userRank, quantity);
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

	@Override
	public Product modifyPrice(Product product) {
		Long id = product.getId();
		Product persistence = findAndValidate(id);
		ProductPriceType productPriceType = persistence.getProductPriceType();
		validate(productPriceType, NOT_NULL, "product price type is null");

		Product productForMerge = new Product();
		productForMerge.setId(id);
		productForMerge.setProductPriceType(productPriceType);
		if(productPriceType == ProductPriceType.一般价格) {
			BigDecimal price = persistence.getPrice();
			validate(price, NOT_NULL, "product price is null");
			productForMerge.setPrice(price);
		} else if(productPriceType == ProductPriceType.脚本价格) {
			String priceScript = persistence.getPriceScript();
			validate(persistence.getPriceScript(), NOT_BLANK, "product price script is null");
			productForMerge.setPriceScript(priceScript);
		}
		productMapper.merge(productForMerge, "productPriceType", "price", "priceScript");

		return null;
	}

	private Product findAndValidate(Long id) {
		validate(id, NOT_NULL, "id is null");
		Product product = productMapper.findOne(id);
		validate(product, NOT_NULL, "product id" + id + " not found");
		return product;
	}

}
