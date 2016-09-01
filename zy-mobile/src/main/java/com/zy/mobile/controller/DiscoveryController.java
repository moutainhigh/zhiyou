package com.zy.mobile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zy.Config;
import com.zy.service.AccountService;

@RequestMapping("/discovery")
@Controller
public class DiscoveryController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private Config config;

	@RequestMapping
	public String index(Model model) {
		//TODO
		return "discovery/index";
	}

}
