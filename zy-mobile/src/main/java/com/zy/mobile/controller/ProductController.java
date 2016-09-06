package com.zy.mobile.controller;

import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.common.util.ValidateUtils.NOT_NULL;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zy.component.ProductComponent;
import com.zy.entity.mal.Product;
import com.zy.model.query.ProductQueryModel;
import com.zy.service.ProductService;

@RequestMapping("/product")
@Controller
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductComponent productComponent;
	
	@RequestMapping
	public String list(boolean isFirst, Model model) {
		
		ProductQueryModel productQueryModel = new ProductQueryModel();
		productQueryModel.setIsOnEQ(true);
		List<Product> products = productService.findAll(productQueryModel);
		model.addAttribute("products", products.stream().map(v -> {
			return productComponent.buildListVo(v);
		}).collect(Collectors.toList()));
		if(isFirst) {
			return "product/productListAgent";
		} else {
			return "product/productList";
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable Long id, Model model) {
		Product product = productService.findOne(id);
		validate(product, NOT_NULL, "product id" + id + " not found");
		validate(product.getIsOn(), v -> true, "product is not on");
		model.addAttribute("product", productComponent.buildDetailVo(product));
		return "product/productDetail";
	}

}
