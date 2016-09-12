package com.zy.component;

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
	
	public PaymentAdminVo buildAdminVo(Payment payment) {
		PaymentAdminVo paymentAdminVo = new PaymentAdminVo();
		BeanUtils.copyProperties(payment, paymentAdminVo);
		
		paymentAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(payment.getUserId())));
		return paymentAdminVo;
	}
}
