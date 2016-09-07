package com.zy.service.impl;

import com.zy.common.model.query.Page;
import com.zy.component.FncComponent;
import com.zy.entity.fnc.Payment;
import com.zy.mapper.PaymentMapper;
import com.zy.model.query.PaymentQueryModel;
import com.zy.service.PaymentService;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

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
	public void success(@NotNull Long id, String outerSn) {
		fncComponent.payPayment(id);
	}

	@Override
	public Payment findByBizNameAndBizName(@NotBlank String bizName, @NotBlank String bizSn) {
		return paymentMapper.findByBizNameAndBizSn(bizName, bizSn);
	}

}
