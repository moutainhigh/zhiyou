package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.cms.Matter;
import com.zy.util.GcUtils;
import com.zy.vo.MatterVo;
import org.springframework.stereotype.Component;

@Component
public class MatterComponent {

	private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	private static final String SIMPLE_TIME_PATTERN = "M月d日";

	public MatterVo buildMatterVo(Matter matter, boolean withDetail) {
		MatterVo matterVo = new MatterVo();
		BeanUtils.copyProperties(matter, matterVo, "detail");
		if (matterVo.getType() == 2){
			matterVo.setUrl(GcUtils.getThumbnail(matter.getUrl(), 360, 216));
		}
		matterVo.setUploadTime(GcUtils.formatDate(matter.getUploadTime(), TIME_PATTERN));
		return matterVo;
	}

	public MatterVo buildMatterListVo(Matter matter) {
		MatterVo matterVo = new MatterVo();
		BeanUtils.copyProperties(matter, matterVo, "detail");
		if (matterVo.getType() == 2){
			matterVo.setUrl(GcUtils.getThumbnail(matter.getUrl(), 360, 216));
		}
		matterVo.setUploadTime(GcUtils.formatDate(matter.getUploadTime(), SIMPLE_TIME_PATTERN));
		return matterVo;
	}

}
