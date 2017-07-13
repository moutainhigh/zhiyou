package com.zy.entity.tour;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.Query;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import io.gd.generator.api.query.Predicate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static com.zy.entity.tour.TourUser.VO_EXPORT;
import static com.zy.entity.tour.TourUser.VO_ADMIN;
import static com.zy.entity.tour.TourUser.VO_JOINEXPORT;
import static com.zy.entity.tour.TourUser.VO_LIST;


/**
 * Author: Xuwq
 * Date: 2017/6/29.
 */
@Entity
@Table(name = "ts_tour_user")
@Getter
@Setter
@ViewObject(groups = {VO_ADMIN, VO_LIST, VO_EXPORT, VO_JOINEXPORT})
@QueryModel
@Type(label = "旅游用户")
public class TourUser implements Serializable {

    public static final String VO_ADMIN = "TourUserAdminVo";
    public static final String VO_LIST = "TourUserListVo";
    public static final String VO_EXPORT = "TourUserExportVo";
    public static final String VO_JOINEXPORT = "TourUserExportVo";

    @Id
    @Field(label = "id")
    @View
    private Long id;

    @Id
    @Field(label = "tourTimeId")
    @View
    @Query(Predicate.EQ)
    private Long tourTimeId;

    @Id
    @Field(label = "tourId")
    @View
    @Query(Predicate.EQ)
    private Long tourId;

    @Id
    @Field(label = "userId")
    @View
    private Long userId;

    @Id
    @Field(label = "parentId")
    @View
    private Long parentId;

    @NotNull
    @View(name = "createDateLabel", type = String.class, groups = { VO_ADMIN, VO_EXPORT, VO_JOINEXPORT ,VO_LIST})
    @View(groups = { VO_ADMIN, VO_EXPORT, VO_JOINEXPORT,VO_LIST })
    @Field(label = "创建时间")
    private Date createDate;

    @NotNull
    @View(name = "updateDateLabel", type = String.class, groups = { VO_ADMIN  , VO_EXPORT, VO_JOINEXPORT,VO_LIST})
    @View(groups = { VO_ADMIN, VO_EXPORT, VO_JOINEXPORT ,VO_LIST})
    @Field(label = "更新时间")
    private Date updateDate;

    @Id
    @Field(label = "updateBy")
    @View
    private Long updateBy;

    @NotNull
    @Query(Predicate.EQ)
    @View(groups = { VO_ADMIN , VO_EXPORT, VO_JOINEXPORT ,VO_LIST})
    @Field(label = "是否有效")
    private Integer isEffect;

    @NotNull
    @Query(Predicate.EQ)
    @Field(label = "审核状态")
    @View(groups = { VO_ADMIN , VO_EXPORT, VO_JOINEXPORT,VO_LIST })
    private Integer auditStatus;

    @Id
    @Field(label = "reportId")
    @View
    @View(groups = { VO_ADMIN , VO_EXPORT, VO_JOINEXPORT,VO_LIST  })
    private Long reportId;

    @NotNull
    @Query(Predicate.EQ)
    @View(groups = { VO_ADMIN , VO_EXPORT, VO_JOINEXPORT ,VO_LIST})
    @Field(label = "是否接车")
    private Integer isTransfers;

    @NotNull
    @Query(Predicate.EQ)
    @View(groups = { VO_ADMIN , VO_EXPORT , VO_JOINEXPORT,VO_LIST})
    @Field(label = "车次")
    private String carNumber;

    @NotNull
    @Query(Predicate.EQ)
    @View(groups = { VO_ADMIN , VO_EXPORT, VO_JOINEXPORT ,VO_LIST})
    @Field(label = "旅游单号")
    private String sequenceId;

    @NotNull
    @View(name = "planTimeLabel", type = String.class, groups = { VO_ADMIN })
    @View(groups = { VO_ADMIN , VO_EXPORT, VO_JOINEXPORT ,VO_LIST})
    @Field(label = "计划到达时间")
    private Date planTime;

    @NotBlank
    @Field(label = "票务照片")
    @View(name = "imageThumbnai", type = String.class, groups = { VO_ADMIN , VO_EXPORT, VO_JOINEXPORT ,VO_LIST})
    private String carImages;

    @NotBlank
    @Field(label = "票务照片")
    @View(groups = { VO_ADMIN , VO_EXPORT, VO_JOINEXPORT ,VO_LIST})
    private String image;

    @NotBlank
    @Field(label = "用户备注")
    @View(groups = { VO_ADMIN , VO_EXPORT, VO_JOINEXPORT,VO_LIST })
    private String userRemark;

    @NotBlank
    @Field(label = "房型")
    @View(groups = { VO_ADMIN , VO_EXPORT, VO_JOINEXPORT,VO_LIST })
    private Integer houseType;

    @NotBlank
    @Field(label = "是否加床")
    @View(groups = { VO_ADMIN , VO_EXPORT, VO_JOINEXPORT,VO_LIST })
    private Integer isAddBed;

    @NotBlank
    @Field(label = "审核备注")
    @View(groups = { VO_ADMIN , VO_EXPORT, VO_JOINEXPORT,VO_LIST })
    private String revieweRemark;
//
//    @NotNull
//    @View(name = "departureTimeLabel", type = String.class, groups = { VO_ADMIN })
//    @View(groups = { VO_ADMIN , VO_EXPORT })
//    @Field(label = "计划离开时间")
//    private Date departureTime;

    @NotBlank
    @Field(label = "是否参游")
    @View(groups = { VO_ADMIN , VO_EXPORT, VO_JOINEXPORT ,VO_LIST })
    private Integer isJoin;

    @NotBlank
    @Field(label = "消费金额")
    @View(groups = { VO_ADMIN , VO_EXPORT, VO_JOINEXPORT,VO_LIST })
    private BigDecimal amount;
}

