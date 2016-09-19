package com.zy.component;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.fnc.Bank;
import com.zy.entity.fnc.BankCard;
import com.zy.entity.usr.User;
import com.zy.service.BankService;
import com.zy.util.VoHelper;
import com.zy.vo.BankCardAdminVo;
import com.zy.vo.BankCardVo;

@Component
public class BankCardComponent {

	@Autowired
	private CacheComponent cacheComponent;

	@Autowired
	private BankService bankService;
	
	public BankCardAdminVo buildAdminVo(BankCard bankCard) {
		BankCardAdminVo bankCardAdminVo = new BankCardAdminVo();
		BeanUtils.copyProperties(bankCard, bankCardAdminVo);
		
		User user = cacheComponent.getUser(bankCard.getUserId());
		bankCardAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(user));
		return bankCardAdminVo;
	}

	public BankCardVo buildVo(BankCard bankCard) {
		BankCardVo bankCardVo = new BankCardVo();
		BeanUtils.copyProperties(bankCard, bankCardVo);
		
		bankCardVo.setCardNumberLabel(StringUtils.right(bankCard.getCardNumber(), 4));
		Bank bank = bankService.findOne(bankCard.getBankId());
		bankCardVo.setBankCode(bank.getCode());
		return bankCardVo;
	}
	
}
