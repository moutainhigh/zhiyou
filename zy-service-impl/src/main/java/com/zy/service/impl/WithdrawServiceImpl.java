package com.zy.service.impl;

import com.zy.Config;
import com.zy.ServiceUtils;
import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.component.FncComponent;
import com.zy.entity.fnc.AccountLog.InOut;
import com.zy.entity.fnc.Withdraw.WithdrawStatus;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.entity.usr.User.UserType;
import com.zy.entity.usr.WeixinUser;
import com.zy.extend.SKWxMpService;
import com.zy.model.BizCode;
import com.zy.model.Constants.ProfitBizName;
import com.zy.model.query.WithdrawQueryModel;
import com.zy.service.WithdrawService;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxRedpackResult;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;
import static me.chanjar.weixin.common.util.StringUtils.isNotBlank;

@Service
@Validated
public class WithdrawServiceImpl implements WithdrawService {

	@Autowired
	private com.zy.mapper.AccountMapper accountMapper;

	@Autowired
	private com.zy.mapper.UserMapper userMapper;

	@Autowired
	private com.zy.mapper.BankCardMapper bankCardMapper;

	@Autowired
	private com.zy.mapper.WithdrawMapper withdrawMapper;

	@Autowired
	private com.zy.mapper.WeixinUserMapper weixinUserMapper;


	@Autowired
	private FncComponent fncComponent;
	
	@Autowired
	private Config config;

    @Autowired
    private SKWxMpService skWxMpService;

    @Override
    public com.zy.entity.fnc.Withdraw create(@NotNull Long userId, @NotNull com.zy.entity.fnc.CurrencyType currencyType, @NotNull @DecimalMin("0.01") BigDecimal amount) {
        final BigDecimal zero = new BigDecimal("0.00");
        validate(userId, NOT_NULL, "user id is null");
        validate(currencyType, NOT_NULL, "currency type is null");
        validate(amount, NOT_NULL, "amount is null");
        validate(amount, v -> v.compareTo(zero) >= 0, currencyType + "amount must not be less than 0.00");

		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + " is not found");
		com.zy.entity.fnc.Account account = accountMapper.findByUserIdAndCurrencyType(userId, currencyType);
		validate(account, NOT_NULL, "account user id " + userId + " currency type " + currencyType + " is not found");
 
		if(currencyType.equals(com.zy.entity.fnc.CurrencyType.金币)) {
			if(UserRank.V2.compareTo(user.getUserRank()) > 0) {
				throw new BizException(BizCode.ERROR, "提现试客币,需要用户等级达到铜牌及以上");
			}
		}
		
		/* check 提现币种 */
		UserType userType = user.getUserType();
		Boolean isToBankCard = null;
		Long bankCardId = null;
		String openId = null;
		if (userType == UserType.商家) {
			if(currencyType != com.zy.entity.fnc.CurrencyType.现金) {
				throw new BizException(BizCode.ERROR, "提现种类不匹配");
			}
			com.zy.entity.fnc.BankCard bankCard = bankCardMapper.findByUserId(userId);
			if (bankCard == null) {
				throw new BizException(BizCode.ERROR, "必须先绑定银行卡才能提现");
			}
			bankCardId = bankCard.getId();
			isToBankCard = false;
		} else if (userType == UserType.代理) {
			if(currencyType != com.zy.entity.fnc.CurrencyType.现金 && currencyType != com.zy.entity.fnc.CurrencyType.金币) {
				throw new BizException(BizCode.ERROR, "提现种类不匹配");
			}
			WeixinUser weixinUser = weixinUserMapper.findByUserId(userId);
			if (weixinUser == null) {
				throw new BizException(BizCode.ERROR, "必须先绑定微信才能提现");
			}
			openId = weixinUser.getOpenId();
			isToBankCard = true;
		} else {
			throw new BizException(BizCode.ERROR, "提现用户不匹配");
		}

		/* check 余额 */
		BigDecimal beforeAmount = account.getAmount();
		BigDecimal afterAmount = beforeAmount.subtract(amount);
		if (afterAmount.compareTo(zero) < 0) {
			throw new BizException(BizCode.ERROR, "提现申请失败,余额不足");
		}
		Date date = new Date();
		BigDecimal feeRate = config.getWithdrawFeeRate(userType, currencyType);

		BigDecimal fee = amount.multiply(feeRate).setScale(2, RoundingMode.HALF_UP);
		BigDecimal realAmount = amount.subtract(fee);

		com.zy.entity.fnc.Withdraw withdraw = new com.zy.entity.fnc.Withdraw();
		withdraw.setCreatedTime(date);
		withdraw.setCurrencyType(currencyType);
		withdraw.setAmount(amount);
		withdraw.setFeeRate(feeRate);
		withdraw.setFee(fee);
		withdraw.setRealAmount(realAmount);
		withdraw.setAmount(amount);
		withdraw.setSn(ServiceUtils.generateWithdrawSn());
		withdraw.setUserId(userId);
		withdraw.setVersion(0);
		withdraw.setIsToBankCard(isToBankCard);
		withdraw.setBankCardId(bankCardId);
		withdraw.setOpenId(openId);
		withdraw.setTitle("提现");
		withdraw.setWithdrawStatus(WithdrawStatus.已申请);
		validate(withdraw);
		withdrawMapper.insert(withdraw);
		
		String currencyTypeLabel = currencyType == com.zy.entity.fnc.CurrencyType.金币 ? "试客币" : (currencyType == com.zy.entity.fnc.CurrencyType.现金 ? "本金" : currencyType.getAlias());
		fncComponent.recordAccountLog(userId, currencyTypeLabel +  "提现", currencyType, amount, InOut.支出, withdraw);
		fncComponent.recordAccountLog(config.getSysUserId(), currencyTypeLabel + "提现", currencyType, amount, InOut.收入, withdraw);
		return withdraw;
	}

	@Override
	public void success(@NotNull Long id, @NotNull Long operatorUserId, @NotBlank String remark) {

		final BigDecimal zero = new BigDecimal("0.00");
		com.zy.entity.fnc.Withdraw withdraw = withdrawMapper.findOne(id);
		validate(withdraw, NOT_NULL, "withdraw id " + id + " is not found null");
		User user = userMapper.findOne(operatorUserId);
		validate(user, NOT_NULL, "operator user id" + operatorUserId + " is not found null");

        if (withdraw.getWithdrawStatus() == WithdrawStatus.提现成功) {
            return; // 幂等处理
        } else if (withdraw.getWithdrawStatus() != WithdrawStatus.已申请) {
            throw new BizException(BizCode.ERROR, "只有已申请状态的提现单可以确认成功");
        }
        if (!withdraw.getIsToBankCard()) {
            try {
                WxRedpackResult result = this.skWxMpService.transfer(withdraw.getSn(), withdraw.getOpenId(), withdraw.getAmount(), withdraw.getTitle());
                if (isNotBlank(result.getReturnMsg())) {
                    throw new BizException(BizCode.ERROR, result.getReturnMsg());
                }
            } catch (WxErrorException e) {
                throw new BizException(BizCode.ERROR, e.getMessage());
            }
        }

		withdraw.setRemark(remark);
		withdraw.setOperatorUserId(operatorUserId);
		withdraw.setWithdrawStatus(WithdrawStatus.提现成功);
		withdraw.setWithdrawedTime(new Date());
		if (withdrawMapper.update(withdraw) == 0) {
			throw new ConcurrentException();
		}
		com.zy.entity.fnc.CurrencyType currencyType = withdraw.getCurrencyType();
		Long sysUserId = config.getSysUserId();

		fncComponent.recordAccountLog(sysUserId, "提现出账", currencyType, withdraw.getRealAmount(), InOut.支出, withdraw);
		
		Long feeUserId = config.getFeeUserId();
		BigDecimal fee = withdraw.getFee();
		if (fee.compareTo(zero) > 0) {
			String title = "提现单" + withdraw.getSn() + "收益";
			com.zy.entity.fnc.Profit profit = fncComponent.createProfit(ProfitBizName.平台收益, currencyType, title, feeUserId, fee, null);
			fncComponent.recordAccountLog(sysUserId, title, currencyType, fee, InOut.支出, profit);
			fncComponent.recordAccountLog(feeUserId, title, currencyType, fee, InOut.收入, profit);
		}
	}

	@Override
	public void cancel(Long id, Long operatorUserId, String remark) {
		validate(id, NOT_NULL, "withdraw is null");
		validate(operatorUserId, NOT_NULL, "operator user id is null");
		validate(remark, NOT_BLANK, "remark is blank");

		com.zy.entity.fnc.Withdraw withdraw = withdrawMapper.findOne(id);
		validate(withdraw, NOT_NULL, "withdraw id " + id + " is not found null");
		User user = userMapper.findOne(operatorUserId);
		validate(user, NOT_NULL, "operator user id" + operatorUserId + " is not found null");

		if (withdraw.getWithdrawStatus() == WithdrawStatus.已取消) {
			return; // 幂等处理
		} else if (withdraw.getWithdrawStatus() != WithdrawStatus.已申请) {
			throw new BizException(BizCode.ERROR, "只有已申请状态的提现单可以取消");
		}

		withdraw.setRemark(remark);
		withdraw.setOperatorUserId(operatorUserId);
		withdraw.setWithdrawStatus(WithdrawStatus.已取消);
		if (withdrawMapper.update(withdraw) == 0) {
			throw new ConcurrentException();
		}
		com.zy.entity.fnc.CurrencyType currencyType = withdraw.getCurrencyType();
		
		BigDecimal amount = withdraw.getAmount();
		
		fncComponent.recordAccountLog(withdraw.getUserId(), "提现取消退款", currencyType, amount, InOut.收入, withdraw);
		fncComponent.recordAccountLog(config.getSysUserId(), "提现取消退款", currencyType, amount, InOut.支出, withdraw);
	}

	@Override
	public Page<com.zy.entity.fnc.Withdraw> findPage(@NotNull WithdrawQueryModel withdrawQueryModel) {
		if (withdrawQueryModel.getPageNumber() == null)
			withdrawQueryModel.setPageNumber(0);
		if (withdrawQueryModel.getPageSize() == null)
			withdrawQueryModel.setPageSize(20);
		long total = withdrawMapper.count(withdrawQueryModel);
		List<com.zy.entity.fnc.Withdraw> data = withdrawMapper.findAll(withdrawQueryModel);
		Page<com.zy.entity.fnc.Withdraw> page = new Page<>();
		page.setPageNumber(withdrawQueryModel.getPageNumber());
		page.setPageSize(withdrawQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public com.zy.entity.fnc.Withdraw findOne(@NotNull Long id) {
		return withdrawMapper.findOne(id);
	}

	@Override
	public List<com.zy.entity.fnc.Withdraw> findAll(@NotNull WithdrawQueryModel withdrawQueryModel) {
		return withdrawMapper.findAll(withdrawQueryModel);
	}

	@Override
	public long count(@NotNull WithdrawQueryModel withdrawQueryModel) {
		return withdrawMapper.count(withdrawQueryModel);
	}

}
