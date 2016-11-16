package com.zy.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositSumDto implements Serializable {

	private Integer count;
	
	private BigDecimal sumAmount;
	
}
