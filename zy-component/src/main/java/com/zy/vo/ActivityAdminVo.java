package com.zy.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ActivityAdminVo implements Serializable {
	/* 原生 */
	private Long id;
	private String title;
	private Long areaId;
	private String address;
	private Double latitude;
	private Double longitude;
	private String image;
	private String detail;
	private Long signedInCount;
	private Long collectedCount;
	private Long appliedCount;
	private Long viewedCount;
	private Boolean isReleased;

	/* 扩展 */
	private String status;
	private String province;
	private String city;
	private String district;
	private String imageBig;
	private String imageThumbnail;
	private String applyDeadlineLabel;
	private String applyDeadlineFormatted;
	private String startTimeLabel;
	private String startTimeFormatted;
	private String endTimeLabel;
	private String endTimeFormatted;

}