package com.zy.admin.controller.fnc;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zy.admin.model.AdminPrincipal;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.PaymentComponent;
import com.zy.entity.fnc.Payment;
import com.zy.entity.fnc.Payment.PaymentStatus;
import com.zy.model.query.PaymentQueryModel;
import com.zy.service.PaymentService;
import com.zy.vo.PaymentAdminVo;

@RequestMapping("/payment")
@Controller
public class PaymentController {

	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private PaymentComponent paymentComponent;
	
	@RequiresPermissions("payment:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("paymentStatuses", PaymentStatus.values());
		return "fnc/paymentList";
	}
	
	@RequiresPermissions("payment:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<PaymentAdminVo> list(PaymentQueryModel paymentQueryModel) {
		Page<Payment> page = paymentService.findPage(paymentQueryModel);
		return new Grid<PaymentAdminVo>(PageBuilder.copyAndConvert(page, paymentComponent::buildAdminVo));
	}
	
	@RequiresPermissions("payment:confirmPaid")
	@RequestMapping(value = "/confirmPaid", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> confirmPaid(@RequestParam(required = true) Long id, @RequestParam(required = true) boolean isSuccess, String remark) {
		AdminPrincipal principal = (AdminPrincipal)SecurityUtils.getSubject().getPrincipal();
		Payment payment = paymentService.findOne(id);
		validate(payment, NOT_NULL, "payment id not found,id = " + id);
		if(isSuccess){
			paymentService.offlineSuccess(id, principal.getUserId(), remark);
		} else {
			paymentService.offlineFailure(id, principal.getUserId(), remark);
		}
		return ResultBuilder.ok("支付单[" + payment.getTitle() + "]确认支付成功");
	}
}
