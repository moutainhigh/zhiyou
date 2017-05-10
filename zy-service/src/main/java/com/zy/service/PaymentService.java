package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.fnc.Payment;
import com.zy.model.query.PaymentQueryModel;

import java.util.Date;
import java.util.List;

public interface PaymentService {

	Payment findOne(Long id);

	long count(PaymentQueryModel paymentQueryModel);
	
	Page<Payment> findPage(PaymentQueryModel paymentQueryModel);

	List<Payment> findAll(PaymentQueryModel paymentQueryModel);

	Payment findBySn(String sn);

	void success(Long id, String outerSn);

	void offlineSuccess(Long id, Long operatorId, String remark);
	
	void offlineFailure(Long id, Long operatorId, String remark);

	void balancePay(Long paymentId, boolean checkBalance);
	
	void modifyOffline(Long paymentId, String offlineImage, String offlineMemo);

	Payment modifyOuterSn(Long paymentId, String outerSn, Date expiredTime);

	Payment create(Payment payment);

	void cancel(Long id);

	void refund(Long id);

}
