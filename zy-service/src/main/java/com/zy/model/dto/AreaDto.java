package com.gc.model.dto;

import com.gc.entity.sys.Area.AreaType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AreaDto implements Serializable {

	private String province;
	private String city;
	private String district;

	private Long areaId;

	private Long cityId;
	private Long districtId;
	private Long provinceId;

	private AreaType areaType;

}
