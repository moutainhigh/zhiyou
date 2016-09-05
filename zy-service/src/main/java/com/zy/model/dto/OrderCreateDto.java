package com.zy.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OrderCreateDto implements Serializable {


	private Long userId;

	private Long productId;

	private Long quantity;

	private Long addressId;

}
