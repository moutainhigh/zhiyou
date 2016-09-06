package com.zy.component;

import static java.util.Objects.isNull;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.mal.Product;
import com.zy.util.GcUtils;
import com.zy.vo.ProductAdminVo;
import com.zy.vo.ProductDetailVo;
import com.zy.vo.ProductListVo;

@Component
public class ProductComponent {
	
	@Autowired
	private CacheComponent cacheComponent;
	
	public ProductAdminVo buildAdminVo(Product product) {
		ProductAdminVo productAdminVo = new ProductAdminVo();
		BeanUtils.copyProperties(product, productAdminVo);
		
		BigDecimal price = product.getPrice();
		if(!isNull(price)) {
			productAdminVo.setPriceLabel(price.toString());
		}
		BigDecimal marketPrice = product.getMarketPrice();
		if(!isNull(marketPrice)) {
			productAdminVo.setMarketPriceLabel(marketPrice.toString());
		}
		productAdminVo.setImage1Thumbnail(GcUtils.getThumbnail(product.getImage1()));
		return productAdminVo;
	}
	
	public ProductDetailVo buildDetailVo(Product product) {
		ProductDetailVo productDetailVo = new ProductDetailVo();
		BeanUtils.copyProperties(product, productDetailVo);
		
		BigDecimal price = product.getPrice();
		if(!isNull(price)) {
			productDetailVo.setPriceLabel(price.toString());
		}
		BigDecimal marketPrice = product.getMarketPrice();
		if(!isNull(marketPrice)) {
			productDetailVo.setMarketPriceLabel(marketPrice.toString());
		}
		productDetailVo.setImage1Thumbnail(GcUtils.getThumbnail(product.getImage1()));
		return productDetailVo;
	}
	
	public ProductListVo buildListVo(Product product) {
		ProductListVo productListVo = new ProductListVo();
		BeanUtils.copyProperties(product, productListVo);
		
		BigDecimal price = product.getPrice();
		if(!isNull(price)) {
			productListVo.setPriceLabel(price.toString());
		}
		BigDecimal marketPrice = product.getMarketPrice();
		if(!isNull(marketPrice)) {
			productListVo.setMarketPriceLabel(marketPrice.toString());
		}
		productListVo.setImage1Thumbnail(GcUtils.getThumbnail(product.getImage1()));
		return productListVo;
	}
}
