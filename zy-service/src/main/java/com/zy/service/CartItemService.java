package com.zy.service;

import com.zy.entity.mal.CartItem;

import java.util.List;

public interface CartItemService {

	void add(Long userId, Long productId, Long quantity);

	void modify(Long userId, Long productId, Long quantity);

	void remove(Long userId, Long productId);
	
	List<CartItem> findByUserId(Long userId);

	void deleteByUserId(Long userId);

	//void check(Long userId); TODO
}
