package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.mal.CartItem;
import com.zy.entity.mal.Product;
import com.zy.model.CartVo;
import com.zy.service.ProductService;
import com.zy.util.GcUtils;
import com.zy.vo.CartItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartComponent {

	@Autowired
	private ProductService productService;

	public CartVo buildCartVo(List<CartItem> cartItems) {

		CartVo cartVo = new CartVo();
		cartVo.setAmount(cartItems.stream().map(v -> v.getPrice().multiply(new BigDecimal(v.getQuantity()))).reduce(new BigDecimal("0.00"), (u, v) -> u.add(v)));
		cartVo.setTotalQuantity(cartItems.stream().mapToLong(CartItem::getQuantity).sum());
		cartVo.setCartItems(cartItems.stream().map(v -> {
			CartItemVo cartItemVo = new CartItemVo();
			BeanUtils.copyProperties(v, cartItemVo);
			Product product = productService.findOne(v.getProductId());
			if (product != null) {
				cartItemVo.setProductImageThumbnail(GcUtils.getThumbnail(product.getImage1(), 160, 160));
				cartItemVo.setMarketPrice(product.getMarketPrice());
				cartItemVo.setProductTitle(product.getTitle());
			}
			return cartItemVo;
		}).collect(Collectors.toList()));

		return cartVo;
	}

}
