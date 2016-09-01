package com.gc.service.impl;

import com.zy.common.model.query.Page;
import com.gc.entity.sys.ShortMessage;
import com.gc.mapper.ShortMessageMapper;
import com.gc.model.query.ShortMessageQueryModel;
import com.gc.service.ShortMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import java.util.List;

@Service
@Validated
public class ShortMessageServiceImpl implements ShortMessageService {

	
	@Autowired
	private ShortMessageMapper shortMessageMapper;
	

	@Override
	public Page<ShortMessage> findPage(@NotNull ShortMessageQueryModel shortMessageQueryModel) {
		long total = shortMessageMapper.count(shortMessageQueryModel);
		List<ShortMessage> data = shortMessageMapper.findAll(shortMessageQueryModel);
		Page<ShortMessage> page = new Page<>();

		page.setPageNumber(shortMessageQueryModel.getPageNumber());
		page.setPageSize(shortMessageQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public ShortMessage create(@NotNull ShortMessage shortMessage) {
		shortMessageMapper.insert(shortMessage);
		return shortMessage;
	}

}
