package com.zy.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamReportVo {

	private Long userId;

	private String nickname;

	private String rootRootName;

	private String v4UserNickname;

	private Long v1Count;

	private Long v2Count;

	private Long v3Count;

	private Long v0Count;

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
