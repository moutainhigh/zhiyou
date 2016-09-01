package com.zy.admin.controller.mal;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.query.Page;
import com.zy.common.model.ui.Grid;
import com.zy.entity.mal.Product;
import com.zy.model.query.ProductQueryModel;
import com.zy.service.ProductService;

@RequestMapping("/product")
@Controller
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "mal/productList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<Product> listAjax(ProductQueryModel productQueryModel) {
		Page<Product> page = productService.findPage(productQueryModel);
		return new Grid<Product>(page);
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create() {
		return "mal/productCreate";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(Product product, Model mode, RedirectAttributes redirectAttributes) {
		productService.create(product);
		redirectAttributes.addFlashAttribute("保存成功");
		return "redirect:/product";
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String update(@PathVariable Long id, Model model) {
		Product product = productService.findOne(id);
		validate(product, NOT_NULL, "product not fund, id = " + id);
		model.addAttribute("product", product);
		
		String detail = StringEscapeUtils.unescapeHtml4(product.getDetail());
		if (StringUtils.isNotBlank(detail)) {
			detail = detail.replace("\r\n", "").replace("'", "\\'");
		}
		model.addAttribute("detail", detail);
		return "mal/productUpdate";
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Product product, String mainImage, RedirectAttributes redirectAttributes, Model model) {
		Long id = product.getId();
		validate(id, NOT_NULL, "id为空");
		Product pproduct = productService.findOne(id);
		validate(pproduct, NOT_NULL, "product not found, id = " + id);
		
		productService.modify(product);
		
		return "redirect:/product";
	}
	
	@RequestMapping("/on")
	@ResponseBody
	public boolean isOn(Long id, Boolean isOn) {
		validate(id, NOT_NULL, "id is null");
		validate(isOn, NOT_NULL, "isOn is null");
		
		Product product = productService.findOne(id);
		validate(product, NOT_NULL, "product is null");
		
		productService.on(id, isOn);
		return true;
	}

	
}
