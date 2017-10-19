package com.zy.admin.controller.star;

import com.zy.admin.model.AdminPrincipal;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.LessonComponent;
import com.zy.entity.star.Lesson;
import com.zy.entity.star.LessonUser;
import com.zy.entity.usr.User;
import com.zy.model.Principal;
import com.zy.model.query.LessonQueryModel;
import com.zy.model.query.LessonUserQueryModel;
import com.zy.service.LessonService;
import com.zy.service.UserService;
import com.zy.vo.LessonAdminVo;
import com.zy.vo.LessonUserAdminVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by it001 on 2017/7/21.
 */
@RequestMapping("/lesson")
@Controller
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private LessonComponent lessonComponent;

    @Autowired
    private UserService userService;

    /**
     * 跳转到查询所有课程页面 查询
     * @param model
     * @return
     */
    @RequiresPermissions("lesson:view")
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        return "star/lessionList";
    }

    /**
     * 获取 所有课程
     * @param lessonQueryModel
     * @return
     */
    @RequiresPermissions("lesson:view")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Grid<LessonAdminVo> list(LessonQueryModel lessonQueryModel) {
        if(lessonQueryModel.getPageNumber() == null){
            lessonQueryModel.setPageNumber(0);
        }
        if(lessonQueryModel.getPageSize() == null) {
            lessonQueryModel.setPageSize(20);
        }
        lessonQueryModel.setViewFlageEQ(1);
        Page<Lesson> page = lessonService.findPage(lessonQueryModel);
        Page<LessonAdminVo> voPage = PageBuilder.copyAndConvert(page, lessonComponent::buildAdminVo);
        return new Grid<>(voPage);
    }

    /**
     * 跳转到  课程添加页面
     * @param model
     * @return
     */
    @RequiresPermissions("lesson:edit")
    @RequestMapping( value = "/tocreate",method = RequestMethod.GET)
    public String tocreate(Model model){
     return "star/lessionCreate";
    }

    /**
     * 跳转到课程选择页面
     * @param model
     * @return
     */
    @RequiresPermissions("lesson:edit")
    @RequestMapping( value = "/selectLesson",method = RequestMethod.GET)
    public String selectLesson(Model model){
        return "star/selectLesson";
    }

    /**
     * 添加课程
     * @param lesson
     * @param adminPrincipal
     * @return
     */
    @RequiresPermissions("lesson:edit")
    @RequestMapping( value = "/create",method = RequestMethod.POST)
    public String create(Lesson lesson, AdminPrincipal adminPrincipal){
        lesson.setCreateById(adminPrincipal.getUserId());
        lesson.setCreateDate(new Date());
        lesson.setViewFlage(1);
        lesson.setLessonLevel(1);
        lessonService.insert(lesson);
        return "redirect:/lesson";
    }


    /**
     * 删除课程逻辑
     * @param lessonId
     * @return
     */
    @RequiresPermissions("lesson:edit")
    @RequestMapping( value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public boolean delete(Long lessonId, AdminPrincipal adminPrincipal){
        lessonService.updateEdit(lessonId, adminPrincipal.getUserId());
        return true;
    }

    /**
     * 更新课程
     *
     */
    @RequestMapping( value = "/update",method = RequestMethod.GET)
    public String update(Long lessonId,Model model){
        Lesson lesson = lessonService.findOne(lessonId);
        model.addAttribute("lessionVo",lessonComponent.buildAdminVo(lesson));
        return "star/lessionUpdate";
    }

    /**
     * 更新提交
     * @return
     */
    @RequestMapping( value = "/updateEdit",method = RequestMethod.POST)
    public String updateEdit(Lesson lesson, AdminPrincipal adminPrincipal){
        Lesson lessons = lessonService.findOne(lesson.getId());
        lessons.setTitle(lesson.getTitle());
        lessons.setParentAllId(lesson.getParentAllId());
        lessons.setUpdateId(adminPrincipal.getUserId());
        lessons.setUpdateTime(new Date());
        lessonService.update(lessons);
        return "redirect:/lesson";
    }

    //课程 用户相关逻辑

    /**
     * 课程 用户列表
     * @return
     */
    @RequiresPermissions("lessonUser:view")
    @RequestMapping( value = "/user",method = RequestMethod.GET)
    public String lessonUser(){
        return "star/lessionUserList";
    }

    /**
     * 获取 所有课程 用户
     * @param lessonUserQueryModel
     * @return
     */
    @RequiresPermissions("lessonUser:view")
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    @ResponseBody
    public Grid<LessonUserAdminVo> list(LessonUserQueryModel lessonUserQueryModel) {
        if(lessonUserQueryModel.getPageNumber() == null){
            lessonUserQueryModel.setPageNumber(0);
        }
        if(lessonUserQueryModel.getPageSize() == null) {
            lessonUserQueryModel.setPageSize(20);
        }
        Page<LessonUser> page = lessonService.findPageByLessonUser(lessonUserQueryModel);
        Page<LessonUserAdminVo> voPage = PageBuilder.copyAndConvert(page, lessonComponent::buildLessonUserAdminVo);
        return new Grid<>(voPage);
    }


    /**
     * 跳转到 添加页面
     * @return
     */
    @RequiresPermissions("lessonUser:edit")
    @RequestMapping(value = "/user/tocreate",method = RequestMethod.GET)
    public String tocreateLessonUser(Long lessonUserId,Model model){
        if (lessonUserId!=null){
            LessonUser lessonUser = lessonService.findOneByLessonUser(lessonUserId);
            model.addAttribute("phone",userService.findOne(lessonUser.getUserId()).getPhone());
        }
        return "star/lessionUserCreate";
    }

    /**
     * 查询 用户信息
     * @param phone
     * @return
     */
    @RequiresPermissions("lessonUser:edit")
    @RequestMapping(value = "/user/findUserInfo",method = RequestMethod.POST)
    @ResponseBody
    public Result<?>findUserInfo(String phone,Long lseeonId){
        Map<String,Object> map = new HashMap<>();
        User user = userService.findByPhone(phone);
        if (user!=null){
            LessonUserQueryModel lessonUserQueryModel = new LessonUserQueryModel();
            lessonUserQueryModel.setUserIdEQ(user.getId());
            lessonUserQueryModel.setLessonIdEQ(lseeonId);
            Page<LessonUser> page = lessonService.findPageByLessonUser(lessonUserQueryModel);
            if (page.getTotal()>0){
                return ResultBuilder.error("客户已参加过此课程，不能重复添加！");
            }
            map.put("phone",user.getPhone());
            map.put("name",userService.findRealName(user.getId()));
            return ResultBuilder.result(map);
        }else{
            return ResultBuilder.error("号码错误，请核对后再提交");
        }
    }

    /**
     * 添加  用户课程信息
     * @return
     */
    @RequiresPermissions("lessonUser:edit")
    @RequestMapping(value = "/user/create",method = RequestMethod.POST)
    public String createuser(String phone,Long lseeonId,  AdminPrincipal adminPrincipal){
        LessonUser lessonUser = new LessonUser();
        lessonUser.setLessonId(lseeonId);
        lessonUser.setCreateById(adminPrincipal.getUserId());
        lessonUser.setCreateDate(new Date());
        lessonUser.setLessonStatus(2);
        lessonUser.setRemark("后台手动添加");
        User user = userService.findByPhone(phone);
        lessonUser.setUserId(user.getId());
        lessonService.createByLessonUser(lessonUser);
        return "redirect:/lesson/user";
    }

    @RequiresPermissions("lessonUser:edit")
    @RequestMapping(value = "/user/delete",method = RequestMethod.POST)
    @ResponseBody
    public Boolean deleteLessonUser(Long lessonUserId){
        lessonService.deleteLessonUser(lessonUserId);
        return true;
    }
}
