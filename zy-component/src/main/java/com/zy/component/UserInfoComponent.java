package com.zy.component;

import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Arrays;

import com.zy.common.util.BeanUtils;
import com.zy.entity.usr.Job;
import com.zy.entity.usr.UserInfo;
import com.zy.entity.usr.User;
import com.zy.model.dto.AreaDto;
import com.zy.service.TagService;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.UserInfoAdminVo;
import com.zy.vo.UserInfoVo;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserInfoComponent {
	
	@Autowired
	private CacheComponent cacheComponent;
	
	@Autowired
	private TagService tagService;
	
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
		userInfoVo.setJobName(cacheComponent.getJob(userInfo.getJobId()).getJobName());
		userInfoVo.setTagNames(new ArrayList<String>(Arrays.stream(userInfo.getTagIds().split(",")).map(tag -> this.tagService.findById(Long.valueOf(tag)).getTagName()).collect(toSet())));
		userInfoVo.setBirthdayLabel(DateFormatUtils.format(userInfo.getBirthday(), "yyyy-MM-dd"));
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
		userInfoAdminVo.setTagNames(new ArrayList<String>(Arrays.stream(userInfo.getTagIds().split(",")).map(tag -> this.tagService.findById(Long.valueOf(tag)).getTagName()).collect(toSet())));
		userInfoAdminVo.setBirthdayLabel(DateFormatUtils.format(userInfo.getBirthday(), "yyyy-MM-dd"));
		return userInfoAdminVo;
	}
}
