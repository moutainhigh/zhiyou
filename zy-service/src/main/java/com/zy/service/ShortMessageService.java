package com.gc.service;

import com.zy.common.model.query.Page;
import com.gc.entity.sys.ShortMessage;
import com.gc.model.query.ShortMessageQueryModel;

public interface ShortMessageService {
	
	ShortMessage create(ShortMessage shortMessage);
	
	Page<ShortMessage> findPage(ShortMessageQueryModel shortMessageQueryModel);
	
}
