package com.zy.admin.controller.fnc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zy.service.TransferService;

@RequestMapping("/transfer")
@Controller
public class TransferController {

	@Autowired
	private TransferService transferService;
}
