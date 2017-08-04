package com.zy.model.dto;

import com.zy.common.extend.StringBinder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class OrderDeliverDto implements Serializable {

	@NotNull
	private Long id;

	@NotNull
	private Boolean isUseLogistics;

	@StringBinder
	@Length(max = 20)
	private String logisticsName;

	@Length(max = 100)
	@StringBinder
	private String logisticsSn;

	@DecimalMin("0.00")
	private BigDecimal logisticsFee;


	private Integer isUku;
}
