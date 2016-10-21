package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.usr.Job;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.model.dto.AreaDto;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.UserInfoAdminVo;
import com.zy.vo.UserInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserInfoComponent {

    Logger logger = LoggerFactory.getLogger(UserInfoComponent.class);

    @Autowired
    private CacheComponent cacheComponent;

    private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm";

    public UserInfoVo buildVo(UserInfo userInfo) {

        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo, userInfoVo);

        String idCardNumber = userInfo.getIdCardNumber();
        userInfoVo.setIdCardNumber(StringUtils.overlay(idCardNumber, "***********", 6, idCardNumber.length()));

        String image1 = userInfo.getImage1();
        String image2 = userInfo.getImage2();

        userInfoVo.setImage1Thumbnail(StringUtils.isBlank(image1) ? null : GcUtils.getThumbnail(image1, 240, 150));
        userInfoVo.setImage2Thumbnail(StringUtils.isBlank(image2) ? null : GcUtils.getThumbnail(image2, 240, 150));

        logger.error( "user info  " + userInfo );



        Long areaId = userInfo.getAreaId();
        if (areaId != null) {
            AreaDto areaDto = cacheComponent.getAreaDto(areaId);
            if (areaDto != null) {
                userInfoVo.setProvince(areaDto.getProvince());
                userInfoVo.setCity(areaDto.getCity());
                userInfoVo.setDistrict(areaDto.getDistrict());
            } else {
                logger.error("area id " + areaId + " is not found");
            }
        }

        Long jobId = userInfo.getJobId();
        if (jobId != null) {
            Job job = cacheComponent.getJob(userInfo.getJobId());
            if (job != null) {
                userInfoVo.setJobName(job.getJobName());
            }
        }

        String tagIds = userInfo.getTagIds();
        if (StringUtils.isNotBlank(tagIds)) {
            List<String> tagNames = Arrays.stream(StringUtils.split(tagIds, ","))
                    .filter(v -> StringUtils.isNotBlank(v))
                    .map(v -> Long.valueOf(v))
                    .map(v -> cacheComponent.getTag(v))
                    .filter(v -> v != null)
                    .map(v -> v.getTagName())
                    .collect(Collectors.toList());
            userInfoVo.setTagNames(tagNames);
        }

        userInfoVo.setBirthdayLabel(GcUtils.formatDate(userInfo.getBirthday(), "yyyy-MM-dd"));

        return userInfoVo;
    }

    public UserInfoAdminVo buildAdminVo(UserInfo userInfo) {
        UserInfoAdminVo userInfoAdminVo = new UserInfoAdminVo();
        BeanUtils.copyProperties(userInfo, userInfoAdminVo);
        User user = cacheComponent.getUser(userInfo.getUserId());
        userInfoAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(user));
        userInfoAdminVo.setImage1Thumbnail(GcUtils.getThumbnail(userInfo.getImage1(), 240, 150));
        userInfoAdminVo.setImage2Thumbnail(GcUtils.getThumbnail(userInfo.getImage2(), 240, 150));
        userInfoAdminVo.setAppliedTimeLabel(GcUtils.formatDate(userInfo.getAppliedTime(), TIME_PATTERN));
        userInfoAdminVo.setConfirmedTimeLabel(GcUtils.formatDate(userInfo.getConfirmedTime(), TIME_PATTERN));
        userInfoAdminVo.setConfirmStatusStyle(GcUtils.getConfirmStatusStyle(userInfo.getConfirmStatus()));
        userInfoAdminVo.setBirthdayLabel(GcUtils.formatDate(userInfo.getBirthday(), "yyyy-MM-dd"));

        Long areaId = userInfo.getAreaId();
        if (areaId != null) {
            AreaDto areaDto = cacheComponent.getAreaDto(areaId);
            if (areaDto != null) {
                userInfoAdminVo.setProvince(areaDto.getProvince());
                userInfoAdminVo.setCity(areaDto.getCity());
                userInfoAdminVo.setDistrict(areaDto.getDistrict());
            }
        }

        Long jobId = userInfo.getJobId();
        if (jobId != null) {
            Job job = cacheComponent.getJob(jobId);
            if (job != null) {
                userInfoAdminVo.setJobName(job.getJobName());
            }
        }

        String tagIds = userInfo.getTagIds();
        if (StringUtils.isNotBlank(tagIds)) {
            List<String> tagNames = Arrays.stream(StringUtils.split(tagIds, ","))
                    .filter(v -> StringUtils.isNotBlank(v))
                    .map(v -> Long.valueOf(v))
                    .map(v -> cacheComponent.getTag(v))
                    .filter(v -> v != null)
                    .map(v -> v.getTagName())
                    .collect(Collectors.toList());
            userInfoAdminVo.setTagNames(tagNames);
        }

        userInfoAdminVo.setBirthdayLabel(GcUtils.formatDate(userInfo.getBirthday(), "yyyy-MM-dd"));
        return userInfoAdminVo;
    }
}
