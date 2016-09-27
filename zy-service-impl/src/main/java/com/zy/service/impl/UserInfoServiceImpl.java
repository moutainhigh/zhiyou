package com.zy.service.impl;

import com.zy.common.exception.BizException;
import com.zy.common.model.query.Page;
import com.zy.entity.sys.Area;
import com.zy.entity.sys.Area.AreaType;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.Job;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.extend.Producer;
import com.zy.mapper.AreaMapper;
import com.zy.mapper.JobMapper;
import com.zy.mapper.UserInfoMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.model.query.UserInfoQueryModel;
import com.zy.service.UserInfoService;
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
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AreaMapper areaMapper;

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private Producer producer;

    @Override
    public UserInfo findOne(@NotNull Long id) {
        return userInfoMapper.findOne(id);
    }

    @Override
    public Page<UserInfo> findPage(@NotNull UserInfoQueryModel userInfoQueryModel) {
        if (userInfoQueryModel.getPageNumber() == null)
            userInfoQueryModel.setPageNumber(0);
        if (userInfoQueryModel.getPageSize() == null)
            userInfoQueryModel.setPageSize(20);
        long total = userInfoMapper.count(userInfoQueryModel);
        List<UserInfo> data = userInfoMapper.findAll(userInfoQueryModel);
        Page<UserInfo> page = new Page<>();
        page.setPageNumber(userInfoQueryModel.getPageNumber());
        page.setPageSize(userInfoQueryModel.getPageSize());
        page.setData(data);
        page.setTotal(total);
        return page;
    }

    @Override
    public UserInfo findByUserId(@NotNull Long userId) {
        return this.userInfoMapper.findByUserId(userId);
    }

    @Override
    public UserInfo create(@NotNull UserInfo userInfo) {
        validate(userInfo);

        Long userId = userInfo.getUserId();
        checkUser(userId);

        Long jobId = userInfo.getJobId();
        checkJob(jobId);

        Long areaId = userInfo.getAreaId();
        checkArea(areaId);

        userInfoMapper.insert(userInfo);
        return userInfo;
    }

    @Override
    public void modify(@NotNull UserInfo userInfo) {

        Long id = userInfo.getId();
        validate(id, NOT_NULL, "user info id is null");
        UserInfo persistence = userInfoMapper.findOne(id);
        validate(persistence, NOT_NULL, "user info id " + id + " is not found");

        if(persistence.getConfirmStatus() == ConfirmStatus.已通过) {
            throw new BizException(BizCode.ERROR, "实名认证已审核过,不能编辑");
        }

        Long jobId = userInfo.getJobId();
        checkJob(jobId);

        Long areaId = userInfo.getAreaId();
        checkArea(areaId);

        persistence.setAreaId(areaId);
        persistence.setJobId(jobId);
        persistence.setGender(userInfo.getGender());
        persistence.setBirthday(userInfo.getBirthday());
        persistence.setHometownAreaId(userInfo.getHometownAreaId());
        persistence.setTagIds(userInfo.getTagIds());
        persistence.setConsumptionLevel(userInfo.getConsumptionLevel());


        persistence.setRealname(userInfo.getRealname());
        persistence.setIdCardNumber(userInfo.getIdCardNumber());
        persistence.setImage1(userInfo.getImage1());
        persistence.setImage2(userInfo.getImage2());
        persistence.setAppliedTime(new Date());
        persistence.setConfirmStatus(ConfirmStatus.待审核);
        persistence.setConfirmRemark(null);
        persistence.setConfirmedTime(null);

        validate(persistence);

        userInfoMapper.update(persistence);

    }

    @Override
    public long count(@NotNull UserInfoQueryModel userInfoQueryModel) {
        return userInfoMapper.count(userInfoQueryModel);
    }

    @Override
    public void confirm(@NotNull Long id,
                        @NotNull boolean isSuccess, String confirmRemark) {
        UserInfo userInfo = this.userInfoMapper.findOne(id);
        validate(userInfo, NOT_NULL, "userInfo is not exists");
        if (userInfo.getConfirmStatus() == ConfirmStatus.已通过)
            throw new BizException(BizCode.ERROR, "实名认证已审核,不能再次审核");
        UserInfo merge = new UserInfo();
        merge.setId(id);
        if (!isSuccess) {
            validate(confirmRemark, NOT_NULL, "审核不通过时,备注必须填写");
            merge.setConfirmRemark(confirmRemark);
            merge.setConfirmStatus(ConfirmStatus.未通过);
            producer.send(Constants.TOPIC_USER_INFO_REJECTED, userInfo.getId());
        } else {
            merge.setConfirmRemark(confirmRemark);
            merge.setConfirmStatus(ConfirmStatus.已通过);
            merge.setConfirmedTime(new Date());
            // 通过审核发送消息
            producer.send(Constants.TOPIC_USER_INFO_CONFIRMED, userInfo.getId());
        }
        userInfoMapper.merge(merge, "confirmRemark", "confirmStatus", "confirmedTime");
    }

    private void checkUser(@NotNull Long userId) {
        User user = userMapper.findOne(userId);
        validate(user, NOT_NULL, "user id " + userId + "not found");
    }

    private void checkJob(@NotNull Long jobId) {
        Job job = jobMapper.findOne(jobId);
        validate(job, NOT_NULL, "job id " + jobId + " is not found");
    }

    private void checkArea(@NotNull Long areaId) {
        Area area = areaMapper.findOne(areaId);
        validate(area, NOT_NULL, "地区不存在");
        validate(area, v -> v.getAreaType() == AreaType.区, "必须选择到区");
    }

}
