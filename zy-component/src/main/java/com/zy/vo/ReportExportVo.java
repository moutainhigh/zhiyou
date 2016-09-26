package com.zy.vo;

import io.gd.generator.annotation.Field;
import com.zy.entity.act.Report.ReportResult;
import com.zy.entity.usr.Portrait.Gender;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
public class ReportExportVo implements Serializable {
	/* 原生 */
	@Field(label = "报告编号", order = 10)
	private Long id;
	@Field(label = "客户姓名", order = 20)
	private String realname;
	@Field(label = "年龄", order = 30)
	private Integer age;
	@Field(label = "性别", order = 40)
	private Gender gender;
	@Field(label = "手机号", order = 50)
	private String phone;
	@Field(label = "检测结果", order = 60)
	private ReportResult reportResult;
	@Field(label = "检测心得", order = 70)
	private String text;

	/* 扩展 */
	@Field(label = "图片")
	private List<String> images = new ArrayList<>();
	@Field(label = "图片")
	private List<String> imageThumbnails = new ArrayList<>();
	@Field(label = "图片")
	private List<String> imageBigs = new ArrayList<>();

}