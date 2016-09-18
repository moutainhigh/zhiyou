package com.zy.service.impl;

import com.zy.entity.sys.Notify;
import com.zy.mapper.NotifyMapper;
import com.zy.model.query.NotifyQueryModel;
import com.zy.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by freeman on 16/9/14.
 */
@Service
@Validated
public class NotifyServiceImpl implements NotifyService {

	@Autowired
	private NotifyMapper notifyMapper;

	@Override
	public List<Notify> findAll(@NotNull NotifyQueryModel notifyQueryModel) {
		return notifyMapper.findAll(notifyQueryModel);
	}

	@Override
	public void sendSuccess(@NotNull Long notifyId) {
		final Notify notify = this.notifyMapper.findOne(notifyId);
		Objects.requireNonNull(notify);
		notify.setSentTime(new Date());
		notify.setIsSent(true);
		this.notifyMapper.merge(notify,"sentTime","isSent");
	}
}
