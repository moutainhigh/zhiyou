package com.zy.service;

import com.zy.entity.sys.Notify;
import com.zy.model.query.NotifyQueryModel;

import java.util.List;

/**
 * Created by freeman on 16/9/14.
 */
public interface NotifyService {

	List<Notify> findAll(NotifyQueryModel notifyQueryModel);

	void sendSuccess(Long notifyId);

}
