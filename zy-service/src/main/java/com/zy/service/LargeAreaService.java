package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.act.Activity;
import com.zy.entity.report.LargeArea;
import com.zy.model.query.ActivityQueryModel;
import com.zy.model.query.LargeAreaQueryModel;

import java.util.List;

public interface LargeAreaService {

	LargeArea create(LargeArea largeArea);


	LargeArea findOne(Long largeAreaId);

	Page<LargeArea> findPage(LargeAreaQueryModel largeAreaQueryModel);

}
