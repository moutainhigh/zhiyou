package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.fnc.BankCard;
import com.zy.entity.fnc.Withdraw;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.BankCardAdminVo;
import com.zy.vo.WithdrawAdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

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
		withdrawAdminVo.setWithdrawStatusStyle(GcUtils.getWithdrawStatusStyle(withdraw.getWithdrawStatus()));
		return withdrawAdminVo;
	}
	
}
