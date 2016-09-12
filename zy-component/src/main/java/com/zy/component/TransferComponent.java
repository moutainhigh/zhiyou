package com.zy.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.fnc.Transfer;
import com.zy.util.VoHelper;
import com.zy.vo.TransferAdminVo;

@Component
public class TransferComponent {

	@Autowired
	private CacheComponent cacheComponent;
	
	public TransferAdminVo buildAdminVo(Transfer transfer) {
		TransferAdminVo transferAdminVo = new TransferAdminVo();
		BeanUtils.copyProperties(transfer, transferAdminVo);
		
		transferAdminVo.setFromUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(transfer.getFromUserId())));
		transferAdminVo.setToUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(transfer.getToUserId())));
		return transferAdminVo;
	}
}
