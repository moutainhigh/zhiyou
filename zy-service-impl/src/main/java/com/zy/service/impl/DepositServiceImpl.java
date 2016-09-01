package com.zy.service.impl;

import com.zy.ServiceUtils;
import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.component.FncComponent;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Deposit;
import com.zy.entity.fnc.PayType;
import com.zy.entity.fnc.Payment;
import com.zy.entity.fnc.AccountLog.InOut;
import com.zy.entity.fnc.Deposit.DepositStatus;
import com.zy.entity.usr.User;
import com.zy.mapper.DepositMapper;
import com.zy.mapper.PaymentMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.BizCode;
import com.zy.model.query.DepositQueryModel;
import com.zy.service.DepositService;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Validated
public class DepositServiceImpl implements DepositService {

	Logger logger = LoggerFactory.getLogger(DepositServiceImpl.class);

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private DepositMapper depositMapper;
	
	@Autowired
	private PaymentMapper paymentMapper;

	@Autowired
	private FncComponent fncComponent;

	@Override
	public Deposit create(@NotBlank String title, @NotNull CurrencyType currencyType1, @NotNull @DecimalMin("0.01") BigDecimal amount1, 
			CurrencyType currencyType2, BigDecimal amount2, @NotNull PayType payType, @NotNull Long userId, Long paymentId) {
		
		
		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + " is not found");
		
		validate(payType, v -> v != PayType.余额, "充值不支持余额支付");
		validate(currencyType1, v -> v != CurrencyType.积分, "积分不支持充值");
		
		if(paymentId != null) {
			Payment payment = paymentMapper.findOne(paymentId);
			validate(payment, NOT_NULL, "payment id " + paymentId + " is not found");
			validate(payment, v -> v.getUserId().equals(userId), "user id does not match");
		}
		
		BigDecimal totalAmount;
		if (currencyType2 != null) {
			validate(amount2, NOT_NULL, "amount 2 is null");
			validate(amount2, v -> v.compareTo(new BigDecimal("0.00")) > 0, "amount 2 must be greater than 0.00");
			validate(currencyType2, v -> v != currencyType1, "currency type 1 must not be the same with currency type 2");
			validate(currencyType2, v -> v != CurrencyType.积分, "积分不支持充值");
			totalAmount = amount1.add(amount2);
		} else {
			totalAmount = amount1;
		}
		
		Deposit deposit = new Deposit();
		deposit.setTitle(title);
		deposit.setPayType(payType);
		deposit.setCurrencyType1(currencyType1);
		deposit.setCurrencyType2(currencyType2);
		deposit.setAmount1(amount1);
		deposit.setAmount2(amount2);
		deposit.setTotalAmount(totalAmount);
		deposit.setUserId(userId);
		deposit.setPaymentId(paymentId);
		deposit.setSn(ServiceUtils.generateDepositSn());
		deposit.setDepositStatus(DepositStatus.待充值);
		deposit.setVersion(0);
		deposit.setCreatedTime(new Date());
		deposit.setIsOuterCreated(false);

		validate(deposit);
		depositMapper.insert(deposit);
		return deposit;
	}
	
	@Override
	public void success(Long id, String outerSn) {
		validate(id, NOT_NULL, "deposit is null");

		Deposit deposit = depositMapper.findOne(id);
		validate(deposit, NOT_NULL, "deposit id " + id + "is not found null");

		if (deposit.getDepositStatus() == DepositStatus.充值成功) {
			return; // 幂等处理
		} else if (deposit.getDepositStatus() == DepositStatus.已取消) {
			logger.warn("已取消充值单充值成功");
		}
		PayType payType = deposit.getPayType();
		deposit.setDepositStatus(DepositStatus.充值成功);
		deposit.setPaidTime(new Date());
		if (payType != PayType.微信 && payType != PayType.微信公众号 && StringUtils.isBlank(outerSn)) {
			throw new BizException(BizCode.ERROR, "outer sn参数缺失");
		}
		if (StringUtils.isNotBlank(outerSn)) {
			deposit.setOuterSn(outerSn);
		}
		if (depositMapper.update(deposit) == 0) {
			throw new ConcurrentException();
		}

		Long userId = deposit.getUserId();

		/* 充值记录流水 */ 
		CurrencyType currencyType1 = deposit.getCurrencyType1();
		CurrencyType currencyType2 = deposit.getCurrencyType2();
		
		fncComponent.recordAccountLog(userId, "充值成功", currencyType1, deposit.getAmount1(), InOut.收入, deposit);
		
		if (currencyType2 != null) {
			fncComponent.recordAccountLog(userId, "充值成功", currencyType2, deposit.getAmount2(), InOut.收入, deposit);
		}

	}

	@Override
	public void cancel(Long id) {
		validate(id, NOT_NULL, "deposit is null");
		Deposit deposit = depositMapper.findOne(id);
		validate(deposit, NOT_NULL, "deposit id " + id + "is not found null");
		DepositStatus depositStatus = deposit.getDepositStatus();
		if (depositStatus == DepositStatus.已取消) {
			return; // 幂等处理
		} else if (depositStatus != DepositStatus.待充值) {
			throw new BizException(BizCode.ERROR, "只有待充值状态的充值单才能取消");
		}
		deposit.setDepositStatus(DepositStatus.已取消);
		if (depositMapper.update(deposit) == 0) {
			throw new ConcurrentException();
		}
	}

	@Override
	public Page<Deposit> findPage(DepositQueryModel depositQueryModel) {
		validate(depositQueryModel, NOT_NULL, "deposit query model is null");
		if (depositQueryModel.getPageNumber() == null)
			depositQueryModel.setPageNumber(0);
		if (depositQueryModel.getPageSize() == null)
			depositQueryModel.setPageSize(20);
		long total = depositMapper.count(depositQueryModel);
		List<Deposit> data = depositMapper.findAll(depositQueryModel);
		Page<Deposit> page = new Page<>();
		page.setPageNumber(depositQueryModel.getPageNumber());
		page.setPageSize(depositQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public List<Deposit> findAll(DepositQueryModel depositQueryModel) {
		validate(depositQueryModel, NOT_NULL, "deposit query model is null");
		return depositMapper.findAll(depositQueryModel);
	}

	@Override
	public Deposit findOne(Long id) {
		validate(id, NOT_NULL, "deposit id is null");
		return depositMapper.findOne(id);
	}

	@Override
	public Deposit findBySn(String sn) {
		validate(sn, NOT_BLANK, "deposit sn is blank");
		return depositMapper.findBySn(sn);
	}

	@Override
	public void update(Deposit deposit) {
		validate(deposit);
		if (depositMapper.update(deposit) == 0) {
			throw new ConcurrentException();
		}
	}

}
