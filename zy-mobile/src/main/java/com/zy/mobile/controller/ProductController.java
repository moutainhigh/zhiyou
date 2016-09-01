package com.zy.mobile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gc.entity.mal.Product;
import com.gc.service.ProductService;

@RequestMapping("/product")
@Controller
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@RequestMapping
	public String list(boolean isFirst, Model model) {
		
		if(isFirst) {
			return "product/productListAgent";
		} else {
			return "product/productList";
		}
	}


	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable Long id, Model model) {
		Product product = productService.findOne(id);
		
		return "product/productDetail";
	}

}
