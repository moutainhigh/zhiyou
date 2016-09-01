package com.gc.service;

import java.util.List;

import com.zy.common.model.query.Page;
import com.gc.entity.fnc.Payment;
import com.gc.model.query.PaymentQueryModel;

public interface PaymentService {

	Payment findOne(Long id);

	Page<Payment> findPage(PaymentQueryModel paymentQueryModel);

	List<Payment> findAll(PaymentQueryModel paymentQueryModel);

	Payment findBySn(String sn);

	void pay(Long id);

	Payment findByBizNameAndBizName(String bizName, String bizSn);
}
