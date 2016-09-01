package com.zy.mobile.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/about")
@Controller
public class AboutController {

	final Logger logger = LoggerFactory.getLogger(AboutController.class);

	@RequestMapping
	public String index(Model model) {

		return "about";
	}

}
