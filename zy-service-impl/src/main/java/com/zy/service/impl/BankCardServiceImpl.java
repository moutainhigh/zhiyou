package com.zy.service.impl;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.zy.common.exception.BizException;
import com.zy.common.model.query.Page;
import com.zy.entity.fnc.Bank;
import com.zy.entity.fnc.BankCard;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.User;
import com.zy.mapper.BankCardMapper;
import com.zy.mapper.BankMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.BizCode;
import com.zy.model.query.BankCardQueryModel;
import com.zy.service.BankCardService;

@Service
@Validated
public class BankCardServiceImpl implements BankCardService {

	@Autowired
	private BankCardMapper bankCardMapper;

	@Autowired
	private BankMapper bankMapper;
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public BankCard findOne(@NotNull Long id) {
		return bankCardMapper.findOne(id);
	}

	@Override
	public List<BankCard> findByUserId(@NotNull Long userId) {
		BankCardQueryModel bankCardQueryModel = new BankCardQueryModel();
		bankCardQueryModel.setUserIdEQ(userId);
		List<BankCard> bankCards = bankCardMapper.findAll(bankCardQueryModel);
		return bankCards;
	}

	@Override
	public BankCard create(@NotNull BankCard bankCard) {
		Long userId = bankCard.getUserId();
		validate(userId, NOT_NULL, "user id is null");
		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id " + userId + " is not found");
		
		Long bankId = bankCard.getBankId();
		validate(bankId, NOT_NULL, "bank id is null");
		Bank bank = bankMapper.findOne(bankId);
		validate(bank, NOT_NULL, "bank id" + bankId + " not found");
		if(bank.getIsDeleted()) {
			throw new BizException(BizCode.ERROR, "当前银行不存在");
		}

		bankCard.setId(null);
		bankCard.setConfirmStatus(ConfirmStatus.待审核);
		bankCard.setAppliedTime(new Date());
		bankCard.setIsDeleted(false);
		validate(bankCard);
		bankCardMapper.insert(bankCard);
		return bankCard;
	}

	@Override
	public void update(@NotNull BankCard bankCard) {
		Long id = bankCard.getId();
		validate(id, NOT_NULL, "bank card id is null");
		BankCard persistence = bankCardMapper.findOne(id);
		validate(persistence, NOT_NULL, "bank card id" + id + "is not found");
		if (persistence.getConfirmStatus() == ConfirmStatus.已通过) {
			throw new BizException(BizCode.ERROR, "已审核通过，不允许修改，如需修改，请联系客服！");
		} else {
			Boolean isDefault = bankCard.getIsDefault();
			if (isDefault) {
				List<BankCard> persistentBankCards = bankCardMapper.findAll(BankCardQueryModel.builder().userIdEQ(persistence.getUserId()).build());
				for(BankCard persistentBankCard : persistentBankCards) {
					if (persistentBankCard.getIsDefault()) {
						persistentBankCard.setIsDefault(false);
						bankCardMapper.update(persistentBankCard);
					}
				}
			}
			
			persistence.setRealname(bankCard.getRealname());
			persistence.setCardNumber(bankCard.getCardNumber());
			persistence.setBankName(bankCard.getBankName());
			persistence.setBankBranchName(bankCard.getBankBranchName());
			persistence.setConfirmStatus(ConfirmStatus.待审核);
			persistence.setConfirmedTime(new Date());
			persistence.setIsDefault(isDefault);
			validate(persistence);
			bankCardMapper.update(persistence);
		}
	}
	
	@Override
	public void delete(@NotNull Long id) {
		BankCard bankCard = bankCardMapper.findOne(id);
		validate(bankCard, NOT_NULL, "address id " + id + " is not found");
		BankCard bankCardForMerge = new BankCard();
		bankCardForMerge.setId(id);
		bankCardForMerge.setIsDeleted(true);
		bankCardForMerge.setIsDefault(false);
		bankCardMapper.merge(bankCardForMerge, "isDeleted", "isDefault");
	}

	@Override
	public Page<BankCard> findPage(@NotNull BankCardQueryModel bankCardQueryModel) {
		if (bankCardQueryModel.getPageNumber() == null)
			bankCardQueryModel.setPageNumber(0);
		if (bankCardQueryModel.getPageSize() == null)
			bankCardQueryModel.setPageSize(20);
		long total = bankCardMapper.count(bankCardQueryModel);
		List<BankCard> data = bankCardMapper.findAll(bankCardQueryModel);
		Page<BankCard> page = new Page<>();
		page.setPageNumber(bankCardQueryModel.getPageNumber());
		page.setPageSize(bankCardQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public long count(@NotNull BankCardQueryModel bankCardQueryModel) {
		return bankCardMapper.count(bankCardQueryModel);
	}

	@Override
	public void confirm(@NotNull Long id,@NotNull boolean isSuccess, String confirmRemark) {
		validate(id, NOT_NULL, "id can not be null");
		BankCard bankCard = bankCardMapper.findOne(id);
		validate(bankCard, NOT_NULL, "appearance is not exists");
		if (bankCard.getConfirmStatus() == ConfirmStatus.已通过)
			throw new BizException(BizCode.ERROR, "已审核,不能再次审核");
		BankCard merge = new BankCard();
		merge.setId(id);
		if (!isSuccess) {
			validate(confirmRemark, NOT_NULL, "审核不通过时,备注必须填写");
			merge.setConfirmRemark(confirmRemark);
			merge.setConfirmStatus(ConfirmStatus.未通过);
		} else {
			merge.setConfirmStatus(ConfirmStatus.已通过);
			merge.setConfirmedTime(new Date());
		}
		bankCardMapper.merge(merge, "confirmStatus", "confirmRemark", "confirmedTime");
	}

}
