package com.gc.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.gc.entity.fnc.BankCard;
import com.gc.entity.usr.User;
import com.zy.util.VoHelper;
import com.gc.vo.BankCardAdminVo;

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
