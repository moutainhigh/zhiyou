package com.zy.component;

import static java.util.Objects.isNull;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.mal.Product;
import com.zy.util.GcUtils;
import com.zy.vo.ProductAdminVo;
import com.zy.vo.ProductDetailVo;
import com.zy.vo.ProductListVo;

@Component
public class ProductComponent {
	
	public ProductAdminVo buildAdminVo(Product product) {
		ProductAdminVo productAdminVo = new ProductAdminVo();
		BeanUtils.copyProperties(product, productAdminVo);
		
		BigDecimal price = product.getPrice();
		if(!isNull(price)) {
			productAdminVo.setPrice(price);
		}
		BigDecimal marketPrice = product.getMarketPrice();
		if(!isNull(marketPrice)) {
			productAdminVo.setMarketPrice(marketPrice);
		}
		productAdminVo.setImage1Thumbnail(GcUtils.getThumbnail(product.getImage1()));
		productAdminVo.setImage1Big(GcUtils.getThumbnail(product.getImage1(), 240, 150));
		return productAdminVo;
	}
	
	public ProductDetailVo buildDetailVo(Product product) {
		ProductDetailVo productDetailVo = new ProductDetailVo();
		BeanUtils.copyProperties(product, productDetailVo);
		
		BigDecimal price = product.getPrice();
		if(!isNull(price)) {
			productDetailVo.setPrice(price);
		}
		BigDecimal marketPrice = product.getMarketPrice();
		if(!isNull(marketPrice)) {
			productDetailVo.setMarketPrice(marketPrice);
		}
		productDetailVo.setImage1Thumbnail(GcUtils.getThumbnail(product.getImage1()));
		productDetailVo.setImage1Big(GcUtils.getThumbnail(product.getImage1(), 640, 640));
		return productDetailVo;
	}
	
	public ProductListVo buildListVo(Product product) {
		ProductListVo productListVo = new ProductListVo();
		BeanUtils.copyProperties(product, productListVo);
		
		BigDecimal price = product.getPrice();
		if(!isNull(price)) {
			productListVo.setPrice(price);
		}
		BigDecimal marketPrice = product.getMarketPrice();
		if(!isNull(marketPrice)) {
			productListVo.setMarketPrice(marketPrice);
		}
		productListVo.setImage1Thumbnail(GcUtils.getThumbnail(product.getImage1()));
		return productListVo;
	}
}
