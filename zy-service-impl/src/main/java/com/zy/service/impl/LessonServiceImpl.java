package com.zy.service.impl;

import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.entity.fnc.Account;
import com.zy.entity.fnc.AccountLog;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.star.Lesson;
import com.zy.entity.star.LessonUser;
import com.zy.mapper.AccountLogMapper;
import com.zy.mapper.AccountMapper;
import com.zy.mapper.LessonMapper;
import com.zy.mapper.LessonUserMapper;
import com.zy.model.query.AccountLogQueryModel;
import com.zy.model.query.LessonQueryModel;
import com.zy.model.query.LessonUserQueryModel;
import com.zy.service.AccountLogService;
import com.zy.service.LessonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.entity.fnc.AccountLog.InOut.收入;

@Service
@Validated
public class LessonServiceImpl implements LessonService {

	@Autowired
	private LessonMapper lessonMapper;

	@Autowired
	private LessonUserMapper lessonUserMapper;

	/**
	 * 查询 lesson数据
	 * @param lessonQueryModel
	 * @return
     */
	@Override
	public Page<Lesson> findPage(LessonQueryModel lessonQueryModel) {
		long total = lessonMapper.count(lessonQueryModel);
		List<Lesson> data = lessonMapper.findAll(lessonQueryModel);

		Page<Lesson> page = new Page<>();
		page.setPageNumber(lessonQueryModel.getPageNumber());
		page.setPageSize(lessonQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	/**
	 * 插入课程数据
	 * @param lesson
     */
	@Override
	public void insert(Lesson lesson) {
		lessonMapper.insert(lesson);
	}


	/**
	 * 删除 课程 并更新其他课程
	 * @param lessonId
     */
	@Override
	public void updateEdit(Long lessonId) {
		Lesson lesson = lessonMapper.findOne(lessonId);
		if (lesson!=null){
			lesson.setViewFlage(0);
			lessonMapper.update(lesson);
		}
		//将所有 parentId 是这个的更新掉
		LessonQueryModel lessonQueryModel = new LessonQueryModel();
		lessonQueryModel.setViewFlageEQ(1);
		List<Lesson> lessonList = lessonMapper.findAll(lessonQueryModel);
		lessonList=lessonList.stream().filter(v -> v.getParentAllId()!=null).collect(Collectors.toList());
		lessonList = lessonList.stream().filter(v -> v.getParentAllId().indexOf(String.valueOf(lessonId)) != -1).collect(Collectors.toList());
		for(Lesson lessons :lessonList){
			String parentAllId =lessons.getParentAllId();
			parentAllId = parentAllId.replace(String.valueOf(lessonId), "");
			if ("".equals(parentAllId)){
				parentAllId=null;
			}
			lessons.setParentAllId(parentAllId);
			lessonMapper.update(lessons);
		}


	}


	@Override
	public Lesson findOne(Long lessonId) {
		return lessonMapper.findOne(lessonId);
	}

	@Override
	public void update(Lesson lesson) {
		lessonMapper.update(lesson);
	}

	/**
	 * 查询   用户课程信息
	 * @param lessonUserQueryModel
	 * @return
     */
	@Override
	public Page<LessonUser> findPageByLessonUser(LessonUserQueryModel lessonUserQueryModel) {
		long total = lessonUserMapper.count(lessonUserQueryModel);
		List<LessonUser> data = lessonUserMapper.findAll(lessonUserQueryModel);
		Page<LessonUser> page = new Page<>();
		page.setPageNumber(lessonUserQueryModel.getPageNumber());
		page.setPageSize(lessonUserQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public LessonUser findOneByLessonUser(Long lessonUserId) {
		return lessonUserMapper.findOne(lessonUserId);
	}

	@Override
	public void createByLessonUser(LessonUser lessonUser) {
		lessonUserMapper.insert(lessonUser);
	}

	@Override
	public void deleteLessonUser(Long lessonUserId) {
		lessonUserMapper.delete(lessonUserId);
	}

	@Override
	public List<LessonUser> findHonor(Long loginUserId) {
		List<LessonUser> lessonUsers = new ArrayList<>();
		lessonUsers = lessonUserMapper.findHonor(loginUserId);
		return lessonUsers;
	}
}
