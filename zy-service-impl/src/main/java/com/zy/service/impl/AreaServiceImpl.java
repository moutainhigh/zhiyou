package com.gc.service.impl;

import com.gc.component.SysComponent;
import com.gc.entity.sys.Area;
import com.gc.entity.sys.Area.AreaType;
import com.gc.mapper.AreaMapper;
import com.gc.model.dto.AreaDto;
import com.gc.model.query.AreaQueryModel;
import com.gc.service.AreaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class AreaServiceImpl implements AreaService {

	@Autowired
	private AreaMapper areaMapper;
	
	@Autowired
	private SysComponent sysComponent;

	@Override
	public List<Area> findAllOrderByOrderNumberAsc() {
		return areaMapper.findAllOrderByOrderNumberAsc();
	}

	@Override
	public void init() {
		List<Area> areas = areaMapper.findAllOrderByCodeAsc();
		if (!areas.isEmpty() && areas.get(0).getAreaType() == null) {
			Map<Integer, Long> codeMap = new HashMap<>();
			for (Area area : areas) {
				Integer code = area.getCode();
				codeMap.put(code, area.getId());

				/* handle order number */
				area.setOrderNumber(code);

				/* handle area type */
				AreaType areaType = null;
				if (code % 10000 == 0) {
					areaType = AreaType.省;
				} else if (code % 100 == 0) {
					areaType = AreaType.市;
				} else {
					areaType = AreaType.区;
				}
				area.setAreaType(areaType);

				/* handle parent id */
				Long parentId = null;
				if (code % 10000 == 0) {
					// ignore
				} else if (code % 100 == 0) {
					parentId = codeMap.get(code / 10000 * 10000);
				} else {
					parentId = codeMap.get(code / 100 * 100);
				}
				area.setParentId(parentId);
				areaMapper.update(area);
			}
		}

	}

	@Override
	public Area findOne(@NotNull Long id) {
		validate(id, NOT_NULL, "area id is null");
		return areaMapper.findOne(id);
	}

	@Override
	public AreaDto findOneDto(@NotNull Long id) {
		return sysComponent.findOneDto(id);
	}

	@Override
	public void create(@NotNull Area area) {
		validate(area);
		areaMapper.insert(area);
	}

	@Override
	public Area merge(@NotNull Area area, @NotNull String... fields) {
		areaMapper.merge(area, fields);
		return area;
	}

	@Override
	public List<Area> findAll(@NotNull AreaQueryModel areaQueryModel) {
		return areaMapper.findAll(areaQueryModel);
	}

}
