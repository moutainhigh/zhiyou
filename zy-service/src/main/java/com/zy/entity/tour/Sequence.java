package com.zy.entity.tour;

import io.gd.generator.annotation.Field;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by it001 on 2017/6/27.
 *   模拟 序列化
 */
@Entity
@Table(name = "act_activity")
@Getter
@Setter
public class Sequence {
    @Field(label = "序列名称")
    private String sequenceName;
    @Field(label = "序列当前值")
    private Long currentVal;
    @Field(label = "序列增长值")
    private Integer incrementval;
    @Field(label = "序列类型")
    private String sequenceType;

}
