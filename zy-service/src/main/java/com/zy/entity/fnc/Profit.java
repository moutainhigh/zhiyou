package com.zy.entity.fnc;

import com.zy.entity.usr.User;
import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.AssociationView;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static com.zy.entity.fnc.Profit.VO_ADMIN;
import static io.gd.generator.api.query.Predicate.*;

@Entity
@Table(name = "fnc_profit")
@Getter
@Setter
@QueryModel
@Type(label = "收益单")
@ViewObject(groups = VO_ADMIN)
public class Profit implements Serializable {

	public static final String VO_ADMIN = "ProfitAdminVo";

	public enum ProfitType {
		补偿,
		订单收款, 
		数据奖,
		销量奖,
		特级平级奖,
		/* 下述为平台用 暂不使用 */
		/*平台收款,
		提现手续费*/
	}

	public enum ProfitStatus {
		待发放, 已发放, 已取消
	}

	@Id
	@Field(label = "id")
	@View
	private Long id;

	@NotNull
	@Field(label = "收益单状态")
	@Query(EQ)
	@View(groups = VO_ADMIN)
	@View(name = "profitStatusStyle", type = String.class, groups = {VO_ADMIN})
	private ProfitStatus profitStatus;

	@NotNull
	@Query({EQ,IN})
	@Field(label = "用户id")
	@View(groups = VO_ADMIN)
	@AssociationView(name = "user", groups = VO_ADMIN, associationGroup = User.VO_ADMIN_SIMPLE)
	private Long userId;

	@Column(length = 60, unique = true)
	@NotBlank
	@Field(label = "收益单号")
	@Query(EQ)
	@View(groups = VO_ADMIN)
	private String sn;

	@NotBlank
	@Field(label = "收益标题")
	@View(groups = VO_ADMIN)
	private String title;

	@NotNull
	@Field(label = "币种")
	@View(groups = VO_ADMIN)
	private CurrencyType currencyType;

	@NotNull
	@DecimalMin("0.01")
	@Field(label = "金额")
	@View(groups = VO_ADMIN)
	@View(name = "amountLabel", type = String.class, groups = VO_ADMIN)
	private BigDecimal amount;

	@NotNull
	@Query({GTE,LT})
	@Field(label = "创建时间")
	@View(groups = VO_ADMIN)
	@View(name = "createdTimeLabel", type = String.class, groups = VO_ADMIN)
	private Date createdTime;

	@Query({GTE,LT})
	@Field(label = "发放时间")
	@View(groups = VO_ADMIN)
	@View(name = "grantedTimeLabel", type = String.class, groups = VO_ADMIN)
	private Date grantedTime;

	@Query({Predicate.EQ, Predicate.IN})
	@Field(label = "收益单类型")
	@View(groups = VO_ADMIN)
	@NotNull
	private ProfitType profitType;

	@Field(label = "关联业务id", description = "可以不填写")
	@View(groups = VO_ADMIN)
	@Query(Predicate.EQ)
	private Long refId;

	@Field(label = "备注")
	@View(groups = VO_ADMIN)
	private String remark;

}
