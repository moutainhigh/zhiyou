package com.zy.service.impl;

import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.model.query.Page;
import com.zy.entity.act.Activity;
import com.zy.entity.act.ActivityApply;
import com.zy.entity.act.ActivityCollect;
import com.zy.entity.act.ActivitySignIn;
import com.zy.entity.sys.Area;
import com.zy.entity.tour.BlackOrWhite;
import com.zy.entity.usr.User;
import com.zy.mapper.*;
import com.zy.model.BizCode;
import com.zy.model.query.ActivityQueryModel;
import com.zy.model.query.BlackOrWhiteQueryModel;
import com.zy.service.ActivityService;
import com.zy.service.BlackOrWhiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class BlackOrWhiteImpl implements BlackOrWhiteService {

	@Override
	public BlackOrWhite create(BlackOrWhite blackOrWhite) {
		return null;
	}

	@Override
	public void modify(BlackOrWhite blackOrWhite) {

	}

	@Override
	public BlackOrWhite findOne(Long BlackOrWhiteId) {
		return null;
	}

	@Override
	public Page<BlackOrWhite> findPage(BlackOrWhiteQueryModel blackOrWhiteQueryModel) {
		return null;
	}
}
