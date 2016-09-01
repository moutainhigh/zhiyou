package com.zy.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zy.entity.fnc.Payment;
import com.zy.model.query.PaymentQueryModel;


public interface PaymentMapper {

	int insert(Payment payment);

	int update(Payment payment);

	int merge(@Param("payment") Payment payment, @Param("fields")String... fields);

	int delete(Long id);

	Payment findOne(Long id);

	List<Payment> findAll(PaymentQueryModel paymentQueryModel);

	long count(PaymentQueryModel paymentQueryModel);

	Payment findBySn(String sn);

	Payment findByBizNameAndBizSn(@Param("bizName") String bizName, @Param("bizSn") String bizSn);

}