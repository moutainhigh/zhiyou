package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.sys.Message;
import com.zy.model.query.MessageQueryModel;

import java.util.Date;

public interface MessageMapper {

	int insert(Message message);

	int update(Message message);

	int merge(@Param("message") Message message, @Param("fields")String... fields);

	int delete(Long id);

	Message findOne(Long id);

	List<Message> findAll(MessageQueryModel messageQueryModel);

	long count(MessageQueryModel messageQueryModel);

	int deleteByBatchNumber(String batchNumber);

	int readAll(@Param("userId") Long userId, @Param("readTime") Date readTime);

	List<Message> findGroupByBatchNumber();

}