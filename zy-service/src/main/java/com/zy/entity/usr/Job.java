package com.zy.entity.usr;

import io.gd.generator.annotation.Field;
import io.gd.generator.annotation.Type;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "usr_job")
@Getter
@Setter
@Type(label = "职位")
public class Job implements Serializable {

	@Id
	@Field(label = "id")
	private Long id;

	@NotBlank
	@Column(length = 60, unique = true)
	@Field(label = "职位名")
	private String jobName;

}
