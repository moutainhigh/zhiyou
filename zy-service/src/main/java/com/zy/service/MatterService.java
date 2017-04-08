package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.cms.Matter;
import com.zy.model.query.MatterQueryModel;

public interface MatterService {

	Page<Matter> findPage(MatterQueryModel matterQueryModel);

	Page<Matter> mobileFindPage(MatterQueryModel matterQueryModel);

	Matter findOne(MatterQueryModel matterQueryModel);

	void updateStatus(Long id, Integer status);

	void updateMatter(Matter matter);

	void create(Matter matter);

	void click(Long id);

	void collect(Long id, Long userId);

	void uncollect(Long id, Long userId);
}
