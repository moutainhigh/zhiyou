package com.zy.mobile.controller.ucenter;

import com.zy.component.LessonComponent;
import com.zy.entity.star.LessonUser;
import com.zy.model.Principal;
import com.zy.service.LessonService;
import com.zy.vo.LessonUserDetailVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Xuwq
 * Date: 2017/7/25.
 */
@RequestMapping("/u/honor")
@Controller
public class UcenterHonorController {
    Logger logger = LoggerFactory.getLogger(UcenterHonorController.class);

    @Autowired
    private LessonService lessonService;

    @Autowired
    private LessonComponent lessonComponent;

    /**
     * 我的荣誉
     * @param principal
     * @param model
     * @return
     */
    @RequestMapping
    public String detail(Principal principal, Model model) {
        List<Long> idList = new ArrayList<>();
        LessonUserDetailVo lessonUserDetailVo = new LessonUserDetailVo();
        Long loginUserId = principal.getUserId();
        List<LessonUser>  lessonUsers = lessonService.findHonor(loginUserId);
        if (lessonUsers != null){
            for (LessonUser lessonUser: lessonUsers) {
                idList.add(lessonUser.getLessonId());
            }
            lessonUserDetailVo = lessonComponent.buildLessonUserDetailVo(idList,loginUserId);
        }
        model.addAttribute("lessonUserDetailVo",lessonUserDetailVo);
        return "ucenter/honor/honor";
    }
}
