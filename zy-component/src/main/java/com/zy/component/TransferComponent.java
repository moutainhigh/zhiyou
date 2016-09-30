package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.fnc.Transfer;
import com.zy.entity.usr.User;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.TransferAdminVo;
import com.zy.vo.TransferListVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.zy.util.GcUtils.formatDate;
import static com.zy.util.GcUtils.getTransferStatusStyle;

@Component
public class TransferComponent {

	@Autowired
	private CacheComponent cacheComponent;
	
	private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	public TransferAdminVo buildAdminVo(Transfer transfer) {
		TransferAdminVo transferAdminVo = new TransferAdminVo();
		BeanUtils.copyProperties(transfer, transferAdminVo);
		
		transferAdminVo.setFromUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(transfer.getFromUserId())));
		transferAdminVo.setToUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(transfer.getToUserId())));
		transferAdminVo.setCreatedTimeLabel(formatDate(transfer.getCreatedTime(), TIME_PATTERN));
		transferAdminVo.setTransferredTimeLabel(formatDate(transfer.getTransferredTime(), TIME_PATTERN));
		transferAdminVo.setTransferStatusStyle(getTransferStatusStyle(transfer.getTransferStatus()));

		transferAdminVo.setAmountLabel(GcUtils.formatCurreny(transfer.getAmount()));

		return transferAdminVo;
	}

	public TransferListVo buildListVo(Transfer transfer) {
		TransferListVo transferListVo = new TransferListVo();
		BeanUtils.copyProperties(transfer, transferListVo);
		
		Long toUserId = transfer.getToUserId();
		if(toUserId != null) {
			User user = cacheComponent.getUser(toUserId);
			transferListVo.setToUser(VoHelper.buildUserAdminSimpleVo(user));
		}
		transferListVo.setTransferStatusStyle(GcUtils.getTransferStatusStyle(transfer.getTransferStatus()));
		transferListVo.setAmountLabel(GcUtils.formatCurreny(transfer.getAmount()));
		transferListVo.setCreatedTimeLabel(GcUtils.formatDate(transfer.getCreatedTime(), TIME_PATTERN));
		transferListVo.setTransferredTimeLabel(GcUtils.formatDate(transfer.getTransferredTime(), TIME_PATTERN));
		return transferListVo;
	}
}
