package com.zy.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
public class OrderCreateDto implements Serializable {

	@NotNull
	private Long userId;

	@NotNull
	private Long productId;

	@NotNull
	private Long quantity;

	@NotNull
	private Long addressId;

}
