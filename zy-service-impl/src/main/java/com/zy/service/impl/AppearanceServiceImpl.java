package com.zy.service.impl;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.zy.common.exception.BizException;
import com.zy.common.exception.UnauthorizedException;
import com.zy.common.model.query.Page;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.Appearance;
import com.zy.entity.usr.User;
import com.zy.extend.Producer;
import com.zy.mapper.AppearanceMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.model.query.AppearanceQueryModel;
import com.zy.service.AppearanceService;

/**
 * Created by freeman on 16/7/15.
 */
@Service
@Validated
public class AppearanceServiceImpl implements AppearanceService {

	@Autowired
	private AppearanceMapper appearanceMapper;

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private Producer producer;

	@Override
	public Appearance findOne(@NotNull(message = "appearance id can not be null") Long id) {
		return this.appearanceMapper.findOne(id);
	}

	@Override
	public Page<Appearance> findPage(@NotNull(message = "appearanceQueryModel cannot be null") AppearanceQueryModel appearanceQueryModel) {
		if (appearanceQueryModel.getPageNumber() == null)
			appearanceQueryModel.setPageNumber(0);
		if (appearanceQueryModel.getPageSize() == null)
			appearanceQueryModel.setPageSize(20);
		long total = appearanceMapper.count(appearanceQueryModel);
		java.util.List<Appearance> data = appearanceMapper.findAll(appearanceQueryModel);
		Page<Appearance> page = new Page<>();
		page.setPageNumber(appearanceQueryModel.getPageNumber());
		page.setPageSize(appearanceQueryModel.getPageSize());
		page.setData(data);
		page.setTotal(total);
		return page;
	}

	@Override
	public Appearance findByUserId(@NotNull(message = "user id can not be null") Long userId) {
		return this.appearanceMapper.findByUserId(userId);
	}

	@Override
	public void confirm(@NotNull(message = "appearance id can not be null") Long id,
						@NotNull(message ="appearance isSuccess cannot be null" ) boolean isSuccess, String confirmRemark) {
		Appearance appearance = this.appearanceMapper.findOne(id);
		validate(appearance, NOT_NULL, "appearance is not exists");
		if (appearance.getConfirmStatus() == ConfirmStatus.已通过)
			throw new BizException(BizCode.ERROR, "实名认证已审核,不能再次审核");
		Appearance merge = new Appearance();
		merge.setId(id);
		if (!isSuccess) {
			validate(confirmRemark, NOT_NULL, "审核不通过时,备注必须填写");
			merge.setConfirmRemark(confirmRemark);
			merge.setConfirmStatus(ConfirmStatus.未通过);
			producer.send(Constants.TOPIC_APPEARANCE_REJECTED, appearance.getId());
		} else {
			merge.setConfirmRemark(confirmRemark);
			merge.setConfirmStatus(ConfirmStatus.已通过);
			merge.setConfirmedTime(new Date());
			// 通过审核发送消息
			producer.send(Constants.TOPIC_APPEARANCE_CONFIRMED, appearance.getId());
		}
		appearanceMapper.merge(merge, "confirmRemark", "confirmStatus", "confirmedTime");
	}

	@Override
	public Appearance create(@NotNull(message = "appearance cannot be null") Appearance appearance) {
		Long userId = appearance.getUserId();
		validate(userId, NOT_NULL, "user id is null");
		User user = userMapper.findOne(userId);
		validate(user, NOT_NULL, "user id[" + userId + "]not found");
		
		appearance.setAppliedTime(new Date());
		appearance.setConfirmStatus(ConfirmStatus.待审核);
		appearance.setConfirmedTime(null);
		appearance.setConfirmRemark(null);
		validate(appearance);
		appearanceMapper.insert(appearance);
		return appearance;
	}

	@Override
	public void update(@NotNull(message = "appearance cannot be null") Appearance appearance) {
		Long id = appearance.getId();
		validate(id, NOT_NULL, "id is null");
		
		Appearance persistence = appearanceMapper.findOne(id);
		validate(persistence, NOT_NULL, "persistence id[" + id + "]not found");
		if(persistence.getConfirmStatus() == ConfirmStatus.已通过) {
			throw new UnauthorizedException("实名认证已审核过,不能重复编辑");
		}
		
		persistence.setRealname(appearance.getRealname());
		persistence.setIdCardNumber(appearance.getIdCardNumber());
		persistence.setImage1(appearance.getImage1());
		persistence.setImage2(appearance.getImage2());
		persistence.setAppliedTime(new Date());
		persistence.setConfirmStatus(ConfirmStatus.待审核);
		persistence.setConfirmRemark(null);
		persistence.setConfirmedTime(null);
		validate(persistence);
		appearanceMapper.update(persistence);
	}

}
