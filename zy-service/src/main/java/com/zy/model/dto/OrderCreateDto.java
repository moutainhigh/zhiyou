package com.zy.model.dto;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateDto implements Serializable {

	@Length(max = 100)
	private String title;

	@NotNull
	private Long userId;

	@NotNull
	private Long productId;

	@Min(1)
	private long quantity;

	@NotNull
	private Long addressId;

	@Length(max = 100)
	private String buyerMemo;

	private Long parentId;
	
	@NotNull
	private Boolean isPayToPlatform;

}
