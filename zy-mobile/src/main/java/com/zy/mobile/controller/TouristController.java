package com.zy.mobile.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/* 游客 */
@RequestMapping("/tourist")
@Controller
public class TouristController {

	final Logger logger = LoggerFactory.getLogger(TouristController.class);
	
	@RequestMapping
	public String index(Model model) {
		
		return "tourist";
	}

}
