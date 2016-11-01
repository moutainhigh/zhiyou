package com.zy.model;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamReportVo {

	private Long userId;

	@Field(label = "服务商", order = 10)
	private String nickname;

	@Field(label = "系统", order = 20)
	private String rootRootName;

	@Field(label = "直属特级", order = 30)
	private String v4UserNickname;

	@Field(label = "一级", order = 40)
	private Long v1Count;

	@Field(label = "二级", order = 50)
	private Long v2Count;

	@Field(label = "三级", order = 60)
	private Long v3Count;

	@Field(label = "意向服务商", order = 70)
	private Long v0Count;

	@Field(label = "服务商总数量", order = 80)
	private Long v123Count;

	@Getter
	@Setter
	public static class TeamReportQueryModel {
		private Long provinceIdEQ;
		private Long cityIdEQ;
		private Long districtIdEQ;

		private String rootRootNameLK;

		private String v4UserNicknameLK;

		private String phoneEQ;
		private String nickNameLK;

		private Integer pageNumber;
		private Integer pageSize;

	}

}
