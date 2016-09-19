package com.zy.admin.controller.fnc;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.query.Page;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.entity.fnc.Bank;
import com.zy.model.Constants;
import com.zy.model.query.BankQueryModel;
import com.zy.service.BankService;

@RequestMapping("/bank")
@Controller
public class BankController {

	@Autowired
	private BankService bankService;
	
	@RequiresPermissions("bank:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "fnc/bankList";
	}
	
	@RequiresPermissions("bank:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<Bank> list(BankQueryModel bankQueryModel) {
		Page<Bank> page = bankService.findPage(bankQueryModel);
		return new Grid<Bank>(page);
	}
	
	@RequiresPermissions("bank:edit")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create() {
		
		return "fnc/bankCreate";
	}
	
	@RequiresPermissions("bank:edit")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(Bank bank, RedirectAttributes redirectAttributes) {
		try {
			bankService.create(bank);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("保存成功"));
			return "redirect:/bank";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return "redirect:/bank/create";
		}
	}
	
	@RequiresPermissions("bank:edit")
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(@RequestParam Long id, Model model) {
		Bank bank = bankService.findOne(id);
		validate(bank, NOT_NULL, "bank id" + id + " not found");
		model.addAttribute("bank", bank);
		return "fnc/bankUpdate";
	}
	
	@RequiresPermissions("bank:edit")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String udpate(Bank bank, RedirectAttributes redirectAttributes) {
		try {
			bankService.update(bank);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("保存成功"));
			return "redirect:/bank";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return "redirect:/bank/update";
		}
	}
	
	@RequiresPermissions("bank:edit")
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(@RequestParam Long id, RedirectAttributes redirectAttributes) {
		try {
			bankService.delete(id);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("删除成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
		}
		return "redirect:/bank";
	}
}
