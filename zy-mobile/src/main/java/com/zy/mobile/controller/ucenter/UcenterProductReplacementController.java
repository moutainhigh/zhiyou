package com.zy.mobile.controller.ucenter;


import com.zy.common.exception.UnauthorizedException;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.ProductReplacementComponent;
import com.zy.entity.mal.ProductReplacement;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.query.ProductReplacementQueryModel;
import com.zy.service.ProductReplacementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@RequestMapping("/u/productReplacement")
@Controller
@Slf4j
public class UcenterProductReplacementController {

	@Autowired
	private ProductReplacementService productReplacementService;

	@Autowired
	private ProductReplacementComponent productReplacementComponent;

	@RequestMapping()
	public String list(Principal principal, Model model) {
		List<ProductReplacement> all = productReplacementService.findAll(ProductReplacementQueryModel
				.builder().userIdEQ(principal.getUserId()).build());
		model.addAttribute("productReplacements", all.stream().map(productReplacementComponent::buildListVo).collect(Collectors.toList()));
		return "ucenter/order/productReplacementList";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create() {

		return "ucenter/order/productReplacementCreate";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(Principal principal, ProductReplacement productReplacement) {
		productReplacement.setUserId(principal.getUserId());
		try{
			productReplacement = productReplacementService.create(productReplacement);
			System.out.println(productReplacement.getId());
		} catch (Exception e) {
			log.error("换货错误", e);
		}
		return "redirect:/u/productReplacement";
	}

	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public String detail(@RequestParam Long id, Model model) {
		ProductReplacement one = productReplacementService.findOne(id);
		validate(one, NOT_NULL, "product replacement id " + id + " not fount");

		model.addAttribute("productReplacement", productReplacementComponent.buildDetailVo(one));
		return "ucenter/order/productReplacementDetail";
	}

	@RequestMapping("/confirmDelivery")
	public String confirmDelivery(Long id, RedirectAttributes redirectAttributes, Principal principal) {

		ProductReplacement productReplacement = productReplacementService.findOne(id);
		validate(productReplacement, NOT_NULL, "product replacement id" + id + " not found");
		if (!principal.getUserId().equals(productReplacement.getUserId())) {
			throw new UnauthorizedException("权限不足");
		}

		try {
			productReplacementService.receive(id);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("确认收货成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
		}

		return "redirect:/u/productReplacement/detail?id=" + id;
	}
}
