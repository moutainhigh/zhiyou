package com.zy.component;

import static com.zy.util.GcUtils.formatDate;
import static com.zy.util.GcUtils.getThumbnail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.act.Report;
import com.zy.model.dto.AreaDto;
import com.zy.service.AreaService;
import com.zy.service.JobService;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.ReportAdminVo;
import com.zy.vo.ReportDetailVo;
import com.zy.vo.ReportListVo;

@Component
public class ReportComponent {

	@Autowired
	private CacheComponent cacheComponent;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private AreaService areaService;

	private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	public ReportAdminVo buildAdminVo(Report report) {
		ReportAdminVo reportAdminVo = new ReportAdminVo();
		BeanUtils.copyProperties(report, reportAdminVo);

		reportAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(report.getUserId())));
		reportAdminVo.setCreatedTimeLabel(formatDate(report.getCreatedTime(), TIME_PATTERN));
		reportAdminVo.setImage1Thumbnail(getThumbnail(report.getImage1()));
		reportAdminVo.setImage2Thumbnail(getThumbnail(report.getImage2()));
		reportAdminVo.setImage3Thumbnail(getThumbnail(report.getImage3()));
		reportAdminVo.setImage4Thumbnail(getThumbnail(report.getImage4()));
		reportAdminVo.setImage5Thumbnail(getThumbnail(report.getImage5()));
		reportAdminVo.setImage6Thumbnail(getThumbnail(report.getImage6()));
		reportAdminVo.setImage1Big(report.getImage1());
		reportAdminVo.setImage2Big(report.getImage2());
		reportAdminVo.setImage3Big(report.getImage3());
		reportAdminVo.setImage4Big(report.getImage4());
		reportAdminVo.setImage5Big(report.getImage5());
		reportAdminVo.setImage6Big(report.getImage6());
		return reportAdminVo;
	}

	public ReportDetailVo buildDetailVo(Report report) {
		ReportDetailVo reportVo = new ReportDetailVo();
		BeanUtils.copyProperties(report, reportVo);

		AreaDto area = areaService.findOneDto(report.getAreaId());
		reportVo.setJobName(jobService.findOne(report.getJobId()).getJobName());
		reportVo.setProvince(area.getProvince());
		reportVo.setCity(area.getCity());
		reportVo.setDistrict(area.getDistrict());
		reportVo.setCreatedTimeLabel(formatDate(report.getCreatedTime(), TIME_PATTERN));
		reportVo.setImage1Thumbnail(GcUtils.getThumbnail(report.getImage1()));
		reportVo.setImage2Thumbnail(GcUtils.getThumbnail(report.getImage2()));
		reportVo.setImage3Thumbnail(GcUtils.getThumbnail(report.getImage3()));
		reportVo.setImage4Thumbnail(GcUtils.getThumbnail(report.getImage4()));
		reportVo.setImage5Thumbnail(GcUtils.getThumbnail(report.getImage5()));
		reportVo.setImage6Thumbnail(GcUtils.getThumbnail(report.getImage6()));
		reportVo.setImage1Big(GcUtils.getThumbnail(report.getImage1(), 640, 640));
		reportVo.setImage2Big(GcUtils.getThumbnail(report.getImage2(), 640, 640));
		reportVo.setImage3Big(GcUtils.getThumbnail(report.getImage3(), 640, 640));
		reportVo.setImage4Big(GcUtils.getThumbnail(report.getImage4(), 640, 640));
		reportVo.setImage5Big(GcUtils.getThumbnail(report.getImage5(), 640, 640));
		reportVo.setImage6Big(GcUtils.getThumbnail(report.getImage6(), 640, 640));
		
		return reportVo;
	}

	public ReportListVo buildListVo(Report report) {
		ReportListVo reportVo = new ReportListVo();
		BeanUtils.copyProperties(report, reportVo);

		AreaDto area = areaService.findOneDto(report.getAreaId());
		reportVo.setJobName(jobService.findOne(report.getJobId()).getJobName());
		reportVo.setProvince(area.getProvince());
		reportVo.setCity(area.getCity());
		reportVo.setDistrict(area.getDistrict());
		reportVo.setCreatedTimeLabel(formatDate(report.getCreatedTime(), TIME_PATTERN));
		
		return reportVo;
	}
	
}
