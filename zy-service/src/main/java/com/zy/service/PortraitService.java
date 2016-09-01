package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.usr.Portrait;
import com.zy.model.query.PortraitQueryModel;

/**
 * 用户画像操作支持
 * Created by freeman on 16/7/14.
 */

public interface PortraitService {
	
	Portrait findOne(Long id);

	Page<Portrait> findPage(PortraitQueryModel portraitQueryModel);

	Portrait findByUserId(Long userId);

	Portrait create(Portrait portrait);

	void update(Portrait portrait);

}
