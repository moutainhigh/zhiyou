package com.zy.admin.controller.rpt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zy.component.LocalCacheComponent;

@Controller
@RequestMapping("/report/order")
public class OrderReportController {

	@Autowired
	private LocalCacheComponent localCacheComponent;
	
	
	
}
