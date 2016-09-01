package com.gc.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ActivityListVo implements Serializable {
	/* 原生 */
	private Long id;
	private String title;
	private String address;
	private Long collectedCount;
	private Long appliedCount;
	private Long viewedCount;

	/* 扩展 */
	private String status;
	private String province;
	private String city;
	private String district;
	private String imageBig;
	private String imageThumbnail;
	private String applyDeadlineLabel;
	private String startTimeLabel;
	private String endTimeLabel;

}