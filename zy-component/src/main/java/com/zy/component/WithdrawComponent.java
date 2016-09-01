package com.zy.component;

import static java.util.Objects.isNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.fnc.BankCard;
import com.zy.entity.fnc.Withdraw;
import com.zy.util.VoHelper;
import com.zy.vo.BankCardAdminVo;
import com.zy.vo.WithdrawAdminVo;

@Component
public class WithdrawComponent {
	
	@Autowired
	private CacheComponent cacheComponent;
	
	public WithdrawAdminVo buildAdminVo(Withdraw withdraw) {
		WithdrawAdminVo withdrawAdminVo = new WithdrawAdminVo();
		BeanUtils.copyProperties(withdraw, withdrawAdminVo);
		withdrawAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(withdraw.getUserId())));
		
		if(withdraw.getIsToBankCard()) {
			Long bankCardId = withdraw.getBankCardId();
			if(!isNull(bankCardId)) {
				BankCard bankCard = cacheComponent.getBankCard(bankCardId);
				BankCardAdminVo bankCardAdminVo = new BankCardAdminVo();
				BeanUtils.copyProperties(bankCard, bankCardAdminVo);
				withdrawAdminVo.setBankCard(bankCardAdminVo);
			}
		}
		return withdrawAdminVo;
	}
	
}
