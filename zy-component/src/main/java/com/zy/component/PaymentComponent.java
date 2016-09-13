package com.zy.component;

import static com.zy.util.GcUtils.formatDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.fnc.Payment;
import com.zy.util.VoHelper;
import com.zy.vo.PaymentAdminVo;

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
		return paymentAdminVo;
	}
}
