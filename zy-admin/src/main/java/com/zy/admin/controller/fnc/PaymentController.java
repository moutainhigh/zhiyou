package com.zy.admin.controller.fnc;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.query.Page;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.gc.entity.fnc.Payment;
import com.gc.model.query.PaymentQueryModel;
import com.gc.service.PaymentService;

@RequestMapping("/payment")
@Controller
public class PaymentController {

	@Autowired
	private PaymentService paymentService;
	
	@RequiresPermissions("order:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "fnc/paymentList";
	}
	
	@RequiresPermissions("order:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<Payment> list(PaymentQueryModel paymentQueryModel) {
		Page<Payment> page = paymentService.findPage(paymentQueryModel);
		return new Grid<Payment>(page);
	}
	
	@RequiresPermissions("order:confirmPaid")
	@RequestMapping("/confirmPaid/{id}")
	public String confirmPaid(@PathVariable Long id, RedirectAttributes redirectAttributes, String queryString) {
		Payment payment = paymentService.findOne(id);
		validate(payment, NOT_NULL, "payment id not found,id = " + id);
		
		//TODO
		//paymentService.paySuccess(payment.getSn(), null);
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("支付单id=[" + payment.getTitle() + "]确认支付成功"));
		return "redirect:/payment?" + StringEscapeUtils.unescapeHtml4(queryString);
	}
}
