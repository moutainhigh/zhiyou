package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.usr.Tag;


public interface TagMapper {

	int insert(Tag tag);

	int update(Tag tag);

	int merge(@Param("tag") Tag tag, @Param("fields")String... fields);

	int delete(Long id);

	Tag findOne(Long id);

	List<Tag> findAll();

}