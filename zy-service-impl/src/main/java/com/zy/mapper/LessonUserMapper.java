package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.star.LessonUser;
import com.zy.model.query.LessonUserQueryModel;


public interface LessonUserMapper {

	int insert(LessonUser lessonUser);

	int update(LessonUser lessonUser);

	int merge(@Param("lessonUser") LessonUser lessonUser, @Param("fields")String... fields);

	int delete(Long id);

	LessonUser findOne(Long id);

	List<LessonUser> findAll(LessonUserQueryModel lessonUserQueryModel);

	long count(LessonUserQueryModel lessonUserQueryModel);

}