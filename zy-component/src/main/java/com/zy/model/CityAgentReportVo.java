package com.zy.model;

import com.zy.entity.usr.User.UserRank;
import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class CityAgentReportVo {

	@Field(label = "序号", order = 10)
	private Integer id;

	@Field(label = "省份", order = 20)
	private String city;

	@Field(label = "特技服务商人数", order = 30)
	private Integer v4Count;

	@Field(label = "省级服务商人数", order = 40)
	private Integer v3Count;

	@Field(label = "活跃特级服务商人数", order = 50)
	private Integer activeV4Count;

	@Field(label = "当前省份特级活跃度", order = 60)
	private Integer activeCityOrderNumber;
	
	@Getter
	@Setter
	public static class CityAgentReportVoQueryModel {

		private Integer pageNumber;
		private Integer pageSize;

	}
	
}
