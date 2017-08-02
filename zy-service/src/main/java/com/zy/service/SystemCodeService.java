package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.sys.SystemCode;
import com.zy.model.query.SystemCodeQueryModel;

public interface SystemCodeService {

	SystemCode create(SystemCode systemCode);

	void update(SystemCode systemCode);

	SystemCode findOne(Long systemCodeId);

	void delete(Long systemCodeId);

	SystemCode findByTypeAndName(String systemType , String systemName);

	Page<SystemCode> findPage(SystemCodeQueryModel systemCodeQueryModel);
}
