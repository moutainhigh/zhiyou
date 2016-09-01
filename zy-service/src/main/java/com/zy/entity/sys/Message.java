package com.gc.entity.sys;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.AssociationView;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import io.gd.generator.api.query.Predicate;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sys_message")
@Getter
@Setter
@QueryModel
@Type(label = "消息")
@ViewObject(groups = {"MessageListVo", "MessageDetailVo", "MessageAdminVo"})
public class Message implements Serializable {

	public enum MessageType {
		系统消息, 任务通知, 资金变动, 活动通知;
	}

	@Id
	@Field(label = "id")
	@View
	private Long id;

	@Query(Predicate.LK)
	@Field(label = "标题")
	@View
	private String title;

	@Query(Predicate.LK)
	@Field(label = "类型")
	@View(groups = {"MessageDetailVo", "MessageAdminVo"})
	private String content;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "用户id")
	@AssociationView(name = "user", groups = "MessageAdminVo", associationGroup = "UserAdminSimpleVo")
	private Long userId;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "是否已读")
	@View
	private Boolean isRead;

	@Temporal(TemporalType.TIMESTAMP)
	@Field(label = "创建时间")
	@View(groups = "MessageAdminVo")
	@View(name = "createdTimeLabel", type = String.class, groups = {"MessageListVo", "MessageDetailVo"})
	private Date createdTime;

	@Temporal(TemporalType.TIMESTAMP)
	@View(groups = "MessageAdminVo")
	private Date readTime;


	@Query(Predicate.EQ)
	@Field(label = "消息类型")
	@View
	private MessageType messageType;

	@Query(Predicate.EQ)
	@Field(label = "批号")
	@View(groups = "MessageAdminVo")
	private String batchNumber;


}