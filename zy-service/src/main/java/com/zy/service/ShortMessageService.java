package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.sys.ShortMessage;
import com.zy.model.query.ShortMessageQueryModel;

public interface ShortMessageService {
	
	ShortMessage create(ShortMessage shortMessage);
	
	Page<ShortMessage> findPage(ShortMessageQueryModel shortMessageQueryModel);
	
}
