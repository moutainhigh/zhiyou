package com.gc.service;

import java.util.List;

import com.zy.common.model.query.Page;
import com.gc.entity.sys.Message;
import com.gc.model.query.MessageQueryModel;

public interface MessageService {

	List<Message> findGroupByBatchNumber();
	
	Message create(Message message);
	
	Message findOne(Long id);
	
	void deleteByBatchNumber(String batchNumber);
	
	void delete(Long id);
	
	Page<Message> findPage(MessageQueryModel messageQueryModel);
	
	long count(MessageQueryModel messageQueryModel);
	
	void readOne(Long id);
	
	void readAll(Long userId);

	void send(Message messageForCreate, List<Long> userIds);
	
}
