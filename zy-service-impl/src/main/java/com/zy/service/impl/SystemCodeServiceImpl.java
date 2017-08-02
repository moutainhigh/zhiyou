package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.sys.SystemCode;
import com.zy.mapper.SystemCodeMapper;
import com.zy.model.query.SystemCodeQueryModel;
import com.zy.service.SystemCodeService;
import org.hibernate.validator.constraints.NotBlank;
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
public class SystemCodeServiceImpl implements SystemCodeService {

	@Autowired
    private SystemCodeMapper systemCodeMapper;

	@Override
    public SystemCode create(@NotNull SystemCode systemCode) {
		systemCode.setSystemFlag(0);
		validate(systemCode);
		systemCodeMapper.insert(systemCode);
		return systemCode;
	}

	@Override
	public void update(@NotNull SystemCode systemCode) {
		checkAndFindSystemCode(systemCode.getId());
		systemCodeMapper.update(systemCode);
	}

	@Override
	public SystemCode findOne(@NotNull Long systemCodeId) {
		return checkAndFindSystemCode(systemCodeId);
	}

    @Override
    public void delete(@NotNull Long systemCodeId) {
        SystemCode systemCode = checkAndFindSystemCode(systemCodeId);
        systemCode.setSystemFlag(1);
        systemCodeMapper.update(systemCode);
    }

    @Override
	public SystemCode findByTypeAndName(@NotBlank String systemType , @NotBlank String systemName) {
        Map<String , Object> map = new HashMap<>();
        map.put("systemType" , systemType);
        map.put("systemName" , systemName);
        return systemCodeMapper.findByTypeAndName(map);
	}

	@Override
	public Page<SystemCode> findPage(SystemCodeQueryModel systemCodeQueryModel) {
		if (systemCodeQueryModel.getPageNumber() == null)
			systemCodeQueryModel.setPageNumber(0);
		if (systemCodeQueryModel.getPageSize() == null)
			systemCodeQueryModel.setPageSize(20);
		long total = systemCodeMapper.count(systemCodeQueryModel);
		List<SystemCode> data = systemCodeMapper.findAll(systemCodeQueryModel);
		Page<SystemCode> page = new Page<>();
		page.setPageNumber(systemCodeQueryModel.getPageNumber());
		page.setPageSize(systemCodeQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	private SystemCode checkAndFindSystemCode(Long systemCodeId) {
		validate(systemCodeId, NOT_NULL, "systemCode id is null");
		SystemCode systemCode = systemCodeMapper.findOne(systemCodeId);
		validate(systemCode, NOT_NULL, "systemCode id " + systemCodeId + " is not found");
		return systemCode;
	}
}
