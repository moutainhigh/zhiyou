package com.gc.vo;

import com.gc.entity.fnc.CurrencyType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class ProfitAdminVo implements Serializable {
	/* 原生 */
	private Long id;
	private Long userId;
	private String sn;
	private String title;
	private CurrencyType currencyType;
	private BigDecimal amount;
	private Date createdTime;
	private String bizName;
	private String remark;

	/* 扩展 */
	private UserAdminSimpleVo user;

}