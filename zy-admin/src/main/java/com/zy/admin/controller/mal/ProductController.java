package com.zy.admin.controller.mal;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.ProductComponent;
import com.zy.entity.mal.Product;
import com.zy.model.Constants;
import com.zy.model.query.ProductQueryModel;
import com.zy.service.ProductService;
import com.zy.vo.ProductAdminVo;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@RequestMapping("/product")
@Controller
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductComponent productComponent;
	
	@RequiresPermissions("product:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "mal/productList";
	}
	
	@RequiresPermissions("product:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<ProductAdminVo> listAjax(ProductQueryModel productQueryModel) {
		Page<Product> page = productService.findPage(productQueryModel);
		return new Grid<ProductAdminVo>(PageBuilder.copyAndConvert(page, productComponent::buildAdminVo));
	}
	
	@RequiresPermissions("product:edit")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create() {
		
		return "mal/productCreate";
	}
	
	@RequiresPermissions("product:edit")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(Product product, Model mode, RedirectAttributes redirectAttributes) {
		try {
			productService.create(product);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("保存成功"));
			return "redirect:/product";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return "redirect:/product/create";
		}
	}
	
	@RequiresPermissions("product:edit")
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(@RequestParam Long id, Model model) {
		Product product = productService.findOne(id);
		validate(product, NOT_NULL, "product not fund, id = " + id);
		model.addAttribute("product", productComponent.buildAdminVo(product));
		
		String detail = StringEscapeUtils.unescapeHtml4(product.getDetail());
		if (StringUtils.isNotBlank(detail)) {
			detail = detail.replace("\r\n", "").replace("'", "\\'");
		}
		model.addAttribute("detail", detail);
		return "mal/productUpdate";
	}
	
	@RequiresPermissions("product:edit")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Product product, RedirectAttributes redirectAttributes) {
		try {
			productService.modify(product);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("保存成功"));
			return "redirect:/product";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return "redirect:/product/update?id=" + product.getId();
		}
	}
	
	@RequiresPermissions("product:modifyPrice")
	@RequestMapping(value = "/modifyPrice", method = RequestMethod.GET)
	public String modifyPrice(@RequestParam Long id, Model model) {
		Product product = productService.findOne(id);
		validate(product, NOT_NULL, "product not fund, id = " + id);
		model.addAttribute("product", productComponent.buildAdminVo(product));
		return "mal/productModify";
	}
	
	@RequiresPermissions("product:modifyPrice")
	@RequestMapping(value = "/modifyPrice", method = RequestMethod.POST)
	public String modifyPrice(Product product, RedirectAttributes redirectAttributes) {
		try {
			productService.modifyPrice(product);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("保存成功"));
			return "redirect:/product";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return "redirect:/product/modifyPrice?id=" + product.getId();
		}
	}
	
	@RequiresPermissions("product:on")
	@RequestMapping(value = "/on")
	public String on(@RequestParam Long id, RedirectAttributes redirectAttributes, @RequestParam boolean isOn) {
		
		Product product = productService.findOne(id);
		validate(product, NOT_NULL, "product is null");
		if(product.getProductPriceType() == null) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error("请先编辑商品价格"));
			return "redirect:/product";
		}
		
		String released = isOn ? "上架" : "下架";
		try {
			productService.on(id, isOn);
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("商品" + released + "成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error("商品" + released + "失败, 原因" + e.getMessage()));
		}
		return "redirect:/product";
	}
	
}
