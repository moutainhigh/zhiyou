package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.report.SalesVolume;
import com.zy.mapper.SalesVolumeMapper;
import com.zy.model.query.SalesVolumeQueryModel;
import com.zy.service.SalesVolumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class SalesVolumeServiceImpl implements SalesVolumeService {

	@Autowired
	private SalesVolumeMapper salesVolumeMapper;

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
		if (salesVolumeQueryModel.getPageNumber() == null)
			salesVolumeQueryModel.setPageNumber(0);
		if (salesVolumeQueryModel.getPageSize() == null)
			salesVolumeQueryModel.setPageSize(20);
		long total = salesVolumeMapper.count(salesVolumeQueryModel);
		List<SalesVolume> data = salesVolumeMapper.findAll(salesVolumeQueryModel);
		Page<SalesVolume> page = new Page<>();
		page.setPageNumber(salesVolumeQueryModel.getPageNumber());
		page.setPageSize(salesVolumeQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public long countNumber(SalesVolumeQueryModel salesVolumeQueryModel) {
		return salesVolumeMapper.countNumber(salesVolumeQueryModel);
	}

	@Override
	public long sumQuantity(SalesVolumeQueryModel salesVolumeQueryModel) {
		return salesVolumeMapper.sumQuantity(salesVolumeQueryModel);
	}

	@Override
	public long sumAmount(SalesVolumeQueryModel salesVolumeQueryModel) {
		return salesVolumeMapper.sumAmount(salesVolumeQueryModel);
	}

	@Override
	public List<SalesVolume> findExReport(SalesVolumeQueryModel salesVolumeQueryModel) {
		return salesVolumeMapper.findAll(salesVolumeQueryModel);
	}
}
