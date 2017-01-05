package com.zy.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class V4OrderReportVo {

	private String nickname;  //服务商昵称

	private String phone;  //服务商手机

	private Long v4ChildrenCount;  //下线团队特级人数

	private Long directV4ChildrenCount;  //直属特级人数

	private Long inQuantitySum;  //进货量
	private Long outQuantitySum;  //出货量

	private Long teamInQuantitySum;  //团队进货量
	private Long teamOutQuantitySum;  //团队出货量

	@Getter
	@Setter
	public static class V4OrderReportVoQueryModel {

		private Long directV4ParentIdEQ;
		private String nicknameLK;
		private String phoneEQ;
		private Date createdTimeGTE;
		private Date createdTimeLT;
		private Date paidTimeGTE;
		private Date paidTimeLT;
		private Integer pageNumber;
		private Integer pageSize;

	}
}
