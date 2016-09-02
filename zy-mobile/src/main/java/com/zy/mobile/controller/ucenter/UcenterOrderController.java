package com.zy.mobile.controller.ucenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zy.component.OrderComponent;
import com.zy.entity.mal.Order;
import com.zy.model.Principal;
import com.zy.service.OrderService;

@RequestMapping("/u/order")
@Controller
public class UcenterOrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderComponent orderComponent;
	
	
	@RequestMapping("/in")
	public String in(Principal principal, Model model) {
		
		return "order/orderList";
	}
	
	@RequestMapping("/out")
	public String out(Principal principal, Model model) {
		
		return "order/orderList";
	}
	
	@RequestMapping("/{sn}")
	public String detail(@PathVariable String sn, Model model) {
		  Order order =  orderService.findBySn(sn);
		  
		  return "order/orderDetail";  
	}
	
	@RequestMapping(path = "/deliver", method = RequestMethod.GET)
	public String deliver(String sn, Model model) {
		
		return "order/orderDeliver";
	}
	
	@RequestMapping(path = "/deliver", method = RequestMethod.POST)
	public String deliver(String sn, Model model, Principal principal) {
		
		return "redirect:/order/" + sn;
	}
	
	@RequestMapping("/confirmDelivery")
	public String confirmDelivery(String sn, Model model, Principal principal) {
		
		return "redirect:/order/" + sn;
	}
	
}
