package com.zy.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.fnc.BankCard;
import com.zy.entity.usr.User;
import com.zy.util.VoHelper;
import com.zy.vo.BankCardAdminVo;

@Component
public class BankCardComponent {

	@Autowired
	private CacheComponent cacheComponent;

	public BankCardAdminVo buildAdminVo(BankCard bankCard) {
		BankCardAdminVo bankCardAdminVo = new BankCardAdminVo();
		BeanUtils.copyProperties(bankCard, bankCardAdminVo);
		
		User user = cacheComponent.getUser(bankCard.getUserId());
		bankCardAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(user));
		return bankCardAdminVo;
	}

}
