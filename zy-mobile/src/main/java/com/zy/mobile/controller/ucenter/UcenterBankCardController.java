package com.zy.mobile.controller.ucenter;

import io.gd.generator.api.query.Direction;

import java.util.List;
import java.util.stream.Collectors;

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
import com.zy.common.support.cache.CacheSupport;
import com.zy.component.BankCardComponent;
import com.zy.entity.fnc.Bank;
import com.zy.entity.fnc.BankCard;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.Appearance;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.query.BankQueryModel;
import com.zy.service.AppearanceService;
import com.zy.service.BankCardService;
import com.zy.service.BankService;

@RequestMapping("/u/bankCard")
@Controller
public class UcenterBankCardController {
Logger logger = LoggerFactory.getLogger(UcenterBankCardController.class);

	@Autowired
	private BankService bankService;

	@Autowired
	private BankCardService bankCardService;
	
	@Autowired
	private BankCardComponent bankCardComponent;
	
	@Autowired
	private AppearanceService appearanceService;
	
	@Autowired
	private CacheSupport cacheSupport;
	
	private static final String CACHE_KEY_BANK = "bank";
	
	@RequestMapping
	public String list(Principal principal, Model model, Integer pageNumber) {
		List<BankCard> list = bankCardService.findByUserId(principal.getUserId());
		model.addAttribute("bankCards", list.stream().map(bankCardComponent::buildVo).collect(Collectors.toList()));
		return "ucenter/user/bankCardList";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Principal principal, Model model, RedirectAttributes redirectAttributes) {
		
		checkAppearance(principal.getUserId(), redirectAttributes);
		
		List<Bank> banks = cacheSupport.get(Constants.CACHE_NAME_BANK, CACHE_KEY_BANK);
		if(banks == null) {
			banks = bankService.findAll(BankQueryModel.builder().isDeletedEQ(false).orderBy("orderNumber").direction(Direction.ASC).build());
			
			cacheSupport.set(Constants.CACHE_NAME_BANK, CACHE_KEY_BANK, banks);
		}
		model.addAttribute("banks", banks);
		return "ucenter/user/bankCardCreate";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(BankCard bankCard, Model model, Principal principal, RedirectAttributes redirectAttributes) {
		
		checkAppearance(principal.getUserId(), redirectAttributes);
		
		Appearance appearance = appearanceService.findByUserId(principal.getUserId());
		if(appearance == null || appearance.getConfirmStatus() != ConfirmStatus.审核通过) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("您还没有完成实名认证过"));
			return "redirect:/u/userInfo";
		}
		
		try {
			bankCard.setUserId(principal.getUserId());
			bankCardService.create(bankCard);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("银行卡保存成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return "redirect:/u/bankCard/create";
		}
		
		return "redirect:/u/bankCard";
	}
	
	@RequestMapping("/{id}")
	public String edit(Principal principal, @PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
		
		checkAppearance(principal.getUserId(), redirectAttributes);
		
		BankCard bankCard = bankCardService.findOne(id);
		if(bankCard != null) {
			if(!bankCard.getUserId().equals(principal.getUserId())) {
				throw new UnauthorizedException();
			}
		}
		model.addAttribute("bankCard", bankCardComponent.buildVo(bankCard));
		
		List<Bank> banks = cacheSupport.get(Constants.CACHE_NAME_BANK, CACHE_KEY_BANK);
		if(banks == null) {
			banks = bankService.findAll(BankQueryModel.builder().isDeletedEQ(false).orderBy("orderNumber").direction(Direction.ASC).build());
			
			cacheSupport.set(Constants.CACHE_NAME_BANK, CACHE_KEY_BANK, banks);
		}
		model.addAttribute("banks", banks);
		
		return "ucenter/user/bankCardEdit";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(BankCard bankCard, Model model, Principal principal, RedirectAttributes redirectAttributes) {
		
		checkAppearance(principal.getUserId(), redirectAttributes);
		
		Long bankCardId = bankCard.getId();
		BankCard persistence = bankCardService.findOne(bankCardId);
		if (!persistence.getUserId().equals(principal.getUserId())) {
			throw new UnauthorizedException("权限不足只能编辑自己的银行卡");
		}
		
		try {
			bankCardService.update(bankCard);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return "redirect:/u/bankCard/edit";
		}
		redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("银行卡修改成功"));
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
		redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("银行卡删除成功"));
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

	private String checkAppearance(Long userId, RedirectAttributes redirectAttributes) {
		Appearance appearance = appearanceService.findByUserId(userId);
		if(appearance == null || appearance.getConfirmStatus() != ConfirmStatus.审核通过) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("您还没有完成实名认证过"));
			return "redirect:/u/userInfo";
		}
		return null;
	}
}
