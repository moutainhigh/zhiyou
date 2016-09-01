package com.gc.component;

import com.zy.common.util.BeanUtils;
import com.gc.entity.act.Activity;
import com.gc.model.dto.AreaDto;
import com.gc.vo.ActivityAdminVo;
import com.gc.vo.ActivityDetailVo;
import com.gc.vo.ActivityListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.zy.util.GcUtils.getThumbnail;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.time.DateFormatUtils.format;

@Component
public class ActivityComponent {

	@Autowired
	private CacheComponent cacheComponent;

	private static final String TIME_LABEL = "yyyy-MM-dd HH:mm";
	
	public ActivityAdminVo buildAdminVo(Activity activity) {
		String fullFmt = "yyyy-MM-dd HH:mm:ss";
		String shortFmt = "M月d日 HH:mm";
		ActivityAdminVo activityAdminVo = new ActivityAdminVo();
		BeanUtils.copyProperties(activity, activityAdminVo);
		
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
			activityAdminVo.setApplyDeadlineLabel(format(applyDeadline, shortFmt));
			activityAdminVo.setApplyDeadlineFormatted(format(applyDeadline, fullFmt));
		}
		if(startTime != null) {
			activityAdminVo.setStartTimeLabel(format(startTime, shortFmt));
			activityAdminVo.setStartTimeFormatted(format(startTime, fullFmt));
		}
		if(endTime != null) {
			activityAdminVo.setEndTimeLabel(format(endTime, shortFmt));
			activityAdminVo.setEndTimeFormatted(format(endTime, fullFmt));
		}
		activityAdminVo.setStatus(buildStatus(applyDeadline, startTime, endTime));
		return activityAdminVo;
	}
	
	public ActivityDetailVo buildDetailVo(Activity activity) {
		ActivityDetailVo activityDetailVo = new ActivityDetailVo();
		BeanUtils.copyProperties(activity, activityDetailVo);
		
		Long areaId = activity.getAreaId();
		if(!isNull(areaId)) {
			AreaDto areaDto = cacheComponent.getAreaDto(activity.getAreaId());
			activityDetailVo.setProvince(areaDto.getProvince());
			activityDetailVo.setCity(areaDto.getCity());
			activityDetailVo.setDistrict(areaDto.getDistrict());
		}
		activityDetailVo.setImageBig(getThumbnail(activity.getImage(), 750, 450));
		activityDetailVo.setImageThumbnail(getThumbnail(activity.getImage(), 180, 80));
		
		Date applyDeadline = activity.getApplyDeadline();  //报名截止时间
		Date startTime = activity.getStartTime();
		Date endTime = activity.getEndTime();
		if(!isNull(applyDeadline)) {
			activityDetailVo.setApplyDeadlineLabel(format(applyDeadline, TIME_LABEL));
		}
		if(!isNull(startTime)) {
			activityDetailVo.setStartTimeLabel(format(activity.getStartTime(), TIME_LABEL));
		}
		if(!isNull(endTime)) {
			activityDetailVo.setEndTimeLabel(format(activity.getEndTime(), TIME_LABEL));
		}
		activityDetailVo.setStatus(buildStatus(applyDeadline, startTime, endTime));
		return activityDetailVo;
	}
	
	public ActivityListVo buildListVo(Activity activity) {
		ActivityListVo activityListVo = new ActivityListVo();
		BeanUtils.copyProperties(activity, activityListVo);
		
		Long areaId = activity.getAreaId();
		if(!isNull(areaId)) {
			AreaDto areaDto = cacheComponent.getAreaDto(activity.getAreaId());
			activityListVo.setProvince(areaDto.getProvince());
			activityListVo.setCity(areaDto.getCity());
			activityListVo.setDistrict(areaDto.getDistrict());
		}
		activityListVo.setImageBig(getThumbnail(activity.getImage(), 750, 450));
		activityListVo.setImageThumbnail(getThumbnail(activity.getImage(), 180, 80));
		
		Date applyDeadline = activity.getApplyDeadline();  //报名截止时间
		Date startTime = activity.getStartTime();
		Date endTime = activity.getEndTime();
		if(!isNull(applyDeadline)) {
			activityListVo.setApplyDeadlineLabel(format(applyDeadline, TIME_LABEL));
		}
		if(!isNull(startTime)) {
			activityListVo.setStartTimeLabel(format(activity.getStartTime(), TIME_LABEL));
		}
		if(!isNull(endTime)) {
			activityListVo.setEndTimeLabel(format(activity.getEndTime(), TIME_LABEL));
		}
		activityListVo.setStatus(buildStatus(applyDeadline, startTime, endTime));
		return activityListVo;
	}
	
	private String buildStatus(Date applyDeadline, Date startTime, Date endTime) {
		String status = StringUtils.EMPTY;
		if(!isNull(applyDeadline) && !isNull(startTime) && !isNull(endTime)) {
			Date now = new Date();
			if(applyDeadline.before(now)) {
				status = "报名中";
			} else if(applyDeadline.after(now) && startTime.after(now)) {
				status = "报名已结束";
			} else if(startTime.before(now) && endTime.after(now)) {
				status = "进行中";
			} else if(endTime.before(now)) {
				status = "活动已结束";
			}
		}
		return status;
	}
}
