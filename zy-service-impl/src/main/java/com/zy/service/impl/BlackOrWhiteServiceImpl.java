package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.tour.BlackOrWhite;
import com.zy.mapper.BlackOrWhiteMapper;
import com.zy.model.query.BlackOrWhiteQueryModel;
import com.zy.service.BlackOrWhiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
class BlackOrWhiteServiceImpl implements BlackOrWhiteService {

	@Autowired
	private BlackOrWhiteMapper blackOrWhiteMapper;

	@Override
	public BlackOrWhite create(@NotNull BlackOrWhite blackOrWhite) {
		validate(blackOrWhite);
		blackOrWhiteMapper.insert(blackOrWhite);
		return blackOrWhite;
	}

	@Override
	public void modify(@NotNull BlackOrWhite blackOrWhite) {
		blackOrWhiteMapper.update(blackOrWhite);
	}

	@Override
	public BlackOrWhite findOne(@NotNull Long blackOrWhiteId) {
		return blackOrWhiteMapper.findOne(blackOrWhiteId);
	}

	@Override
	public void delete(Long blackOrWhiteId) {
		blackOrWhiteMapper.delete(blackOrWhiteId);
	}

	@Override
	public BlackOrWhite findByUserId(Long userId) {
		return blackOrWhiteMapper.findByUserId(userId);
	}

	@Override
	public Page<BlackOrWhite> findPage(@NotNull BlackOrWhiteQueryModel blackOrWhiteQueryModel) {
		if (blackOrWhiteQueryModel.getPageNumber() == null)
			blackOrWhiteQueryModel.setPageNumber(0);
		if (blackOrWhiteQueryModel.getPageSize() == null)
			blackOrWhiteQueryModel.setPageSize(20);
		long total = blackOrWhiteMapper.count(blackOrWhiteQueryModel);
		List<BlackOrWhite> data = blackOrWhiteMapper.findAll(blackOrWhiteQueryModel);
		Page<BlackOrWhite> page = new Page<>();
		page.setPageNumber(blackOrWhiteQueryModel.getPageNumber());
		page.setPageSize(blackOrWhiteQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}
}
