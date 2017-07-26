package com.zy.component;

import com.zy.common.model.query.Page;
import com.zy.common.util.BeanUtils;
import com.zy.entity.star.Lesson;
import com.zy.entity.star.LessonUser;
import com.zy.model.query.LessonQueryModel;
import com.zy.service.LessonService;
import com.zy.service.UserService;
import com.zy.vo.LessonAdminVo;
import com.zy.vo.LessonUserAdminVo;
import com.zy.vo.LessonUserDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by it001 on 2017/7/21.
 */
@Component
public class LessonComponent {

    @Autowired
    private UserService userService;

    @Autowired
    private LessonService lessonService;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 封装成vo
     * @param lesson
     * @return
     */
    public LessonAdminVo buildAdminVo(Lesson lesson) {
        LessonAdminVo lessonAdminVo = new LessonAdminVo();
        BeanUtils.copyProperties(lesson, lessonAdminVo);
        lessonAdminVo.setCreateLable(userService.findRealName(lesson.getCreateById()));
        lessonAdminVo.setCreateDateLable(dateFormat.format(lesson.getCreateDate()));
        String parentAllId = lesson.getParentAllId();
        if (parentAllId!=null){
            String [] lessonid= parentAllId.split(",");
            LessonQueryModel lessonQueryModel = new LessonQueryModel();
            lessonQueryModel.setParentAllIdIN(lessonid);
            Page<Lesson> page = lessonService.findPage(lessonQueryModel);
            if(page!=null){
                StringBuffer lessonAllnameLable= new StringBuffer();
                for (Lesson lseeon :page.getData()){
                    lessonAllnameLable.append(lseeon.getTitle()).append(";");
                }
                if (lessonAllnameLable.length() > 0) {
                    lessonAllnameLable.delete(lessonAllnameLable.length() - 1, lessonAllnameLable.length());
                }
                lessonAdminVo.setLessonAllnameLable(lessonAllnameLable.toString());
            }
        }
     return lessonAdminVo;
    }


  public LessonUserAdminVo buildLessonUserAdminVo(LessonUser lessonUser){
        LessonUserAdminVo lessonUserAdminVo = new LessonUserAdminVo();
        BeanUtils.copyProperties(lessonUser, lessonUserAdminVo);
        lessonUserAdminVo.setUserName(userService.findRealName(lessonUser.getUserId()));
        Lesson lesson = lessonService.findOne(lessonUser.getLessonId());
       lessonUserAdminVo.setLessonName(lesson.getTitle());
       lessonUserAdminVo.setCreateDateLable(dateFormat.format(lessonUser.getCreateDate()));
        return lessonUserAdminVo;
    }


    /**
     * 我的荣誉
     * @param idList
     * @param loginUserId
     * @return
     */
    public LessonUserDetailVo buildLessonUserDetailVo(List<Long> idList, Long loginUserId) {
        LessonUserDetailVo lessonUserDetailVo = new LessonUserDetailVo();
        List<Lesson> lessons = new ArrayList<>();
        lessonUserDetailVo.setUserId(loginUserId);
        if (idList != null && idList.size() >0){
            for (Long id: idList) {
                Lesson lesson = lessonService.findOne(id);
                if (lesson != null){
                    lessons.add(lesson);
                }
            }
            lessonUserDetailVo.setLessons(lessons);
        }
        return lessonUserDetailVo;
    }
}
