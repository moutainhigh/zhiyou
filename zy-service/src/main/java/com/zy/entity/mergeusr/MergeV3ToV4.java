package com.zy.entity.mergeusr;

import io.gd.generator.annotation.Type;
import io.gd.generator.annotation.query.QueryModel;
import io.gd.generator.annotation.view.ViewObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

import static com.zy.entity.mergeusr.MergeV3ToV4.VO_ADMIN;

/**
 * Created by it001 on 2017-11-07.
 */
@Entity
@Table(name = "td_v3_to_v4")
@Getter
@Setter
@QueryModel
@Type(label = "用户")
@ViewObject(groups = { VO_ADMIN})
public class MergeV3ToV4 implements Serializable {

    public static final String VO_ADMIN = "MergeV3ToV4AdminVo";



    private Long id;


    private String name;


    private  Long UserId;


    private  Integer flage;


    private Long create_by;


    private Date create_date;


    private Long update_by;


    private Date update_date;


    private String image1;


    private String image2;


    private Integer delFlage;

}
