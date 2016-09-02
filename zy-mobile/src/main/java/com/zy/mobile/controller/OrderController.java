package com.zy.mobile.controller;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zy.common.model.query.Page;
import com.zy.component.OrderComponent;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.OrderItem;
import com.zy.entity.mal.Product;
import com.zy.entity.usr.Address;
import com.zy.model.Principal;
import com.zy.model.query.OrderQueryModel;
import com.zy.service.AddressService;
import com.zy.service.OrderItemService;
import com.zy.service.OrderService;
import com.zy.service.ProductService;

@RequestMapping("/order")
@Controller
public class OrderController {

	@Autowired
	private AddressService addressService;
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderItemService orderItemService;
	
	@Autowired
	private OrderComponent orderComponent;
	
	@Autowired
	private ProductService productService;

	//@Autowired
	//private ProductComponent productComponent;
	
	@RequestMapping("/create")
	public String create(Long productId, Long quantity, Model model, Principal principal) {
		//validate(productId, NOT_NULL, "product id is null!");
		Long userId = principal.getUserId();
		validate(userId, NOT_NULL, "user id is null!");
		Address address = addressService.findDefaultByUserId(userId);
		Product product = productService.findOne(productId);
		validate(product, NOT_NULL, "product id:" + productId + " is not found !");
		//ProductListVo productVo = productComponent.buildProductVo(product);
		//model.addAttribute("product", productVo);
		model.addAttribute("quantity", quantity);
		model.addAttribute("address", address);
		return "order/orderCreate";
	}
	
	@RequestMapping("/in")
	public String in(Principal principal, Model model) {
		
		OrderQueryModel orderQueryModel = new OrderQueryModel();
		orderQueryModel.setSellerIdEQ(principal.getUserId());
		orderQueryModel.setPageNumber(0);
		orderQueryModel.setPageSize(6);
		Page<Order> page = orderService.findPage(orderQueryModel);
		List<Order> list = page.getData();
		if(!list.isEmpty()) {

			
			/*Long[] productIds = list.stream().map(v -> v.getProductId()).toArray(Long[]::new);
			ProductQueryModel productQueryModel = new ProductQueryModel();
			productQueryModel.setIdIN(productIds);
			List<Product> products = productService.findAll(productQueryModel);
			
			model.addAttribute("page", PageBuilder.copyAndConvert(page, orderComponent.buildListVo(page.getData(), products)));
			model.addAttribute("timeLT", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));*/
		}
		
		return "order/orderList";
	}
	
	@RequestMapping("/out")
	public String out(Principal principal, Model model) {
		
		OrderQueryModel orderQueryModel = new OrderQueryModel();
		orderQueryModel.setSellerIdEQ(principal.getUserId());
		orderQueryModel.setUserIdEQ(principal.getUserId());
		orderQueryModel.setPageSize(6);
		Page<Order> page = orderService.findPage(orderQueryModel);
		List<Order> list = page.getData();
		if(!list.isEmpty()) {
			/*
			Long[] productIds = list.stream().map(v -> v.getProductId()).toArray(Long[]::new);
			ProductQueryModel productQueryModel = new ProductQueryModel();
			productQueryModel.setIdIN(productIds);
			List<Product> products = productService.findAll(productQueryModel);
			
			model.addAttribute("page", PageBuilder.copyAndConvert(page, orderComponent.buildListVo(page.getData(), products)));
			model.addAttribute("timeLT", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			*/
		}
		
		return "order/orderList";
	}
	
	@RequestMapping("/{id}")
	public String detail(@PathVariable String sn, Model model) {
		  Order order =  orderService.findBySn(sn);
		  //validate(order, NOT_NULL, "point order id" + id + " not found");
		  //model.addAttribute("order", orderComponent.buildVo(order, productService.findOne(order.getProductId())));
		  return "order/orderDetail";  
	}
	
	@RequestMapping("/deliver")
	public String deliver(String sn, Model model, Principal principal) {
		
		return "order/orderDeliver";
	}
	
	@RequestMapping("/confirmDelivery")
	public String confirmDelivery(String sn, Model model, Principal principal) {
		
		return "order/order";
	}
	
}
