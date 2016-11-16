package com.zy.component;

import static java.util.Objects.isNull;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.fnc.BankCard;
import com.zy.entity.fnc.Withdraw;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.BankCardAdminVo;
import com.zy.vo.WithdrawAdminVo;
import com.zy.vo.WithdrawListVo;

@Component
public class WithdrawComponent {
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	
	@Autowired
	private CacheComponent cacheComponent;
	
	public WithdrawAdminVo buildAdminVo(Withdraw withdraw) {
		WithdrawAdminVo withdrawAdminVo = new WithdrawAdminVo();
		BeanUtils.copyProperties(withdraw, withdrawAdminVo);

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
		withdrawAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(withdraw.getUserId())));
		withdrawAdminVo.setAmountLabel(GcUtils.formatCurreny(withdraw.getAmount()));
		withdrawAdminVo.setFeeLabel(GcUtils.formatCurreny(withdraw.getFee()));
		withdrawAdminVo.setRealAmountLabel(GcUtils.formatCurreny(withdraw.getRealAmount()));
		withdrawAdminVo.setCreatedTimeLabel(dateFormat.format(withdraw.getCreatedTime())); 
		withdrawAdminVo.setWithdrawedTimeLabel(dateFormat.format(withdraw.getWithdrawedTime()));

		return withdrawAdminVo;
	}

	public WithdrawListVo buildListVo(Withdraw withdraw) {
		WithdrawListVo withdrawListVo = new WithdrawListVo();
		BeanUtils.copyProperties(withdraw, withdrawListVo);
		
		withdrawListVo.setAmountLabel(GcUtils.formatCurreny(withdraw.getAmount()));
		withdrawListVo.setFeeLabel(GcUtils.formatCurreny(withdraw.getFee()));
		withdrawListVo.setRealAmountLabel(GcUtils.formatCurreny(withdraw.getRealAmount()));
		withdrawListVo.setCreatedTimeLabel(simpleDateFormat.format(withdraw.getCreatedTime())); 
		withdrawListVo.setWithdrawedTimeLabel(simpleDateFormat.format(withdraw.getWithdrawedTime()));
		
		return withdrawListVo;
	}
	
}
