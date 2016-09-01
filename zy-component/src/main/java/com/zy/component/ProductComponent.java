package com.gc.component;

import static java.util.Objects.isNull;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.gc.entity.mal.Product;
import com.zy.util.GcUtils;
import com.gc.vo.ProductAdminVo;
import com.gc.vo.ProductDetailVo;
import com.gc.vo.ProductListVo;

@Component
public class ProductComponent {
	
	@Autowired
	private CacheComponent cacheComponent;
	
	public ProductAdminVo buildAdminVo(Product product) {
		ProductAdminVo productAdminVo = new ProductAdminVo();
		BeanUtils.copyProperties(product, productAdminVo);
		
		BigDecimal price = product.getPrice();
		if(!isNull(price)) {
			productAdminVo.setPrice(price.toString());
		}
		BigDecimal marketPrice = product.getMarketPrice();
		if(!isNull(marketPrice)) {
			productAdminVo.setMarketPrice(marketPrice.toString());
		}
		productAdminVo.setStockQuantity(String.valueOf(product.getStockQuantity()));
		productAdminVo.setImage1Thumbnail(GcUtils.getThumbnail(product.getImage1()));
		return productAdminVo;
	}
	
	public ProductDetailVo buildDetailVo(Product product) {
		ProductDetailVo productDetailVo = new ProductDetailVo();
		BeanUtils.copyProperties(product, productDetailVo);
		
		BigDecimal price = product.getPrice();
		if(!isNull(price)) {
			productDetailVo.setPrice(price.toString());
		}
		BigDecimal marketPrice = product.getMarketPrice();
		if(!isNull(marketPrice)) {
			productDetailVo.setMarketPrice(marketPrice.toString());
		}
		productDetailVo.setStockQuantity(String.valueOf(product.getStockQuantity()));
		productDetailVo.setImage1Thumbnail(GcUtils.getThumbnail(product.getImage1()));
		return productDetailVo;
	}
	
	public ProductListVo buildListVo(Product product) {
		ProductListVo productListVo = new ProductListVo();
		BeanUtils.copyProperties(product, productListVo);
		
		BigDecimal price = product.getPrice();
		if(!isNull(price)) {
			productListVo.setPrice(price.toString());
		}
		BigDecimal marketPrice = product.getMarketPrice();
		if(!isNull(marketPrice)) {
			productListVo.setMarketPrice(marketPrice.toString());
		}
		productListVo.setImage1Thumbnail(GcUtils.getThumbnail(product.getImage1()));
		return productListVo;
	}
}
