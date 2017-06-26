package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.fnc.AccountLog;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.usr.User;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.AccountLogAdminVo;
import com.zy.vo.AccountLogSimpleVo;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountLogComponent {

	private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	@Autowired
	private CacheComponent cacheComponent;

	public AccountLogSimpleVo buildSimpleVo(AccountLog accountLog) {
		AccountLogSimpleVo accountLogSimpleVo = new AccountLogSimpleVo();
		BeanUtils.copyProperties(accountLog, accountLogSimpleVo);
		accountLogSimpleVo.setTransTimeLabel(DateFormatUtils.format(accountLog.getTransTime(), TIME_PATTERN));
		accountLogSimpleVo.setTitle(accountLog.getTitle());
		BigDecimal transAmount = accountLog.getTransAmount();
		BigDecimal afterAmount = accountLog.getAfterAmount();
		accountLogSimpleVo.setInOut(accountLog.getInOut());
		accountLogSimpleVo.setTransAmount(transAmount);
		accountLogSimpleVo.setAfterAmount(afterAmount);

		User user = cacheComponent.getUser(accountLog.getUserId());
		accountLogSimpleVo.setUser(VoHelper.buildUserAdminSimpleVo(user));

		User refUser = cacheComponent.getUser(accountLog.getRefUserId());
		accountLogSimpleVo.setRefUser(VoHelper.buildUserAdminSimpleVo(refUser));
		
		return accountLogSimpleVo;
	}

	public AccountLogAdminVo buildAdminVo(AccountLog accountLog) {
		AccountLogAdminVo accountLogAdminVo = new AccountLogAdminVo();
		BeanUtils.copyProperties(accountLog, accountLogAdminVo);

		User user = cacheComponent.getUser(accountLog.getUserId());
		accountLogAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(user));

		User refUser = cacheComponent.getUser(accountLog.getRefUserId());
		accountLogAdminVo.setRefUser(VoHelper.buildUserAdminSimpleVo(refUser));

		accountLogAdminVo.setAfterAmountLabel(GcUtils.formatCurreny(accountLog.getAfterAmount()));
		accountLogAdminVo.setBeforeAmountLabel(GcUtils.formatCurreny(accountLog.getBeforeAmount()));
		accountLogAdminVo.setTransAmountLabel(GcUtils.formatCurreny(accountLog.getTransAmount()));

		return accountLogAdminVo;
	}
}
