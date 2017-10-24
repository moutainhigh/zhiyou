package com.zy.service.impl;

import com.zy.entity.sys.InviteNumber;
import com.zy.mapper.InviteNumberMapper;
import com.zy.mapper.JobMapper;
import com.zy.model.query.InviteNumberQueryModel;
import com.zy.service.InviteNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Created by it001 on 2017-10-24.
 */
@Service
@Validated
public class InviteNumberServiceImpl implements InviteNumberService {

    @Autowired
    private InviteNumberMapper inviteNumberMapper;

    /**
     * 按number查询 （number 不重复 只有一个）
     * @param number
     * @return
     */
    @Override
    public InviteNumber findOneByNumber(Long number) {
        List<InviteNumber> resultList = inviteNumberMapper.findAll(InviteNumberQueryModel.builder().inviteNumberEQ(number).build());
        return (resultList==null||resultList.isEmpty())?new InviteNumber():resultList.get(0);
    }

    /**
     * 更新 邀请码
     * @param inviteNumber
     */
    @Override
    public void update(InviteNumber inviteNumber) {
        inviteNumberMapper.update(inviteNumber);
    }

    /**
     * 条件查询
     * @param inviteNumberQueryModel
     * @return
     */
    @Override
    public List<InviteNumber> findList(InviteNumberQueryModel inviteNumberQueryModel) {
        return inviteNumberMapper.findAll(inviteNumberQueryModel);
    }
}
