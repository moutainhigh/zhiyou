package com.zy.component;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.zy.vo.DepositExportVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.fnc.Deposit;
import com.zy.entity.usr.User;
import com.zy.model.ImageVo;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.DepositAdminVo;
import com.zy.vo.DepositListVo;

@Component
public class DepositComponent {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
	
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
		depositAdminVo.setCreatedTimeLabel(dateFormat.format(deposit.getCreatedTime()));
		if(deposit.getPaidTime() != null) {
			depositAdminVo.setPaidTimeLabel(dateFormat.format(deposit.getPaidTime()));
		}
		
		return depositAdminVo;
	}

	public DepositListVo buildListVo(Deposit deposit) {
		DepositListVo depositListVo = new DepositListVo();
		BeanUtils.copyProperties(deposit, depositListVo);
		depositListVo.setAmount1Label(GcUtils.formatCurreny(deposit.getAmount1()));
		depositListVo.setAmount2Label(GcUtils.formatCurreny(deposit.getAmount2()));
		depositListVo.setTotalAmountLabel(GcUtils.formatCurreny(deposit.getTotalAmount()));
		depositListVo.setCreatedTimeLabel(simpleDateFormat.format(deposit.getCreatedTime()));
		if(deposit.getPaidTime() != null) {
			depositListVo.setPaidTimeLabel(simpleDateFormat.format(deposit.getPaidTime()));
		}
		return depositListVo;
	}

	public DepositExportVo buildExportVo(Deposit deposit) {
		DepositExportVo depositExportVo = new DepositExportVo();
		BeanUtils.copyProperties(deposit, depositExportVo);

		User user = cacheComponent.getUser(deposit.getUserId());
		depositExportVo.setUserNickname(user.getNickname());
		depositExportVo.setUserPhone(user.getPhone());
		depositExportVo.setAmount1Label(GcUtils.formatCurreny(deposit.getAmount1()));
		depositExportVo.setCreatedTimeLabel(dateFormat.format(deposit.getCreatedTime()));
		if (deposit.getPaidTime() != null) {
			depositExportVo.setPaidTimeLabel(dateFormat.format(deposit.getPaidTime()));
		}
		if (deposit.getExpiredTime() != null) {
			depositExportVo.setExpiredTimeLabel(dateFormat.format(deposit.getExpiredTime()));
		}

		return depositExportVo;
	}
	
}
