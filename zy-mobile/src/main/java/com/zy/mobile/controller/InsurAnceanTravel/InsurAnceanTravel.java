package com.zy.mobile.controller.InsurAnceanTravel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Administrator on 2017/7/3.
 */
@RequestMapping("/iframe")
@Controller
public class InsurAnceanTravel {

    @RequestMapping(value = "/insurance" ,method= RequestMethod.GET)
    public String Insurance(){
        return "ucenter/report/insurance";
    }


    @RequestMapping(value = "/travel" ,method= RequestMethod.GET)
    public String travel(){
        return "ucenter/report/travel";
    }
}
