package com.zy.mobile.controller;

import static com.gc.model.Constants.MODEL_ATTRIBUTE_RESULT;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.support.cache.CacheSupport;
import com.zy.common.util.CookieUtils;
import com.zy.common.util.Identities;
import com.gc.entity.usr.User;
import com.gc.model.Constants;
import com.gc.model.Principal;
import com.gc.model.PrincipalBuilder;
import com.gc.service.UserService;

@RequestMapping
@Controller
public class CommonController {

    final Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private UserService userService;

    @Autowired
    protected CacheSupport cacheSupport;



    @RequestMapping(value = "/checkPhone")
    @ResponseBody
    public boolean checkPhone(@RequestParam String phone) {
        return userService.findByPhone(phone) == null;
    }

    @RequestMapping("/getNotify")
    @ResponseBody
    public Result<Map<String, Object>> getNotify(Principal principal) {
        Map<String, Object> objectMap = new HashMap<>();
        return ResultBuilder.<Map<String, Object>>result(objectMap);
    }


    @RequestMapping("/superLogin/{userId}")
    public String superLogin(@PathVariable Long userId, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        User user = userService.findOne(userId);
        if (user == null) {
            redirectAttributes.addFlashAttribute(MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("用户不存在"));
            return "redirect:/login";
        }
        /* login success */
        onLoginSuccess(request, response, redirectAttributes, user);

        return "redirect:/";
    }

    public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, User user) {
        Long userId = user.getId();
        setTgt(request, response, userId);
    }

    public void setTgt(HttpServletRequest request, HttpServletResponse response, Long userId) {
        String tgt = "tgt-" + Identities.uuid();
        HttpSession session = request.getSession();
        Principal principal = PrincipalBuilder.build(userId, tgt);
        session.setAttribute(Constants.SESSION_ATTRIBUTE_PRINCIPAL, principal);
        int expire = 60 * 60 * 24 * 7;
        boolean rememberMe = true;
        if (rememberMe) {
            CookieUtils.add(response, Constants.COOKIE_NAME_MOBILE_TOKEN, tgt, expire, Constants.DOMAIN_COOKIE);
        } else {
            CookieUtils.add(response, Constants.COOKIE_NAME_MOBILE_TOKEN, tgt, -1, Constants.DOMAIN_COOKIE);
        }
        cacheSupport.set(Constants.CACHE_NAME_TGT, tgt, userId, expire);
    }


    public static class AchievementVo {
        private int id;
        private String name;
        private String desc;

        public AchievementVo(int id, String name, String desc) {
            this.id = id;
            this.name = name;
            this.desc = desc;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}

