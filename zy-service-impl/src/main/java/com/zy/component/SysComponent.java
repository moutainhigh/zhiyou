package com.zy.component;

import com.zy.entity.sys.Area;
import com.zy.mapper.AreaMapper;
import com.zy.mapper.MessageMapper;
import com.zy.model.dto.AreaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class SysComponent {

	@Autowired
	private AreaMapper areaMapper;
	
	@Autowired
	private MessageMapper messageMapper;
	
	public AreaDto findOneDto(@NotNull Long id) {
		Area area = areaMapper.findOne(id);
		if (area == null) {
			return null;
		}

		Area district = null;
		Area city = null;
		Area province = null;

		switch (area.getAreaType()) {
		case 区:
			district = area;
			city = areaMapper.findOne(district.getParentId());
			province = areaMapper.findOne(city.getParentId());
			break;
		case 市:
			city = area;
			province = areaMapper.findOne(city.getParentId());
			break;
		case 省:
			province = area;
			break;
		default:
			break;
		}

		AreaDto areaDto = new AreaDto();
		areaDto.setAreaId(id);
		if (province != null) {
			areaDto.setProvince(province.getName());
			areaDto.setProvinceId(province.getId());
		}
		if (city != null) {
			areaDto.setCity(city.getName());
			areaDto.setCityId(city.getId());
		}
		if (district != null) {
			areaDto.setDistrict(district.getName());
			areaDto.setDistrictId(district.getId());
		}
		areaDto.setAreaType(area.getAreaType());
		return areaDto;
	}
	
}
