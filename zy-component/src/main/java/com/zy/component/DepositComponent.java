package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.fnc.Deposit;
import com.zy.entity.usr.User;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.DepositAdminVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.zy.util.GcUtils.getThumbnail;

@Component
public class DepositComponent {
	
	@Autowired
	private CacheComponent cacheComponent;
	
	public DepositAdminVo buildAdminVo(Deposit deposit) {
		DepositAdminVo depositAdminVo = new DepositAdminVo();
		BeanUtils.copyProperties(deposit, depositAdminVo);

		User user = cacheComponent.getUser(deposit.getUserId());
		depositAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(user));

		depositAdminVo.setOfflineImageThumbnail(getThumbnail(deposit.getOfflineImage()));
		depositAdminVo.setDepositStatusStyle(GcUtils.getDepositStatusStyle(deposit.getDepositStatus()));
		depositAdminVo.setAmount1Label(GcUtils.formatCurreny(deposit.getAmount1()));
		depositAdminVo.setAmount2Label(GcUtils.formatCurreny(deposit.getAmount2()));
		depositAdminVo.setTotalAmountLabel(GcUtils.formatCurreny(deposit.getTotalAmount()));

		return depositAdminVo;
	}
	
}
