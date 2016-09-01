package com.zy.service.impl;

import com.zy.common.exception.BizException;
import com.zy.common.model.query.Page;
import com.zy.entity.sys.Area;
import com.zy.entity.sys.Area.AreaType;
import com.zy.entity.usr.Job;
import com.zy.entity.usr.Portrait;
import com.zy.entity.usr.User;
import com.zy.extend.Producer;
import com.zy.mapper.AreaMapper;
import com.zy.mapper.JobMapper;
import com.zy.mapper.PortraitMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.BizCode;
import com.zy.model.query.PortraitQueryModel;
import com.zy.service.PortraitService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.model.Constants.TOPIC_PORTRAIT_COMPLETED;


@Service
@Validated
public class PortraitServiceImpl implements PortraitService {

    @Autowired
    private PortraitMapper portraitMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AreaMapper areaMapper;

    @Autowired
    private Producer producer;

    @Autowired
    private JobMapper jobMapper;

    @Override
    public Portrait findOne(@NotNull(message = "Portrait id can not be null") Long id) {
        return portraitMapper.findOne(id);
    }

    //待生成代码
    @Override
    public Page<Portrait> findPage(@NotNull(message = "portraitQueryModel id can not be null") PortraitQueryModel portraitQueryModel) {
        if (portraitQueryModel.getPageNumber() == null)
            portraitQueryModel.setPageNumber(0);
        if (portraitQueryModel.getPageSize() == null)
            portraitQueryModel.setPageSize(20);
        long total = portraitMapper.count(portraitQueryModel);
        List<Portrait> data = portraitMapper.findAll(portraitQueryModel);
        Page<Portrait> page = new Page<>();
        page.setPageNumber(portraitQueryModel.getPageNumber());
        page.setPageSize(portraitQueryModel.getPageSize());
        page.setData(data);
        page.setTotal(total);
        return page;
    }

    @Override
    public Portrait findByUserId(@NotNull(message = "user id can not be null") Long userId) {
        return this.portraitMapper.findByUserId(userId);
    }

    @Override
    public Portrait create(@NotNull(message = "portrait can not be null") Portrait portrait) {
        validate(portrait);
        Job job = this.jobMapper.findOne(portrait.getJobId());
        if (job == null) throw new BizException(BizCode.ERROR, "job dose not exists,id=" + portrait.getJobId());
        checkUser(portrait.getUserId());

        Long areaId = portrait.getAreaId();
        Long hometownAreaId = portrait.getHometownAreaId();
        checkArea(areaId);
        checkArea(hometownAreaId);
        portraitMapper.insert(portrait);
        // 完善画像发送消息
        producer.send(TOPIC_PORTRAIT_COMPLETED, portrait);
        return portrait;
    }

    @Override
    public void update(@NotNull(message = "portrait can not be null") Portrait portrait) {

        Long id = portrait.getId();
        validate(id, NOT_NULL, "id is null ");
        Portrait persistence = portraitMapper.findOne(id);
        validate(persistence, NOT_NULL, "portrait id " + id + "not found");
        Job job = this.jobMapper.findOne(portrait.getId());
        if (job == null) throw new BizException(BizCode.ERROR, "job dose not exists,id=" + portrait.getJobId());

        Long areaId = portrait.getAreaId();
        Long hometownAreaId = portrait.getHometownAreaId();
        checkArea(areaId);
        checkArea(hometownAreaId);

        portrait.setUserId(persistence.getUserId());
        portraitMapper.update(portrait);

    }

    private void checkUser(@NotNull Long userId) {
        User user = userMapper.findOne(userId);
        validate(user, NOT_NULL, "user id " + userId + "not found");
    }

    private void checkArea(@NotNull Long areaId) {
        Area area = areaMapper.findOne(areaId);
        validate(area, NOT_NULL, "地区不存在");
        validate(area, v -> v.getAreaType() == AreaType.区, "必须选择到区");
    }

}
