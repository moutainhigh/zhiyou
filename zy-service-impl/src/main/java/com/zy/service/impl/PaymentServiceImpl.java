package com.gc.service.impl;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.zy.common.model.query.Page;
import com.gc.component.FncComponent;
import com.gc.entity.fnc.Payment;
import com.gc.mapper.PaymentMapper;
import com.gc.model.query.PaymentQueryModel;
import com.gc.service.PaymentService;

@Service
@Validated
public class PaymentServiceImpl implements PaymentService {

	Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

	@Autowired
	private PaymentMapper paymentMapper;

	@Autowired
	private FncComponent fncComponent;

	@Override
	public Payment findOne(@NotNull Long id) {
		return paymentMapper.findOne(id);
	}

	@Override
	public Page<Payment> findPage(@NotNull PaymentQueryModel paymentQueryModel) {
		if (paymentQueryModel.getPageNumber() == null)
			paymentQueryModel.setPageNumber(0);
		if (paymentQueryModel.getPageSize() == null)
			paymentQueryModel.setPageSize(20);
		long total = paymentMapper.count(paymentQueryModel);
		List<Payment> data = paymentMapper.findAll(paymentQueryModel);
		Page<Payment> page = new Page<>();
		page.setPageNumber(paymentQueryModel.getPageNumber());
		page.setPageSize(paymentQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public List<Payment> findAll(@NotNull PaymentQueryModel paymentQueryModel) {
		return paymentMapper.findAll(paymentQueryModel);
	}

	@Override
	public Payment findBySn(@NotBlank String sn) {
		return paymentMapper.findBySn(sn);
	}

	@Override
	public void pay(@NotNull Long id) {
		fncComponent.payPayment(id);
	}

	@Override
	public Payment findByBizNameAndBizName(@NotBlank String bizName, @NotBlank String bizSn) {
		return paymentMapper.findByBizNameAndBizSn(bizName, bizSn);
	}

}
