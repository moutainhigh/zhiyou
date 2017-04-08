package com.zy.mapper;


import com.zy.entity.cms.Matter;
import com.zy.model.query.MatterQueryModel;

import java.util.List;

public interface MatterMapper {

	long count(MatterQueryModel matterQueryModel);

	long mobileCount(MatterQueryModel matterQueryModel);

	List<Matter> findAll(MatterQueryModel matterQueryModel);

	List<Matter> mobileFindAll(MatterQueryModel matterQueryModel);

	Matter findOne(Long id);

	int update(Matter newMatter);

	int insert(Matter matter);

	void click(Long id);

	void updateMatter(Matter matter);

	void collect(Long id);

	void updateNum(Long id);
}