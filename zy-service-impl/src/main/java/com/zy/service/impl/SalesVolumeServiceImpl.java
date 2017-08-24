package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.report.SalesVolume;
import com.zy.model.query.SalesVolumeQueryModel;
import com.zy.service.SalesVolumeService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class SalesVolumeServiceImpl implements SalesVolumeService {


	@Override
	public SalesVolume create(SalesVolume salesVolume) {
		return null;
	}

	@Override
	public void modify(SalesVolume salesVolume) {

	}

	@Override
	public SalesVolume findOne(Long salesVolumeId) {
		return null;
	}

	@Override
	public Page<SalesVolume> findPage(SalesVolumeQueryModel salesVolumeQueryModel) {
		return null;
	}
}
