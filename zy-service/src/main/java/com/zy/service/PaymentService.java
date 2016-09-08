package com.zy.service;

import com.zy.common.model.query.Page;
import com.zy.entity.fnc.Payment;
import com.zy.model.query.PaymentQueryModel;

import java.util.List;

public interface PaymentService {

	Payment findOne(Long id);

	Page<Payment> findPage(PaymentQueryModel paymentQueryModel);

	List<Payment> findAll(PaymentQueryModel paymentQueryModel);

	Payment findBySn(String sn);

	void success(Long id, String outerSn);

	void offlineSuccess(Long id, Long operatorId, String remark);

	void balancePay(Long paymentId, boolean checkBalance);

	Payment create(Payment payment);

	List<Payment> findByBizNameAndBizName(String bizName, String bizSn);
}
