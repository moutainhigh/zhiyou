package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.report.SalesVolume;
import com.zy.model.query.SalesVolumeQueryModel;

public interface SalesVolumeService {

	SalesVolume create(SalesVolume salesVolume);

	void modify(SalesVolume salesVolume);

	SalesVolume findOne(Long salesVolumeId);

	Page<SalesVolume> findPage(SalesVolumeQueryModel salesVolumeQueryModel);

	long countNumber(SalesVolumeQueryModel salesVolumeQueryModel);

	long sumQuantity(SalesVolumeQueryModel salesVolumeQueryModel);

	long sumAmount(SalesVolumeQueryModel salesVolumeQueryModel);
}
