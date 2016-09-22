package com.zy.entity.sys;

import com.zy.entity.usr.User;
import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.AssociationView;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

import static com.zy.entity.sys.Message.*;
import static io.gd.generator.api.query.Predicate.EQ;
import static io.gd.generator.api.query.Predicate.LK;

@Entity
@Table(name = "sys_message")
@Getter
@Setter
@QueryModel
@Type(label = "消息")
@ViewObject(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
public class Message implements Serializable {

	public static final String VO_ADMIN = "MessageAdminVo";
	public static final String VO_LIST = "MessageListVo";
	public static final String VO_DETAIL = "MessageDetailVo";

	public enum MessageType {
		系统消息, 资金变动, 活动通知;
	}

	@Id
	@Field(label = "id")
	@View
	private Long id;

	@Query(LK)
	@Field(label = "标题")
	@View
	private String title;

	@Query(LK)
	@Field(label = "类型")
	@View(groups = {VO_DETAIL, VO_ADMIN})
	private String content;

	@NotNull
	@Query(EQ)
	@Field(label = "用户id")
	@AssociationView(name = "user", groups = VO_ADMIN, associationGroup = User.VO_ADMIN_SIMPLE)
	private Long userId;

	@NotNull
	@Query(EQ)
	@Field(label = "是否已读")
	@View
	private Boolean isRead;

	@Temporal(TemporalType.TIMESTAMP)
	@Field(label = "创建时间")
	@View(groups = VO_ADMIN)
	@View(name = "createdTimeLabel", type = String.class, groups = {VO_LIST, VO_DETAIL})
	@NotNull
	private Date createdTime;

	@Temporal(TemporalType.TIMESTAMP)
	@View(groups = VO_ADMIN)
	private Date readTime;


	@Query(EQ)
	@Field(label = "消息类型")
	@View
	private MessageType messageType;

	@Query(EQ)
	@Field(label = "批号")
	@View(groups = VO_ADMIN)
	private String batchNumber;

	@Field(label = "token")
	@Column(length = 60, unique = true)
	@Query(EQ)
	@NotNull
	private String token;

}