package com.zy.mobile.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/discovery")
@Controller
public class DiscoveryController {

	@RequestMapping
	public String index(Model model) {
		//TODO
		return "discovery/index";
	}

}
