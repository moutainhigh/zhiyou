package com.zy.service.impl;


import com.zy.common.model.query.Page;
import com.zy.entity.mergeusr.MergeUser;
import com.zy.entity.usr.User;
import com.zy.mapper.MergeUserMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.query.MergeUserQueryModel;
import com.zy.service.MergeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@Service
@Validated
public class MergeUserServiceImpl implements MergeUserService  {

    @Autowired
    private MergeUserMapper mergeUserMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public MergeUser findOne(Long id) {
        return mergeUserMapper.findOne(id);
    }

    @Override
    public MergeUser findByUserIdAndProductType( Long userId, Integer productType) {
        Map<String , Object> map = new HashMap<>();
        map.put("userId" , userId);
        map.put("productType" , productType);
        return mergeUserMapper.findByUserIdAndProductType(map);
    }

    @Override
    public void create(MergeUser mergeUser) {
        validate(mergeUser);
        mergeUserMapper.insert(mergeUser);
    }

    @Override
    public Page<MergeUser> findPage(MergeUserQueryModel mergeUserQueryModel) {
        if(mergeUserQueryModel.getPageNumber() == null)
            mergeUserQueryModel.setPageNumber(0);
        if(mergeUserQueryModel.getPageSize() == null)
            mergeUserQueryModel.setPageSize(20);
        long total = mergeUserMapper.count(mergeUserQueryModel);
        List<MergeUser> data = mergeUserMapper.findAll(mergeUserQueryModel);
        Page<MergeUser> page = new Page<>();
        page.setPageNumber(mergeUserQueryModel.getPageNumber());
        page.setPageSize(mergeUserQueryModel.getPageSize());
        page.setData(data);
        page.setTotal(total);
        return page;
    }

    @Override
    public List<MergeUser> findAll(MergeUserQueryModel mergeUserQueryModel) {
        return mergeUserMapper.findAll(mergeUserQueryModel);
    }

    @Override
    public void modifyParentId(Long id, Long parentId) {
        MergeUser mergeUser = findAndValidateMergeUser(id);
        mergeUser.setParentId(parentId);
        mergeUserMapper.merge(mergeUser,"parentId");
    }

    @Override
    public void modifyV4Id(Long id, Long v4Id) {
        MergeUser mergeUser = findAndValidateMergeUser(id);
        mergeUser.setParentId(v4Id);
        mergeUserMapper.merge(mergeUser,"v4Id");
    }

    @Override
    public long count(MergeUserQueryModel mergeUserQueryModel) {
        return 0;
    }

    private User findAndValidateUser(Long id) {
        validate(id, NOT_NULL, "id is null");
        User user = userMapper.findOne(id);
        validate(user, NOT_NULL, "product id" + id + " not found");
        return user;
    }

    private MergeUser findAndValidateMergeUser(Long id) {
        validate(id, NOT_NULL, "id is null");
        MergeUser mergeUser = mergeUserMapper.findOne(id);
        validate(mergeUser, NOT_NULL, "product id" + id + " not found");
        return mergeUser;
    }
}
