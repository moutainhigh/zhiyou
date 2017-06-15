package com.zy.model;

import com.zy.vo.CartItemVo;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartVo {

	private Long totalQuantity;

	private BigDecimal amount;

	private List<CartItemVo> cartItems = new ArrayList<>();

}
