package com.gc.service.impl;

import com.zy.common.model.query.Page;
import com.gc.entity.cms.Banner;
import com.gc.mapper.BannerMapper;
import com.gc.model.query.BannerQueryModel;
import com.gc.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class BannerServiceImpl implements BannerService {

	@Autowired
	private BannerMapper bannerMapper;

	@Override
	public void delete(@NotNull Long id) {
		bannerMapper.delete(id);
	}

	@Override
	public Banner findOne(@NotNull Long id) {
		return bannerMapper.findOne(id);
	}

	@Override
	public Banner create(@NotNull Banner banner) {
		banner.setIsReleased(true);
		validate(banner);
		bannerMapper.insert(banner);
		return banner;
	}

	@Override
	public void modify(@NotNull Banner banner) {
		bannerMapper.merge(banner, "title", "url", "image", "isOpenBlank", "orderNumber");
	}

	@Override
	public Page<Banner> findPage(@NotNull BannerQueryModel bannerQueryModel) {
		if (bannerQueryModel.getPageNumber() == null)
			bannerQueryModel.setPageNumber(0);
		if (bannerQueryModel.getPageSize() == null)
			bannerQueryModel.setPageSize(20);
		long total = bannerMapper.count(bannerQueryModel);
		List<Banner> data = bannerMapper.findAll(bannerQueryModel);
		Page<Banner> page = new Page<>();
		page.setPageNumber(bannerQueryModel.getPageNumber());
		page.setPageSize(bannerQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public List<Banner> findAll(@NotNull BannerQueryModel bannerQueryModel) {
		return bannerMapper.findAll(bannerQueryModel);
	}

	@Override
	public void release(@NotNull Long id, boolean isReleased) {
		Banner bannerForMerage = new Banner();
		bannerForMerage.setId(id);
		bannerForMerage.setIsReleased(isReleased);
		bannerMapper.merge(bannerForMerage, "isReleased");
	}

}
