package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.act.Activity;
import com.zy.entity.report.SalesVolume;
import com.zy.model.query.ActivityQueryModel;
import com.zy.model.query.SalesVolumeQueryModel;

import java.util.List;

public interface SalesVolumeService {

	SalesVolume create(SalesVolume salesVolume);

	void modify(SalesVolume salesVolume);

	SalesVolume findOne(Long salesVolumeId);

	Page<SalesVolume> findPage(SalesVolumeQueryModel salesVolumeQueryModel);
}
