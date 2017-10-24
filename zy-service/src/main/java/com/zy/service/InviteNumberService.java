package com.zy.service;

import com.zy.entity.sys.InviteNumber;
import com.zy.model.query.InviteNumberQueryModel;

import java.util.List;

/**
 * Created by it001 on 2017-10-24.
 */
public interface InviteNumberService {

    InviteNumber findOneByNumber(Long number);

    void update(InviteNumber inviteNumber);

    List<InviteNumber> findList(InviteNumberQueryModel inviteNumberQueryModel);
}
