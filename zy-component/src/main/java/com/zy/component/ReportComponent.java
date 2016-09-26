package com.zy.component;

import static com.zy.util.GcUtils.formatDate;
import static com.zy.util.GcUtils.getThumbnail;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
import com.zy.vo.ReportExportVo;
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
		reportAdminVo.setConfirmedTimeLabel(formatDate(report.getConfirmedTime(), TIME_PATTERN));
		reportAdminVo.setPreConfirmedTimeLabel(formatDate(report.getPreConfirmedTime(), TIME_PATTERN));
		List<String> images = Arrays.stream(report.getImage().split(",")).map(v -> v).collect(Collectors.toList());
		reportAdminVo.setImageBigs(images.stream().map(v -> getThumbnail(v, 640, 640)).collect(Collectors.toList()));
		reportAdminVo.setImageThumbnails(images.stream().map(v -> getThumbnail(v)).collect(Collectors.toList()));
		return reportAdminVo;
	}

	public ReportDetailVo buildDetailVo(Report report) {
		ReportDetailVo reportVo = new ReportDetailVo();
		BeanUtils.copyProperties(report, reportVo);

		if(report.getAreaId() != null){
			AreaDto area = areaService.findOneDto(report.getAreaId());
			reportVo.setProvince(area.getProvince());
			reportVo.setCity(area.getCity());
			reportVo.setDistrict(area.getDistrict());
		}
		if(report.getJobId() != null){
			reportVo.setJobName(jobService.findOne(report.getJobId()).getJobName());
		}
		reportVo.setCreatedTimeLabel(formatDate(report.getCreatedTime(), TIME_PATTERN));
		reportVo.setAppliedTimeLabel(formatDate(report.getAppliedTime(), TIME_PATTERN));
		List<String> images = Arrays.stream(report.getImage().split(",")).map(v -> v).collect(Collectors.toList());
		reportVo.setImageBigs(images.stream().map(v -> getThumbnail(v, 640, 640)).collect(Collectors.toList()));
		reportVo.setImageThumbnails(images.stream().map(v -> getThumbnail(v)).collect(Collectors.toList()));
		return reportVo;
	}

	public ReportListVo buildListVo(Report report) {
		ReportListVo reportVo = new ReportListVo();
		BeanUtils.copyProperties(report, reportVo);

		if(report.getAreaId() != null){
			AreaDto area = areaService.findOneDto(report.getAreaId());
			reportVo.setProvince(area.getProvince());
			reportVo.setCity(area.getCity());
			reportVo.setDistrict(area.getDistrict());
		}
		if(report.getJobId() != null){
			reportVo.setJobName(jobService.findOne(report.getJobId()).getJobName());
		}
		reportVo.setCreatedTimeLabel(formatDate(report.getCreatedTime(), TIME_PATTERN));
		reportVo.setAppliedTimeLabel(formatDate(report.getAppliedTime(), TIME_PATTERN));
		
		return reportVo;
	}
	
	public ReportExportVo buildExportVo(Report report) {
		ReportExportVo reportExportVo = new ReportExportVo();
		BeanUtils.copyProperties(report, reportExportVo);

		return reportExportVo;
	}
}
