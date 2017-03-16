package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.act.Activity;
import com.zy.entity.act.ActivityApply;
import com.zy.entity.usr.User;
import com.zy.model.dto.AreaDto;
import com.zy.model.query.ActivityApplyQueryModel;
import com.zy.model.query.ActivityCollectQueryModel;
import com.zy.model.query.ActivitySignInQueryModel;
import com.zy.service.ActivityApplyService;
import com.zy.service.ActivityCollectService;
import com.zy.service.ActivitySignInService;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.zy.util.GcUtils.formatDate;
import static com.zy.util.GcUtils.getThumbnail;
import static java.util.Objects.isNull;

@Component
public class ActivityComponent {

	@Autowired
	private CacheComponent cacheComponent;

	@Autowired
	private ActivityApplyService activityApplyService;

	@Autowired
	private ActivityCollectService activityCollectService;

	@Autowired
	private UserService userService;

	@Autowired
	private ActivitySignInService activitySignInService;
	
	private static final String SIMPLE_TIME_PATTERN = "M月d日 HH:mm";

	public ActivityAdminFullVo buildAdminFullVo(Activity activity) {
		String shortFmt = "yyyy年M月d日 HH:mm";
		ActivityAdminFullVo activityAdminFullVo = new ActivityAdminFullVo();
		BeanUtils.copyProperties(activity, activityAdminFullVo);

		activityAdminFullVo.setAmountLabel(GcUtils.formatCurreny(activity.getAmount()));
		Long activityId = activity.getId();
		Long areaId = activity.getAreaId();
		if(areaId != null) {
			AreaDto areaDto = cacheComponent.getAreaDto(activity.getAreaId());
			if (areaDto != null) {
				activityAdminFullVo.setProvince(areaDto.getProvince());
				activityAdminFullVo.setCity(areaDto.getCity());
				activityAdminFullVo.setDistrict(areaDto.getDistrict());
			}
		}
		activityAdminFullVo.setImageBig(getThumbnail(activity.getImage(), 300, 180));
		activityAdminFullVo.setImageThumbnail(getThumbnail(activity.getImage(), 150, 90));

		Date applyDeadline = activity.getApplyDeadline();  //报名截止时间
		Date startTime = activity.getStartTime();
		Date endTime = activity.getEndTime();
		if(applyDeadline != null) {
			activityAdminFullVo.setApplyDeadlineLabel(formatDate(applyDeadline, shortFmt));
		}
		if(startTime != null) {
			activityAdminFullVo.setStartTimeLabel(formatDate(startTime, shortFmt));
		}
		if(endTime != null) {
			activityAdminFullVo.setEndTimeLabel(formatDate(endTime, shortFmt));
		}
		activityAdminFullVo.setStatus(buildStatus(applyDeadline, startTime, endTime));

		activityAdminFullVo.setActivityApplies(
				activityApplyService.findAll(ActivityApplyQueryModel.builder().activityIdEQ(activityId).orderBy("appliedTime").build())
						.stream().map(activityApply -> {
					ActivityApplyAdminVo activityApplyAdminVo = new ActivityApplyAdminVo();
					BeanUtils.copyProperties(activityApply, activityApplyAdminVo);
					Long userId = activityApply.getUserId();
					User user = cacheComponent.getUser(userId);
					activityApplyAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(user));
					Long inviterId = user.getInviterId();
					if (inviterId != null) {
						activityApplyAdminVo.setInviter(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(inviterId)));
					}
					activityApplyAdminVo.setAppliedTimeLabel(formatDate(activityApply.getAppliedTime(), shortFmt));
					return activityApplyAdminVo;
				}).collect(Collectors.toList()));

		activityAdminFullVo.setActivityCollects(
				activityCollectService.findAll(ActivityCollectQueryModel.builder().activityIdEQ(activityId).orderBy("collectedTime").build())
						.stream().map(activityCollect -> {
					ActivityCollectAdminVo activityCollectAdminVo = new ActivityCollectAdminVo();
					BeanUtils.copyProperties(activityCollect, activityCollectAdminVo);
					Long userId = activityCollect.getUserId();
					activityCollectAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(userId)));
					activityCollectAdminVo.setCollectedTimeLabel(formatDate(activityCollect.getCollectedTime(), shortFmt));
					return activityCollectAdminVo;
				}).collect(Collectors.toList()));

		activityAdminFullVo.setActivitySignIns(
				activitySignInService.findAll(ActivitySignInQueryModel.builder().activityIdEQ(activityId).orderBy("signedInTime").build())
						.stream().map(activitySignIn -> {
					ActivitySignInAdminVo activitySignInAdminVo = new ActivitySignInAdminVo();
					BeanUtils.copyProperties(activity, activitySignInAdminVo);
					Long userId = activitySignIn.getUserId();
					activitySignInAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(userId)));
					System.out.println();
					activitySignInAdminVo.setSignedInTimeLabel(formatDate(activitySignIn.getSignedInTime(), shortFmt));
					return activitySignInAdminVo;
				}).collect(Collectors.toList()));

		return activityAdminFullVo;
	}

	public ActivityAdminVo buildAdminVo(Activity activity, boolean withDetail) {
		String fullFmt = "yyyy-MM-dd HH:mm:ss";
		String shortFmt = "M月d日 HH:mm";
		ActivityAdminVo activityAdminVo = new ActivityAdminVo();
		BeanUtils.copyProperties(activity, activityAdminVo, "detail");
		activityAdminVo.setAmountLabel(GcUtils.formatCurreny(activity.getAmount()));

		if (withDetail) {
			activityAdminVo.setDetail(activity.getDetail());
		}

		Long areaId = activity.getAreaId();
		if(!isNull(areaId)) {
			AreaDto areaDto = cacheComponent.getAreaDto(activity.getAreaId());
			if (areaDto != null) {
				activityAdminVo.setProvince(areaDto.getProvince());
				activityAdminVo.setCity(areaDto.getCity());
				activityAdminVo.setDistrict(areaDto.getDistrict());
			}
		}
		activityAdminVo.setImageBig(getThumbnail(activity.getImage(), 300, 180));
		activityAdminVo.setImageThumbnail(getThumbnail(activity.getImage(), 150, 90));

		Date applyDeadline = activity.getApplyDeadline();  //报名截止时间
		Date startTime = activity.getStartTime();
		Date endTime = activity.getEndTime();
		if(applyDeadline != null) {
			activityAdminVo.setApplyDeadlineLabel(formatDate(applyDeadline, shortFmt));
			activityAdminVo.setApplyDeadlineFormatted(formatDate(applyDeadline, fullFmt));
		}
		if(startTime != null) {
			activityAdminVo.setStartTimeLabel(formatDate(startTime, shortFmt));
			activityAdminVo.setStartTimeFormatted(formatDate(startTime, fullFmt));
		}
		if(endTime != null) {
			activityAdminVo.setEndTimeLabel(formatDate(endTime, shortFmt));
			activityAdminVo.setEndTimeFormatted(formatDate(endTime, fullFmt));
		}
		activityAdminVo.setStatus(buildStatus(applyDeadline, startTime, endTime));
		return activityAdminVo;
	}
	
	public ActivityDetailVo buildDetailVo(Activity activity) {
		ActivityDetailVo activityDetailVo = new ActivityDetailVo();
		BeanUtils.copyProperties(activity, activityDetailVo);
		activityDetailVo.setAmountLabel(GcUtils.formatCurreny(activity.getAmount()));

		Long areaId = activity.getAreaId();
		if(!isNull(areaId)) {
			AreaDto areaDto = cacheComponent.getAreaDto(activity.getAreaId());
			activityDetailVo.setProvince(areaDto.getProvince());
			activityDetailVo.setCity(areaDto.getCity());
			activityDetailVo.setDistrict(areaDto.getDistrict());
		}
		activityDetailVo.setImageBig(getThumbnail(activity.getImage(), 750, 450));
		activityDetailVo.setImageThumbnail(getThumbnail(activity.getImage(), 240, 160));
		
		Date applyDeadline = activity.getApplyDeadline();  //报名截止时间
		Date startTime = activity.getStartTime();
		Date endTime = activity.getEndTime();
		if(!isNull(applyDeadline)) {
			activityDetailVo.setApplyDeadlineLabel(formatDate(applyDeadline, SIMPLE_TIME_PATTERN));
		}
		if(!isNull(startTime)) {
			activityDetailVo.setStartTimeLabel(formatDate(activity.getStartTime(), SIMPLE_TIME_PATTERN));
		}
		if(!isNull(endTime)) {
			activityDetailVo.setEndTimeLabel(formatDate(activity.getEndTime(), SIMPLE_TIME_PATTERN));
		}
		activityDetailVo.setStatus(buildStatus(applyDeadline, startTime, endTime));
		
		List<ActivityApply> applies = activityApplyService.findAll(ActivityApplyQueryModel.builder().activityIdEQ(activity.getId()).build());
		List<UserSimpleVo> userSimplVo = applies.stream().map(v -> {
			User user = cacheComponent.getUser(v.getUserId());
			UserSimpleVo userSimpleVo = new UserSimpleVo();
			BeanUtils.copyProperties(user, userSimpleVo);
			userSimpleVo.setAvatarThumbnail(GcUtils.getThumbnail(user.getAvatar()));
			return userSimpleVo;
		}).collect(Collectors.toList());
		activityDetailVo.setAppliedUsers(userSimplVo);
		
		return activityDetailVo;
	}
	
	public ActivityListVo buildListVo(Activity activity) {
		ActivityListVo activityListVo = new ActivityListVo();
		BeanUtils.copyProperties(activity, activityListVo);
		activityListVo.setAmountLabel(GcUtils.formatCurreny(activity.getAmount()));
		
		Long areaId = activity.getAreaId();
		if(!isNull(areaId)) {
			AreaDto areaDto = cacheComponent.getAreaDto(activity.getAreaId());
			activityListVo.setProvince(areaDto.getProvince());
			activityListVo.setCity(areaDto.getCity());
			activityListVo.setDistrict(areaDto.getDistrict());
		}
		activityListVo.setImageBig(getThumbnail(activity.getImage(), 750, 450));
		activityListVo.setImageThumbnail(getThumbnail(activity.getImage(), 240, 160));
		
		Date applyDeadline = activity.getApplyDeadline();  //报名截止时间
		Date startTime = activity.getStartTime();
		Date endTime = activity.getEndTime();
		if(!isNull(applyDeadline)) {
			activityListVo.setApplyDeadlineLabel(formatDate(applyDeadline, SIMPLE_TIME_PATTERN));
		}
		if(!isNull(startTime)) {
			activityListVo.setStartTimeLabel(formatDate(activity.getStartTime(), SIMPLE_TIME_PATTERN));
		}
		if(!isNull(endTime)) {
			activityListVo.setEndTimeLabel(formatDate(activity.getEndTime(), SIMPLE_TIME_PATTERN));
		}
		activityListVo.setStatus(buildStatus(applyDeadline, startTime, endTime));
		return activityListVo;
	}
	
	private String buildStatus(Date applyDeadline, Date startTime, Date endTime) {
		
		if(applyDeadline != null && startTime != null && endTime != null) {
			Date now = new Date();
			if (endTime.before(now)) {
				return "活动已结束";
			} else if (startTime.before(now)) {
				return "进行中";
			}
			
			if (applyDeadline.before(now)) {
				return "报名已结束";
			} else {
				return "报名中";
			}
		}
		return "活动已结束";
	}
}
