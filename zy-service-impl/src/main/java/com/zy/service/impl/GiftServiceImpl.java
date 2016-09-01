package com.gc.service.impl;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.zy.common.model.query.Page;
import com.gc.entity.mal.Gift;
import com.gc.mapper.GiftMapper;
import com.gc.model.query.GiftQueryModel;
import com.gc.service.GiftService;

@Service
@Validated
public class GiftServiceImpl implements GiftService{

	@Autowired
	private GiftMapper giftMapper;

	
	@Override
	public void delete(@NotNull Long id) {
		giftMapper.delete(id);		
	}

	@Override
	public Gift findOne(@NotNull Long id) {
		return giftMapper.findOne(id);
	}

	@Override
	public void create(@NotNull Gift gift) {
		gift.setCreatedTime(new Date());
		validate(gift);
		giftMapper.insert(gift);
	}

	@Override
	public Gift modify(@NotNull Gift gift) {
		giftMapper.merge(gift, "title", "detail", "price", "marketPrice", "skuCode", "stockQuantity", "lockedCount", "orderNumber",
				"image1", "image2", "image3", "image4", "image5", "image6");
		return gift;
	}
	
	@Override
	public void on(@NotNull Long id, @NotNull Boolean isOn) {
		Gift gift = giftMapper.findOne(id);
		validate(gift, NOT_NULL, "gift is null");
		
		gift.setIsOn(isOn);
		giftMapper.update(gift);
	}

	@Override
	public Page<Gift> findPage(@NotNull GiftQueryModel giftQueryModel) {
		if(giftQueryModel.getPageNumber() == null)
			giftQueryModel.setPageNumber(0);
		if(giftQueryModel.getPageSize() == null)
			giftQueryModel.setPageSize(20);
		long total = giftMapper.count(giftQueryModel);
		List<Gift> data = giftMapper.findAll(giftQueryModel);
		Page<Gift> page = new Page<>();
		page.setPageNumber(giftQueryModel.getPageNumber());
		page.setPageSize(giftQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public List<Gift> findAll(@NotNull GiftQueryModel giftQueryModel) {
		return giftMapper.findAll(giftQueryModel);
	}

}
