package com.zy.component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.usr.Job;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.model.dto.AreaDto;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.UserInfoAdminVo;
import com.zy.vo.UserInfoVo;

@Component
public class UserInfoComponent {
	
	@Autowired
	private CacheComponent cacheComponent;
	
	public UserInfoVo buildVo(UserInfo userInfo) {
		UserInfoVo userInfoVo = new UserInfoVo();
		BeanUtils.copyProperties(userInfo, userInfoVo);
		
		String idCardNumber = userInfo.getIdCardNumber();
		userInfoVo.setIdCardNumber(StringUtils.overlay(idCardNumber, "***********", 6, idCardNumber.length()));
		
		String image1 = userInfo.getImage1();
		String image2 = userInfo.getImage2();
		
		userInfoVo.setImage1Thumbnail(StringUtils.isBlank(image1)? null : GcUtils.getThumbnail(image1, 120, 120));
		userInfoVo.setImage2Thumbnail(StringUtils.isBlank(image2)? null : GcUtils.getThumbnail(image2, 120, 120));
		
		AreaDto areaDto = cacheComponent.getAreaDto(userInfo.getAreaId());
		userInfoVo.setProvince(areaDto.getProvince());
		userInfoVo.setCity(areaDto.getCity());
		userInfoVo.setDistrict(areaDto.getDistrict());
		
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
		userInfoAdminVo.setImage1Thumbnail(GcUtils.getThumbnail(userInfo.getImage1(), 120, 120));
		userInfoAdminVo.setImage2Thumbnail(GcUtils.getThumbnail(userInfo.getImage2(), 120, 120));

		AreaDto areaDto = cacheComponent.getAreaDto(userInfo.getAreaId());
		if (areaDto != null) {
			userInfoAdminVo.setProvince(areaDto.getProvince());
			userInfoAdminVo.setCity(areaDto.getCity());
			userInfoAdminVo.setDistrict(areaDto.getDistrict());
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
