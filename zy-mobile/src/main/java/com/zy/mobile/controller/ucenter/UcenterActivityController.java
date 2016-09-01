package com.zy.mobile.controller.ucenter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gc.model.Principal;

@RequestMapping("/u/activity")
@Controller
public class UcenterActivityController {

	@RequestMapping()
	public String list(Principal principal, Model model) {
		//TODO
		return "ucenter/activity/activityList";
	}

}
