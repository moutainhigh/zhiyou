package com.zy.mobile.controller.ucenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zy.component.UserComponent;
import com.zy.model.Principal;
import com.zy.service.UserService;

@RequestMapping("/u/team")
@Controller
public class UcenterTeamController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private UserComponent userComponent;

	@RequestMapping
	public String agent(Principal principal, Model model) {

		return "ucenter/team/userList";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable Long id){
		
		return "ucenter/team/userDetail";
	}
	
}
