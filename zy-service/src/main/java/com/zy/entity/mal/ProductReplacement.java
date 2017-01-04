package com.zy.entity.mal;

import com.zy.common.extend.StringBinder;
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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "mal_product_replacement")
@Getter
@Setter
@ViewObject(groups = {ProductReplacement.VO_LIST, ProductReplacement.VO_DETAIL, ProductReplacement.VO_ADMIN})
@QueryModel
@Type(label = "服务商换货")
public class ProductReplacement implements Serializable {

	public static final String VO_ADMIN = "ProductReplacementAdminVo";
	public static final String VO_LIST = "ProductReplacementListVo";
	public static final String VO_DETAIL = "ProductReplacementDetailVo";

	@Type(label = "更换状态")
	public enum ProductReplacementStatus {
		已申请, 已发货, 已完成, 已驳回
	}

	@Id
	@Field(label = "id")
	@View
	private Long id;

	@NotNull
	@Query({Predicate.EQ, Predicate.IN})
	@Field(label = "服务商")
	@View
	@AssociationView(name = "user", groups = {VO_ADMIN}, associationGroup = User.VO_ADMIN_SIMPLE)
	private Long userId;

	@NotNull
	@Query({Predicate.EQ})
	@Field(label = "更换状态")
	@View
	@View(name = "productReplacementStatusStyle", type = String.class, groups = {VO_ADMIN})
	private ProductReplacementStatus productReplacementStatus;

	@NotBlank
	@Query({Predicate.LK})
	@Field(label = "被更换产品")
	@View
	private String fromProduct;

	@NotBlank
	@Query({Predicate.LK})
	@Field(label = "更换产品")
	@View
	private String toProduct;

	@Min(1L)
	@Field(label = "数量")
	@View()
	private Long quantity;

	@NotNull
	@Field(label = "收件人区域")
	@View(groups = {VO_DETAIL, VO_ADMIN})
	private Long receiverAreaId;

	@NotBlank
	@StringBinder
	@Field(label = "收件人姓名")
	@View(groups = {VO_DETAIL, VO_ADMIN})
	private String receiverRealname;

	@NotBlank
	@StringBinder
	@Field(label = "收件人电话")
	@View(groups = {VO_DETAIL, VO_ADMIN})
	private String receiverPhone;

	@NotBlank
	@Field(label = "收件人省份")
	@View(groups = {VO_DETAIL, VO_ADMIN})
	private String receiverProvince;

	@NotBlank
	@Field(label = "收件人城市")
	@View(groups = {VO_DETAIL, VO_ADMIN})
	private String receiverCity;

	@NotBlank
	@Field(label = "收件人地区")
	@View(groups = {VO_DETAIL, VO_ADMIN})
	private String receiverDistrict;

	@NotBlank
	@StringBinder
	@Field(label = "收件人详细地址")
	@View(groups = {VO_DETAIL, VO_ADMIN})
	private String receiverAddress;

	@View(name = "deliveredTimeLabel", type = String.class, groups = {VO_DETAIL, VO_ADMIN, VO_LIST})
	@Field(label = "更换时间")
	@View(groups = {VO_DETAIL, VO_ADMIN})
	private Date createdTime;

	@View(name = "deliveredTimeLabel", type = String.class, groups = {VO_DETAIL, VO_ADMIN})
	@Field(label = "发货时间")
	@View(groups = {VO_DETAIL, VO_ADMIN})
	private Date deliveredTime;

	@Query(Predicate.LK)
	@Field(label = "物流公司名")
	@StringBinder
	@View(groups = {VO_DETAIL, VO_ADMIN})
	private String logisticsName;

	@Query(Predicate.LK)
	@Field(label = "物流单号")
	@StringBinder
	@View(groups = {VO_DETAIL, VO_ADMIN})
	private String logisticsSn;

	@DecimalMin("0.00")
	@Field(label = "物流费")
	@View(groups = {VO_DETAIL, VO_ADMIN})
	private BigDecimal logisticsFee;

	@View(groups = {VO_DETAIL, VO_ADMIN})
	@Field(label = "备注")
	private String remark;
}
