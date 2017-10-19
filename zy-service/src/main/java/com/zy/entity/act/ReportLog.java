package com.zy.entity.act;

import com.zy.entity.sys.ConfirmStatus;
import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "act_report_log")
@Getter
@Setter
@Type(label = "检测报告日志记录")
@QueryModel
public class ReportLog {

	@Id
	@Field(label = "id")
	@Query({Predicate.EQ, Predicate.IN})
	private Long id;

	@NotNull
	@Field(label = "检测报告编号")
	@Query({Predicate.EQ, Predicate.IN})
	private Long reportId;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "检测报告初审状态")
	private ConfirmStatus reportPreConfirmStatus;

	@NotNull
	@Query(Predicate.EQ)
	@Field(label = "检测报告终审状态")
	private ConfirmStatus reportConfirmStatus;

	@NotNull
	@Field(label = "创建时间")
	private Date createdTime;

	@NotNull
	@Query({Predicate.GTE, Predicate.LT})
	private Long createId;

	@NotBlank
	@Field(label = "备注 ")
	private String remark;
}
