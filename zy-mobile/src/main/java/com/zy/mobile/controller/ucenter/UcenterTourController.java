package com.zy.mobile.controller.ucenter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Administrator on 2017/7/5.
 */
@RequestMapping("/tour")
@Controller
public class UcenterTourController {

    @RequestMapping
    public String tourList(){
        return "ucenter/tour/tourList";
    }
}
