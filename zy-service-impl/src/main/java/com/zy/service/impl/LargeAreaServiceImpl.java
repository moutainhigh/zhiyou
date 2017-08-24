package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.report.LargeArea;
import com.zy.model.query.LargeAreaQueryModel;
import com.zy.service.LargeAreaService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class LargeAreaServiceImpl implements LargeAreaService {


	@Override
	public LargeArea create(LargeArea largeArea) {
		return null;
	}

	@Override
	public LargeArea findOne(Long largeAreaId) {
		return null;
	}

	@Override
	public Page<LargeArea> findPage(LargeAreaQueryModel largeAreaQueryModel) {
		return null;
	}
}
