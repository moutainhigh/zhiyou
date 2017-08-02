package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.star.Lesson;
import com.zy.model.query.LessonQueryModel;


public interface LessonMapper {

	int insert(Lesson lesson);

	int update(Lesson lesson);

	int merge(@Param("lesson") Lesson lesson, @Param("fields")String... fields);

	int delete(Long id);

	Lesson findOne(Long id);

	List<Lesson> findAll(LessonQueryModel lessonQueryModel);

	long count(LessonQueryModel lessonQueryModel);

}