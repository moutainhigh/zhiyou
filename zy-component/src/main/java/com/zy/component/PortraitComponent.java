package com.zy.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.usr.Job;
import com.zy.entity.usr.Portrait;
import com.zy.model.dto.AreaDto;
import com.zy.service.TagService;
import com.zy.vo.PortraitAdminVo;
import com.zy.vo.PortraitVo;


@Component
public class PortraitComponent {

	@Autowired
	private CacheComponent cacheComponent;

	@Autowired
	private TagService tagService;

	public PortraitVo buildVo(Portrait v) {
		PortraitVo portraitVo = new PortraitVo();

		AreaDto areaDto = cacheComponent.getAreaDto(v.getAreaId());
		portraitVo.setProvince(areaDto.getProvince());
		portraitVo.setCity(areaDto.getCity());
		portraitVo.setDistrict(areaDto.getDistrict());

		return portraitVo;
	}
	
	public PortraitAdminVo buildAdminVo(Portrait portrait) {
		
		PortraitAdminVo portraitAdminVo = new PortraitAdminVo();
		BeanUtils.copyProperties(portrait, portraitAdminVo);

		AreaDto areaDto = cacheComponent.getAreaDto(portrait.getAreaId());
		if (areaDto != null) {
			portraitAdminVo.setProvince(areaDto.getProvince());
			portraitAdminVo.setCity(areaDto.getCity());
			portraitAdminVo.setDistrict(areaDto.getDistrict());
		}

		Long jobId = portrait.getJobId();
		if (jobId != null) {
			Job job = cacheComponent.getJob(jobId);
			if (job != null) {
				portraitAdminVo.setJobName(job.getJobName());	
			}
		}
		
		return portraitAdminVo;
	}

}
