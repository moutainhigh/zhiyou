package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.fnc.Deposit;
import com.zy.entity.usr.User;
import com.zy.model.ImageVo;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.DepositAdminVo;
import com.zy.vo.DepositListVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
			String[] offlineImages = StringUtils.split(offlineImage, ",");
			List<ImageVo> images = Stream.of(offlineImages).filter(v -> StringUtils.isNotBlank(v)).map(v -> {
				ImageVo imageVo = new ImageVo();
				imageVo.setImage(v);
				imageVo.setImageThumbnail(GcUtils.getThumbnail(v));
				return imageVo;
			}).collect(Collectors.toList());
			depositAdminVo.setOfflineImages(images);
		}

		depositAdminVo.setDepositStatusStyle(GcUtils.getDepositStatusStyle(deposit.getDepositStatus()));
		depositAdminVo.setAmount1Label(GcUtils.formatCurreny(deposit.getAmount1()));
		depositAdminVo.setAmount2Label(GcUtils.formatCurreny(deposit.getAmount2()));
		depositAdminVo.setTotalAmountLabel(GcUtils.formatCurreny(deposit.getTotalAmount()));

		return depositAdminVo;
	}

	public DepositListVo buildListVo(Deposit deposit) {
		DepositListVo depositListVo = new DepositListVo();
		BeanUtils.copyProperties(deposit, depositListVo);
		// TODO
		return depositListVo;
	}
	
}
