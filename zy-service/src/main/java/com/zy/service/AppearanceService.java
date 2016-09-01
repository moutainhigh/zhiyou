package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.usr.Appearance;
import com.zy.model.query.AppearanceQueryModel;

public interface AppearanceService {

	Appearance findOne(Long id);

	Page<Appearance> findPage(AppearanceQueryModel appearanceQueryModel);

	Appearance findByUserId(Long userId);

	void confirm(Long id, boolean isSuccess, String confirmRemark, Integer score);

	Appearance create(Appearance appearance);

	void update(Appearance appearance);

}
