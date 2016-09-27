package com.zy.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Profit.ProfitStatus;
import com.zy.entity.fnc.Profit.ProfitType;

@Getter
@Setter
public class ProfitDto implements Serializable {

	private Long id;

	private ProfitStatus profitStatus;

	private Long userId;

	private String sn;

	private String title;

	private CurrencyType currencyType;

	private BigDecimal amount;

	private Date createdTime;

	private Date grantedTime;

	private ProfitType profitType;

	private Long refId;

	private String remark;

}
