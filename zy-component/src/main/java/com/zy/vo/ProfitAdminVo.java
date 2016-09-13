package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.fnc.Profit.ProfitStatus;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Profit.ProfitType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class ProfitAdminVo implements Serializable {
	/* 原生 */
	@Field(label = "id")
	private Long id;
	@Field(label = "收益单状态")
	private ProfitStatus profitStatus;
	@Field(label = "用户id")
	private Long userId;
	@Field(label = "收益单号")
	private String sn;
	@Field(label = "收益标题")
	private String title;
	@Field(label = "币种")
	private CurrencyType currencyType;
	@Field(label = "金额")
	private BigDecimal amount;
	@Field(label = "创建时间")
	private Date createdTime;
	@Field(label = "发放时间")
	private Date grantedTime;
	@Field(label = "收益单类型")
	private ProfitType profitType;
	@Field(label = "关联业务id")
	private Long refId;
	@Field(label = "备注")
	private String remark;

	/* 扩展 */
	@Field(label = "用户id")
	private UserAdminSimpleVo user;
	@Field(label = "创建时间")
	private String createdTimeLabel;
	@Field(label = "发放时间")
	private String grantedTimeLabel;

}