package com.zy.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSumDto implements Serializable {

	@NotNull
	private Long id;
	
	private Integer countNumber;
	
	private Integer sumQuantity;
	
	private BigDecimal sumAmount;
	
}
