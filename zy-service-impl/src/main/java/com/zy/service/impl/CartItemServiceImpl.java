package com.zy.service.impl;

import com.zy.common.exception.BizException;
import com.zy.component.MalComponent;
import com.zy.entity.mal.CartItem;
import com.zy.entity.mal.Product;
import com.zy.entity.usr.User;
import com.zy.mapper.CartItemMapper;
import com.zy.mapper.ProductMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.BizCode;
import com.zy.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;


@Service
@Validated
public class CartItemServiceImpl implements CartItemService {

	@Autowired
	private CartItemMapper cartItemMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private MalComponent malComponent;

	@Override
	public void add(@NotNull Long userId, @NotNull Long productId, @NotNull @Min(1) Long quantity) {
		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + " is not found");

		Product product = productMapper.findOne(productId);
		validate(product, NOT_NULL, "product id " + productId + " is not found");

		if (!product.getIsOn()) {
			throw new BizException(BizCode.ERROR, "商品已下架,不能加入购物车");
		}

		CartItem cartItem = cartItemMapper.findByUserIdAndProductId(userId, productId);

		if (cartItem == null) {
			cartItem = new CartItem();
			cartItem.setUserId(userId);
			cartItem.setProductId(productId);
			cartItem.setQuantity(quantity);
			cartItem.setPrice(malComponent.getPrice(productId, user.getUserRank(), quantity));
			cartItem.setIsSettlement(product.getIsSettlement());
			validate(cartItem);
			cartItemMapper.insert(cartItem);
		} else {
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
			cartItemMapper.update(cartItem);
		}
	}


	@Override
	public void modify(@NotNull Long userId, @NotNull Long productId, @NotNull @Min(1) Long quantity) {
		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + " is not found");

		Product product = productMapper.findOne(productId);
		validate(product, NOT_NULL, "product id " + productId + " is not found");

		if (!product.getIsOn()) {
			throw new BizException(BizCode.ERROR, "商品已下架,不能加入购物车");
		}

		CartItem cartItem = cartItemMapper.findByUserIdAndProductId(userId, productId);

		if (cartItem == null) {
			cartItem = new CartItem();
			cartItem.setUserId(userId);
			cartItem.setProductId(productId);
			cartItem.setQuantity(quantity);
			cartItem.setPrice(malComponent.getPrice(productId, user.getUserRank(), quantity));
			cartItem.setIsSettlement(product.getIsSettlement());
			validate(cartItem);
			cartItemMapper.insert(cartItem);
		} else {
			cartItem.setQuantity(quantity);
			cartItemMapper.update(cartItem);
		}
	}

	@Override
	public void remove(@NotNull Long userId, @NotNull Long productId) {
		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + " is not found");

		CartItem cartItem = cartItemMapper.findByUserIdAndProductId(userId, productId);

		if (cartItem != null) {
			cartItemMapper.delete(cartItem.getId());
		}
	}

	@Override
	public List<CartItem> findByUserId(Long userId) {
		return cartItemMapper.findByUserId(userId);
	}

	@Override
	public void deleteByUserId(Long userId) {
		cartItemMapper.deleteByUserId(userId);
	}

}
