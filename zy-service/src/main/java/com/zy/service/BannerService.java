package com.gc.service;

import com.zy.common.model.query.Page;
import com.gc.entity.cms.Banner;
import com.gc.model.query.BannerQueryModel;

import java.util.List;

public interface BannerService {

	void delete(Long id);

	Banner findOne(Long id);

	Banner create(Banner banner);

	void modify(Banner banner);

	Page<Banner> findPage(BannerQueryModel bannerQueryModel);

	List<Banner> findAll(BannerQueryModel bannerQueryModel);

	void release(Long id, boolean isReleased);
}
