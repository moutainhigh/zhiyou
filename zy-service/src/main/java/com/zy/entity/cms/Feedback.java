package com.zy.entity.cms;

import com.zy.common.extend.StringBinder;
import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "cms_feedback")
@Getter
@Setter
@QueryModel
@Type(label = "反馈")
public class Feedback implements Serializable {

	public enum FeedbackStatus {
		等待客服接手, 客服已接手, 已关闭
	}

	public enum FeedbackType {
		申诉, 建议
	}

	@Id
	@Field(label = "id")
	private Long id;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "提交者id")
	private Long userId;

	@NotNull
	@Field(label = "反馈类型")
	private FeedbackType feedbackType;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "反馈状态")
	private FeedbackStatus feedbackStatus;

	@NotBlank
	@StringBinder
	@Column(length = 2000)
	@Field(label = "反馈内容")
	private String content;

	@StringBinder
	@Column(length = 2000)
	@Field(label = "客服回复")
	private String reply;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Field(label = "创建时间")
	private Date createdTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Field(label = "回复时间")
	private Date repliedTime;

}