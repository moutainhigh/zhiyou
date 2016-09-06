package com.zy.mobile.controller;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.util.Identities;
import com.zy.component.OrderComponent;
import com.zy.component.ProductComponent;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.Order.OrderStatus;
import com.zy.entity.mal.Product;
import com.zy.entity.usr.Address;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.service.AddressService;
import com.zy.service.OrderService;
import com.zy.service.ProductService;
import com.zy.vo.OrderDetailVo;
import com.zy.vo.OrderItemVo;
import com.zy.vo.ProductListVo;

@RequestMapping("/order")
@Controller
public class OrderController {

	@Autowired
	private AddressService addressService;
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderComponent orderComponent;
	
	@Autowired
	private ProductService productService;

	@Autowired
	private ProductComponent productComponent;
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Long productId, Long quantity, Model model, Principal principal) {
		validate(productId, NOT_NULL, "product id is null!");
		Long userId = principal.getUserId();
		validate(userId, NOT_NULL, "user id is null!");
		Address address = addressService.findDefaultByUserId(userId);
		Product product = productService.findOne(productId);
		validate(product, NOT_NULL, "product id:" + productId + " is not found !");
		ProductListVo productVo = productComponent.buildListVo(product);
		model.addAttribute("product", productVo);
		model.addAttribute("quantity", quantity);
		model.addAttribute("address", address);
		return "order/orderCreate";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(HttpServletRequest request, OrderDetailVo orderDto, Long addressId, Principal principal, RedirectAttributes redirectAttributes, Model model) {
		validate(addressId, NOT_NULL, "address id is null!");
		Address address = addressService.findOne(addressId);
		validate(address, NOT_NULL, "address is not found id:" + addressId);
		List<OrderItemVo> orderItems = orderDto.getOrderItems();
		validate(orderItems, v -> v.size() > 0, "orderItems size is less than 0!");
		/*
		OrderDto newOrderDto = new OrderDto();
		BigDecimal zero = new BigDecimal("0.00");
		Date date = new Date();
		Date expireTime = DateUtils.addMinutes(date, Constants.WEIXIN_PAY_EXPIRE_IN_MINUTES);
		
		newOrderDto.setOrderItems(orderDto.getOrderItems());
		newOrderDto.setPrice(orderDto.getPrice());
		newOrderDto.setAmount(orderDto.getPrice());
		newOrderDto.setUserId(principal.getUserId());
		newOrderDto.setReceiverProvince(address.getProvince());
		newOrderDto.setReceiverCity(address.getCity());
		newOrderDto.setReceiverDistrict(address.getDistrict());
		newOrderDto.setReceiverAddress(address.getAddress());
		newOrderDto.setReceiverPhone(address.getPhone());
		newOrderDto.setReceiverRealname(address.getRealname());
		newOrderDto.setMemo(orderDto.getMemo());
		
		newOrderDto.setCreatedTime(date);
		newOrderDto.setAdjustFee(zero);
		newOrderDto.setDiscountFee(zero);
		newOrderDto.setOrderStatus(OrderStatus.待支付);
		newOrderDto.setExpireTime(expireTime);
		newOrderDto.setSn(Identities.uuid2());
		newOrderDto.setVersion(0);
		
		Order order = orderService.createByDto(newOrderDto);
		*/
		return "redirect:/ucenter/order/" + orderDto.getSn();
	}
	
}
