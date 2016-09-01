package com.zy.vo;

import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.act.Report.ReportResult;
import com.zy.entity.usr.Portrait.Gender;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class ReportAdminVo implements Serializable {
	/* 原生 */
	private Long id;
	private Long userId;
	private String realname;
	private Integer age;
	private Gender gender;
	private ReportResult reportResult;
	private String text;
	private Date createdTime;
	private ConfirmStatus confirmStatus;
	private String confirmRemark;
	private Date appliedTime;
	private Date confirmedTime;
	private Boolean isSettledUp;

	/* 扩展 */
	private String dateLabel;
	private String image1Big;
	private String image1Thumbnail;
	private String image2Big;
	private String image2Thumbnail;
	private String image3Big;
	private String image3Thumbnail;
	private String image4Big;
	private String image4Thumbnail;
	private String image5Big;
	private String image5Thumbnail;
	private String image6Big;
	private String image6Thumbnail;

}