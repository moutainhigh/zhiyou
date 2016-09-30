package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.fnc.Transfer;
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
		return null;
	}
}
