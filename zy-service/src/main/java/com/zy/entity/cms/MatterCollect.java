package com.zy.entity.cms;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.View;
import io.gd.generator.annotation.view.ViewObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

import static com.zy.entity.cms.Article.*;

/**
 * Author: Xuwq
 * Date: 2017/4/5.
 */
@Entity
@Table(name = "cms_matter_collect")
@Getter
@Setter
@ViewObject(groups = {VO_LIST, VO_DETAIL, VO_ADMIN})
@QueryModel
@Type(label = "资源关注")
public class MatterCollect implements Serializable {

    @Id
    @Field(label = "id")
    @View
    private Long id;

    @Id
    @Field(label = "matterId")
    @View
    private Long matterId;

    @Id
    @Field(label = "userId")
    @View
    private Long userId;

    @NotNull
    @Field(label = "关注时间")
    private Date createTime;
}
