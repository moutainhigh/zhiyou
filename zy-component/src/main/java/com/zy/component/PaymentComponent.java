package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.fnc.Payment;
import com.zy.model.ImageVo;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.PaymentAdminVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zy.util.GcUtils.formatDate;

@Component
public class PaymentComponent {

	@Autowired
	private CacheComponent cacheComponent;
	
	private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	public PaymentAdminVo buildAdminVo(Payment payment) {
		PaymentAdminVo paymentAdminVo = new PaymentAdminVo();
		BeanUtils.copyProperties(payment, paymentAdminVo);
		
		paymentAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(payment.getUserId())));
		paymentAdminVo.setCreatedTimeLabel(formatDate(payment.getCreatedTime(), TIME_PATTERN));
		paymentAdminVo.setExpiredTimeLabel(formatDate(payment.getExpiredTime(), TIME_PATTERN));
		paymentAdminVo.setPaidTimeLabel(formatDate(payment.getPaidTime(), TIME_PATTERN));
		paymentAdminVo.setRefundedTimeLabel(formatDate(payment.getRefundedTime(), TIME_PATTERN));
		paymentAdminVo.setPaymentStatusStyle(GcUtils.getPaymentStatusStyle(payment.getPaymentStatus()));
		
		String offlineImage = payment.getOfflineImage();
		if (StringUtils.isNotBlank(offlineImage)) {
			String[] offlineImages = StringUtils.split(offlineImage);
			List<ImageVo> images = Stream.of(offlineImages).filter(v -> StringUtils.isNotBlank(v)).map(v -> {
				ImageVo imageVo = new ImageVo();
				imageVo.setImage(v);
				imageVo.setImageThumbnail(GcUtils.getThumbnail(v));
				return imageVo;
			}).collect(Collectors.toList());
			paymentAdminVo.setOfflineImages(images);
		}

		paymentAdminVo.setAmount1Label(GcUtils.formatCurreny(payment.getAmount1()));
		paymentAdminVo.setAmount2Label(GcUtils.formatCurreny(payment.getAmount2()));


		return paymentAdminVo;
	}
}
