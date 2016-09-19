package com.zy.component;

import com.zy.entity.fnc.Account;
import com.zy.entity.usr.User;
import com.zy.model.query.AccountQueryModel;
import com.zy.service.AccountService;
import com.zy.util.VoHelper;
import com.zy.vo.AccountAdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountComponent {

	@Autowired
	private AccountService accountService;
	
	
	public List<AccountAdminVo> buildAdminVo(List<User> users) {
		if (users.isEmpty()) {
			return new ArrayList<>();
		}

		AccountQueryModel accountQueryModel = new AccountQueryModel();
		accountQueryModel.setUserIdIN(users.stream().map(v -> v.getId()).toArray(Long[]::new));
		List<Account> accounts = accountService.findAll(accountQueryModel);
		
		return users.stream().map(v -> {
			Long userId = v.getId();
			AccountAdminVo accountAdminVo = new AccountAdminVo();
			accountAdminVo.setUserId(userId);
			accountAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(v));
			accounts.stream().forEach(u -> {
				if (u.getUserId().equals(userId)) {
					BigDecimal amount = u.getAmount();
					switch (u.getCurrencyType()) {
					case 现金:
						accountAdminVo.setMoney(amount);
						break;
					case 金币:
						accountAdminVo.setCoin(amount);
						break;
					case 积分:
						accountAdminVo.setPoint(amount);
						break;
					default:
						break;
					}
				}
			});
			return accountAdminVo;
		}).collect(Collectors.toList());
	}
	
}
