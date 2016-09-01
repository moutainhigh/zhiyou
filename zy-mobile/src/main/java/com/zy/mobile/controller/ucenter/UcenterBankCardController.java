package com.zy.mobile.controller.ucenter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.exception.UnauthorizedException;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.entity.fnc.BankCard;
import com.zy.model.Principal;
import com.zy.service.BankCardService;

@RequestMapping("/u/bankCard")
@Controller
public class UcenterBankCardController {
Logger logger = LoggerFactory.getLogger(UcenterBankCardController.class);
	
	@Autowired
	private BankCardService bankCardService;
	
	@RequestMapping
	public String list(Principal principal, Model model, Integer pageNumber) {
		List<BankCard> list = bankCardService.findByUserId(principal.getUserId());
		model.addAttribute("list", list);
		return "ucenter/user/bankCardList";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Principal principal) {
		//TODO
		return "ucenter/user/bankCardCreate";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(BankCard bankCard, Model model, Principal principal, RedirectAttributes redirectAttributes) {
		
		try {
			bankCard.setUserId(principal.getUserId());
			bankCardService.create(bankCard);
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("银行卡保存成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error(e.getMessage()));
			return "redirect:/u/bankCard/create";
		}
		
		return "redirect:/u/bankCard";
	}
	
	@RequestMapping("/{id}")
	public String detail(Principal principal, @PathVariable Long id, Model model) {
		BankCard bankCard = bankCardService.findOne(id);
		if(bankCard != null) {
			if(!bankCard.getUserId().equals(principal.getUserId())) {
				throw new UnauthorizedException();
			}
		}
		model.addAttribute("bankCard", bankCard);
		return "ucenter/user/bankCardEdit";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(BankCard bankCard, Model model, Principal principal, RedirectAttributes redirectAttributes) {
		
		Long bankCardId = bankCard.getId();
		BankCard persistence = bankCardService.findOne(bankCardId);
		if (!persistence.getUserId().equals(principal.getUserId())) {
			throw new UnauthorizedException("权限不足只能编辑自己的地址");
		}
		
		try {
			bankCardService.update(bankCard);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error(e.getMessage()));
			return "redirect:/u/bankCard/edit";
		}
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("银行卡修改成功"));
		return "redirect:/u/bankCard";
	}
	
	@RequestMapping(value = "/delete/{id}")
	public String delete(@PathVariable Long id, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		BankCard bankCard = bankCardService.findOne(id);
		if(bankCard != null) {
			if(!bankCard.getUserId().equals(principal.getUserId())) {
				throw new UnauthorizedException();
			}
		}
		bankCardService.delete(id);
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("银行卡删除成功"));
		return "redirect:/u/bankCard";
	}
	
	@RequestMapping(value = "/createAjax")
	@ResponseBody
	public Result<BankCard> createAjax(BankCard bankCard, Principal principal) {
		bankCard.setUserId(principal.getUserId());
		bankCard.setIsDefault(false);
		bankCard = bankCardService.create(bankCard);
		return ResultBuilder.result(bankCard);
	}
	
	@RequestMapping(value = "/listAjax")
	@ResponseBody
	public Result<List<BankCard>> listAjax(Principal principal) {
		List<BankCard> bankCardList = bankCardService.findByUserId(principal.getUserId());
		return ResultBuilder.result(bankCardList);
	}
}
