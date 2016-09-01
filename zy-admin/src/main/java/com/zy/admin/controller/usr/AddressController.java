package com.zy.admin.controller.usr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zy.common.model.query.Page;
import com.zy.common.model.ui.Grid;
import com.gc.entity.usr.Address;
import com.gc.model.query.AddressQueryModel;
import com.gc.service.AddressService;

@RequestMapping("/address")
@Controller
public class AddressController {

	@Autowired
	private AddressService addressService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "usr/addressList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<Address> list(AddressQueryModel addressQueryModel) {
		Page<Address> page = addressService.findPage(addressQueryModel);
		return new Grid<Address>(page);
	}
}
