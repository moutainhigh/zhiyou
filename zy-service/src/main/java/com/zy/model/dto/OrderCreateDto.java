package com.zy.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

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

	private Long parentId; // 若用户下单parentId为空则必填
	
	@NotNull
	private Boolean isPayToPlatform;

}
