package com.zy.component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.fnc.Deposit;
import com.zy.entity.usr.User;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.DepositAdminVo;

@Component
public class DepositComponent {
	
	@Autowired
	private CacheComponent cacheComponent;
	
	public DepositAdminVo buildAdminVo(Deposit deposit) {
		DepositAdminVo depositAdminVo = new DepositAdminVo();
		BeanUtils.copyProperties(deposit, depositAdminVo);

		User user = cacheComponent.getUser(deposit.getUserId());
		depositAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(user));

		String offlineImage = deposit.getOfflineImage();
		if (StringUtils.isNotBlank(offlineImage)) {
			String[] offlineImages = StringUtils.split(offlineImage);
			depositAdminVo.getOfflineImages().addAll(Stream.of(offlineImages).filter(v -> StringUtils.isNotBlank(v)).collect(Collectors.toList()));
			depositAdminVo.getOfflineImageThumbnails().addAll(Stream.of(offlineImages).filter(v -> StringUtils.isNotBlank(v)).map(v -> GcUtils.getThumbnail(v)).collect(Collectors.toList()));
		}

		depositAdminVo.setDepositStatusStyle(GcUtils.getDepositStatusStyle(deposit.getDepositStatus()));
		depositAdminVo.setAmount1Label(GcUtils.formatCurreny(deposit.getAmount1()));
		depositAdminVo.setAmount2Label(GcUtils.formatCurreny(deposit.getAmount2()));
		depositAdminVo.setTotalAmountLabel(GcUtils.formatCurreny(deposit.getTotalAmount()));

		return depositAdminVo;
	}
	
}
