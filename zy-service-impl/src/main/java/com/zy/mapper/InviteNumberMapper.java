package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.sys.InviteNumber;
import com.zy.model.query.InviteNumberQueryModel;


public interface InviteNumberMapper {

	int insert(InviteNumber inviteNumber);

	int update(InviteNumber inviteNumber);

	int merge(@Param("inviteNumber") InviteNumber inviteNumber, @Param("fields")String... fields);

	int delete(Long id);

	InviteNumber findOne(Long id);

	List<InviteNumber> findAll(InviteNumberQueryModel inviteNumberQueryModel);

	long count(InviteNumberQueryModel inviteNumberQueryModel);

}