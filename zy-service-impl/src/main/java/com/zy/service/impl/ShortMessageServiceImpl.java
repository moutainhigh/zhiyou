package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.entity.sys.ShortMessage;
import com.zy.mapper.ShortMessageMapper;
import com.zy.model.query.ShortMessageQueryModel;
import com.zy.service.ShortMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.validate;

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
		shortMessage.setCreatedTime(new Date());
		validate(shortMessage);
		shortMessageMapper.insert(shortMessage);
		return shortMessage;
	}

}
