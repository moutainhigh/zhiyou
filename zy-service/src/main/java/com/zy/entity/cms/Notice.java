package com.gc.entity.cms;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.api.query.Predicate;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "cms_notice")
@Getter
@Setter
@QueryModel
@Type(label = "公告")
public class Notice implements Serializable {

	public enum NoticeType {
		全体公告, 代理公告
	}

	@Id
	@Field(label = "id")
	private Long id;

	@NotBlank
	@Query(Predicate.LK)
	@Field(label = "标题")
	private String title;

	@Lob
	@NotBlank
	@Field(label = "内容")
	private String content;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Field(label = "创建时间")
	private Date createdTime;

	@NotNull
	@Query({Predicate.EQ, Predicate.IN})
	@Field(label = "通知类型")
	private NoticeType noticeType;


}