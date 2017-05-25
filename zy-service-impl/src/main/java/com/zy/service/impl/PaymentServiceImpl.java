package com.zy.service.impl;

import com.zy.Config;
import com.zy.ServiceUtils;
import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.component.ActComponent;
import com.zy.component.FncComponent;
import com.zy.component.MalComponent;
import com.zy.entity.fnc.*;
import com.zy.entity.fnc.Payment.PaymentStatus;
import com.zy.entity.mal.Order;
import com.zy.entity.usr.User;
import com.zy.mapper.AccountMapper;
import com.zy.mapper.OrderMapper;
import com.zy.mapper.PaymentMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.BizCode;
import com.zy.model.query.PaymentQueryModel;
import com.zy.service.PaymentService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.*;

@Service
@Validated
public class PaymentServiceImpl implements PaymentService {

	Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

	@Autowired
	private PaymentMapper paymentMapper;
	
	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private FncComponent fncComponent;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private MalComponent malComponent;

	@Autowired
	private ActComponent actComponent;

	@Autowired
	private AccountMapper accountMapper;

	@Autowired
	private Config config;

	@Override
	public Payment findOne(@NotNull Long id) {
		return paymentMapper.findOne(id);
	}

	@Override
	public void update(Payment payment) {
		validate(payment);
		if (paymentMapper.update(payment) == 0) {
			throw new ConcurrentException();
		}
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
	public Payment create(@NotNull Payment payment) {

		Long userId = payment.getUserId();
		CurrencyType currencyType1 = payment.getCurrencyType1();
		CurrencyType currencyType2 = payment.getCurrencyType2();

		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + " is not found");
		BigDecimal amount2 = payment.getAmount2();

		if (currencyType2 != null) {
			validate(currencyType2, v -> v != currencyType1, "currency type 1 must not be the same with currency type 2");
			validate(amount2, NOT_NULL, "amount 2 is null");
			validate(amount2, v -> v.compareTo(new BigDecimal("0.00")) > 0, "amount 2 must be greater than 0.00");
		} else {
			validate(amount2, NULL, "amount 2 must be null");
		}

		if (payment.getPaymentType() == Payment.PaymentType.订单支付) {
			Long orderId = payment.getRefId();
			validate(orderId, NOT_NULL, "order id is null");
			Order order = orderMapper.findOne(orderId);
			validate(order, NOT_NULL, "order id " +  orderId +" is not found");
			if (!order.getIsPayToPlatform()) {
				throw new BizException(BizCode.ERROR, "只有平台收款订单才能创建支付单");
			}
		}

		payment.setSn(ServiceUtils.generatePaymentSn());
		payment.setCreatedTime(new Date());
		payment.setPaymentStatus(PaymentStatus.待支付);
		payment.setIsOuterCreated(false);
		payment.setVersion(0);
		validate(payment);
		paymentMapper.insert(payment);
		return payment;
	}

	@Override
	public void success(@NotNull Long id, String outerSn) {
		Payment payment = paymentMapper.findOne(id);
		validate(payment, NOT_NULL, "payment id " + id + "is not found null");

		PayType payType = payment.getPayType();
		if (payType != PayType.支付宝 && payType != PayType.微信 && payType != PayType.支付宝手机 && payType != PayType.富友支付 && payType != PayType.盛付通) {
			throw new BizException(BizCode.ERROR, "支付方式错误");
		}

		if (payType != PayType.微信 && payType != PayType.富友支付 && payType != PayType.盛付通 &&  StringUtils.isBlank(outerSn)) {
			throw new BizException(BizCode.ERROR, "outer sn参数缺失");
		}

		if (payment.getPaymentStatus() == Payment.PaymentStatus.已支付) {
			return; // 幂等处理
		} else if (payment.getPaymentStatus() == Payment.PaymentStatus.已取消) {
			logger.warn("已取消支付单支付成功");
		}

		payment.setOuterSn(outerSn);
		updateSuccess(payment);
		onSuccess(payment);
	}

	@Override
	public void offlineSuccess(@NotNull Long id, @NotNull Long operatorId, @NotBlank String remark) {

		Payment payment = paymentMapper.findOne(id);
		validate(payment, NOT_NULL, "payment id " + id + "is not found null");

		User operator = userMapper.findOne(operatorId);
		validate(operator, NOT_NULL, "operator id" + operatorId + " is not found null");

		PayType payType = payment.getPayType();

		if (payType != PayType.银行汇款) {
			throw new BizException(BizCode.ERROR, "支付方式只能为银行汇款");
		}
		if (payment.getPaymentStatus() == Payment.PaymentStatus.已支付) {
			return; // 幂等处理
		}
		if (payment.getPaymentStatus() != PaymentStatus.待确认) {
			throw new BizException(BizCode.ERROR, "只有待确认的支付单才能确认支付");
		}

		payment.setRemark(remark);
		payment.setOperatorId(operatorId);
		payment.setPaidTime(new Date());
		updateSuccess(payment);
		onSuccess(payment);

	}
	
	@Override
	public void offlineFailure(@NotNull Long id, @NotNull Long operatorId, @NotBlank String remark) {

		Payment payment = paymentMapper.findOne(id);
		validate(payment, NOT_NULL, "payment id " + id + "is not found null");

		User operator = userMapper.findOne(operatorId);
		validate(operator, NOT_NULL, "operator id" + operatorId + " is not found null");

		PayType payType = payment.getPayType();

		if (payType != PayType.银行汇款) {
			throw new BizException(BizCode.ERROR, "支付方式只能为银行汇款");
		}
		if (payment.getPaymentStatus() == PaymentStatus.已取消) {
			return; // 幂等处理
		}
		if (payment.getPaymentStatus() != PaymentStatus.待确认) {
			throw new BizException(BizCode.ERROR, "只有待确认的支付单才能拒绝确认支付");
		}
		
		payment.setRemark(remark);
		payment.setOperatorId(operatorId);
		payment.setPaymentStatus(PaymentStatus.已取消);
		if (paymentMapper.update(payment) == 0) {
			throw new ConcurrentException();
		}

		onFailure(payment);
	}

	private void updateSuccess(Payment payment) {

		payment.setPaymentStatus(Payment.PaymentStatus.已支付);
		payment.setPaidTime(new Date());
		if (paymentMapper.update(payment) == 0) {
			throw new ConcurrentException();
		}

		Long sysUserId = config.getSysUserId();
		Long userId = payment.getUserId();

		/* 支付成功记录流水 */
		CurrencyType currencyType1 = payment.getCurrencyType1();
		CurrencyType currencyType2 = payment.getCurrencyType2();

		fncComponent.recordAccountLog(sysUserId, "支付成功", currencyType1, payment.getAmount1(), AccountLog.InOut.收入, payment, userId);

		if (currencyType2 != null) {
			fncComponent.recordAccountLog(sysUserId, "支付成功", currencyType2, payment.getAmount2(), AccountLog.InOut.收入, payment, userId);
		}
	}

	private void onFailure(Payment payment) {
		Payment.PaymentType paymentType = payment.getPaymentType();
		Long refId = payment.getRefId();
		if (paymentType == Payment.PaymentType.订单支付) {
			malComponent.failureOrder(refId, payment.getRemark());
		}
	}

	private void onSuccess(Payment payment) {
		Payment.PaymentType paymentType = payment.getPaymentType();
		Long refId = payment.getRefId();
		if (paymentType == Payment.PaymentType.订单支付) {
			malComponent.successOrder(refId);
		} else if (paymentType == Payment.PaymentType.活动报名) {
			actComponent.successActivityApply(refId);
		}else if(paymentType == Payment.PaymentType.团队报名){
			actComponent.successActivityTeamApply(refId);
		}
	}

	@Override
	public void balancePay(@NotNull Long paymentId, boolean checkBalance) {
		final BigDecimal zero = new BigDecimal("0.00");
		Payment payment = paymentMapper.findOne(paymentId);
		validate(payment, NOT_NULL, "payment id " + paymentId + " is not found");

		PayType payType = payment.getPayType();
		if (payType != PayType.余额) {
			throw new BizException(BizCode.ERROR, "支付类型错误");
		}
		Long sysUserId = config.getSysUserId();

		PaymentStatus paymentStatus = payment.getPaymentStatus();
		if (paymentStatus == PaymentStatus.已支付) {
			return; // 幂等操作
		} else if (paymentStatus != PaymentStatus.待支付) {
			throw new BizException(BizCode.ERROR, "只有待支付订单才能支付");
		}

		Long userId = payment.getUserId();
		CurrencyType currencyType1 = payment.getCurrencyType1();
		CurrencyType currencyType2 = payment.getCurrencyType2();
		BigDecimal amount1 = payment.getAmount1();
		BigDecimal amount2 = payment.getAmount2();
		Account account1 = accountMapper.findByUserIdAndCurrencyType(userId, currencyType1);

		BigDecimal beforeAmount1 = account1.getAmount();
		BigDecimal afterAmount1 = beforeAmount1.subtract(amount1);
		if (checkBalance) {
			if (afterAmount1.compareTo(zero) < 0) {
				throw new BizException(BizCode.INSUFFICIENT_BALANCE, "支付失败, " + currencyType1.getAlias() + "余额不足");
			}
		}

		String title = payment.getTitle();
		fncComponent.recordAccountLog(userId, title, currencyType1, amount1, AccountLog.InOut.支出, payment, sysUserId);
		fncComponent.recordAccountLog(sysUserId, title, currencyType1, amount1, AccountLog.InOut.收入, payment, userId);

		if (currencyType2 != null) {
			Account account2 = accountMapper.findByUserIdAndCurrencyType(userId, currencyType2);
			BigDecimal beforeAmount2 = account2.getAmount();
			BigDecimal afterAmount2 = beforeAmount2.subtract(amount2);
			if (checkBalance) {
				if (afterAmount2.compareTo(zero) < 0) {
					throw new BizException(BizCode.INSUFFICIENT_BALANCE, "支付失败, " + currencyType2.getAlias() + "余额不足");
				}
			}

			title = payment.getTitle();
			fncComponent.recordAccountLog(userId, title, currencyType2, amount2, AccountLog.InOut.支出, payment, sysUserId);
			fncComponent.recordAccountLog(sysUserId, title, currencyType2, amount2, AccountLog.InOut.收入, payment, userId);
		}

		payment.setPaidTime(new Date());
		payment.setPaymentStatus(PaymentStatus.已支付);
		if (paymentMapper.update(payment) == 0) {
			throw new ConcurrentException();
		}

		onSuccess(payment);
	}
	
	@Override
	public void modifyOffline(@NotNull Long paymentId, @NotBlank @URL String offlineImage, @NotBlank String offlineMemo) {
		Payment persistance = paymentMapper.findOne(paymentId);
		validate(persistance, NOT_NULL, "payment id " + paymentId + " is not found");
		
		if (persistance.getPayType() != PayType.银行汇款) {
			throw new BizException(BizCode.ERROR, "支付方式只能为银行汇款");
		}

		PaymentStatus paymentStatus = persistance.getPaymentStatus();
		if (paymentStatus != PaymentStatus.待支付 && paymentStatus != PaymentStatus.待确认)  {
			throw new BizException(BizCode.ERROR, "只有待支付或者待确认状态的支付单才能操作");
		}
		
		persistance.setOfflineMemo(offlineMemo);
		persistance.setOfflineImage(offlineImage);
		persistance.setPaymentStatus(PaymentStatus.待确认);
		if (paymentMapper.update(persistance) == 0) {
			throw new ConcurrentException();
		}
	}

	@Override
	public Payment modifyOuterSn(@NotNull Long paymentId, @NotBlank String outerSn, @NotNull Date expiredTime) {
		Payment payment = paymentMapper.findOne(paymentId);
		validate(payment, NOT_NULL, "payment id " + paymentId + " is not found");

		if (StringUtils.isNotBlank(payment.getOuterSn())) {
			return payment;
		}

		payment.setOuterSn(outerSn);
		payment.setExpiredTime(expiredTime);
		paymentMapper.update(payment);
		return paymentMapper.findOne(paymentId);
	}

	@Override
	public void cancel(@NotNull Long id) {
		Payment payment = paymentMapper.findOne(id);
		validate(payment, NOT_NULL, "payment id " + id + "is not found");
		Payment.PaymentStatus paymentStatus = payment.getPaymentStatus();
		if (paymentStatus == Payment.PaymentStatus.已取消) {
			return; // 幂等处理
		} else if (paymentStatus != Payment.PaymentStatus.待支付) {
			throw new BizException(BizCode.ERROR, "只有待支付状态的支付订单才能取消");
		}
		payment.setPaymentStatus(Payment.PaymentStatus.已取消);
		if (paymentMapper.update(payment) == 0) {
			throw new ConcurrentException();
		}
	}

	@Override
	public void refund(@NotNull Long id) {

		Payment payment = paymentMapper.findOne(id);
		validate(payment, NOT_NULL, "payment id " + id + " is not found");

		Payment.PaymentStatus paymentStatus = payment.getPaymentStatus();
		if (paymentStatus == Payment.PaymentStatus.已退款) {
			return; // 幂等处理
		} else if (paymentStatus != Payment.PaymentStatus.已支付) {
			throw new BizException(BizCode.ERROR, "只有已支付支付订单才能退款");
		}

		PayType payType = payment.getPayType();

		if (payType == PayType.余额) {

			CurrencyType currencyType1 = payment.getCurrencyType1();
			BigDecimal amount1 = payment.getAmount1();

			String title = payment.getTitle() + currencyType1.getAlias() + "退款";
			Long sysUserId = config.getSysUserId();
			Long userId = payment.getUserId();
			fncComponent.recordAccountLog(sysUserId, title, currencyType1, amount1, AccountLog.InOut.支出, payment, userId);
			fncComponent.recordAccountLog(userId, title, currencyType1, amount1, AccountLog.InOut.收入, payment, sysUserId);

			CurrencyType currencyType2 = payment.getCurrencyType2();
			BigDecimal amount2 = payment.getAmount2();
			if (currencyType2 != null) {
				title = payment.getTitle() + currencyType2.getAlias() + "退款";
				fncComponent.recordAccountLog(sysUserId, title, currencyType2, amount2, AccountLog.InOut.支出, payment, userId);
				fncComponent.recordAccountLog(userId, title, currencyType2, amount2, AccountLog.InOut.收入, payment, sysUserId);
			}
			payment.setRefundedTime(new Date());
			payment.setRefund1(amount1);
			payment.setRefund2(amount2);
			payment.setPaymentStatus(Payment.PaymentStatus.已退款);

			if (paymentMapper.update(payment) == 0) {
				throw new ConcurrentException();
			}
		} else {
			throw new BizException(BizCode.ERROR, "暂不支持余额以外其他支付方式退款");
		}
	}

	@Override
	public long count(PaymentQueryModel paymentQueryModel) {
		return paymentMapper.count(paymentQueryModel);
	}

}
