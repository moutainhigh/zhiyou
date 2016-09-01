package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.sys.ShortMessage;
import com.zy.model.query.ShortMessageQueryModel;


public interface ShortMessageMapper {

	int insert(ShortMessage shortMessage);

	int update(ShortMessage shortMessage);

	int merge(@Param("shortMessage") ShortMessage shortMessage, @Param("fields")String... fields);

	int delete(Long id);

	ShortMessage findOne(Long id);

	List<ShortMessage> findAll(ShortMessageQueryModel shortMessageQueryModel);

	long count(ShortMessageQueryModel shortMessageQueryModel);

}