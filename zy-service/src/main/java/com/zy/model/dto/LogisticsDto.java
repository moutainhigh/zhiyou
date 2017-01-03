package com.zy.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class LogisticsDto implements Serializable {

	@NotBlank
	private String name;

	@NotBlank
	private String sn;

	@NotNull
	private BigDecimal fee;
}
