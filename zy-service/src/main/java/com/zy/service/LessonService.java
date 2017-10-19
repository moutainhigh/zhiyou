package com.zy.service;


import com.zy.common.model.query.Page;
import com.zy.entity.star.Lesson;
import com.zy.entity.star.LessonUser;
import com.zy.model.query.LessonQueryModel;
import com.zy.model.query.LessonUserQueryModel;

import java.util.List;

public interface LessonService {


	Page<Lesson> findPage(LessonQueryModel lessonQueryModel);

	void insert(Lesson lesson);

	void updateEdit(Long lessonId, Long userId);

	Lesson findOne(Long lessonId);

	void update(Lesson lesson);

	Page<LessonUser> findPageByLessonUser(LessonUserQueryModel lessonUserQueryModel);

	LessonUser findOneByLessonUser(Long lessonUserId);

	void createByLessonUser(LessonUser lessonUser);

	void deleteLessonUser(Long lessonUserId);

	List<LessonUser> findHonor(Long loginUserId);

}
