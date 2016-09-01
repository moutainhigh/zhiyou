package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.fnc.AccountLog;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.usr.User;
import com.zy.util.VoHelper;
import com.zy.vo.AccountLogAdminVo;
import com.zy.vo.AccountLogSimpleVo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountLogComponent {

	@Autowired
	private CacheComponent cacheComponent;

	public AccountLogSimpleVo buildSimpleVo(AccountLog accountLog) {
		AccountLogSimpleVo accountLogSimpleVo = new AccountLogSimpleVo();
		accountLogSimpleVo.setTransTimeLabel(DateFormatUtils.format(accountLog.getTransTime(), "yyyy-MM-dd HH:mm:ss"));
		accountLogSimpleVo.setTitle(accountLog.getTitle());
		BigDecimal transAmount = accountLog.getTransAmount();
		BigDecimal afterAmount = accountLog.getAfterAmount();
		accountLogSimpleVo.setInOut(accountLog.getInOut());
		if (accountLog.getCurrencyType() == CurrencyType.积分) {
			accountLogSimpleVo.setTransAmount(transAmount.setScale(0));
			accountLogSimpleVo.setAfterAmount(afterAmount.setScale(0));
		} else {
			accountLogSimpleVo.setTransAmount(transAmount);
			accountLogSimpleVo.setAfterAmount(afterAmount);
		}
		return accountLogSimpleVo;
	}

	public AccountLogAdminVo buildAdminVo(AccountLog accountLog) {
		AccountLogAdminVo accountLogAdminVo = new AccountLogAdminVo();
		BeanUtils.copyProperties(accountLog, accountLogAdminVo);
		User user = cacheComponent.getUser(accountLog.getUserId());
		accountLogAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(user));
		return accountLogAdminVo;
	}
}
