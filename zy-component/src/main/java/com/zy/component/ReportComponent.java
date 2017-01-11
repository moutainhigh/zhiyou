package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.act.Report;
import com.zy.entity.usr.User;
import com.zy.model.dto.AreaDto;
import com.zy.service.AreaService;
import com.zy.service.JobService;
import com.zy.service.ProductService;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.ReportAdminVo;
import com.zy.vo.ReportDetailVo;
import com.zy.vo.ReportExportVo;
import com.zy.vo.ReportListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.zy.util.GcUtils.formatDate;
import static com.zy.util.GcUtils.getThumbnail;

@Component
public class ReportComponent {

	@Autowired
	private CacheComponent cacheComponent;
	
	@Autowired
	private ProductService productService;

	@Autowired
	private ProductComponent productComponent;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private AreaService areaService;

	private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm";
	
	public ReportAdminVo buildAdminVo(Report report) {
		ReportAdminVo reportAdminVo = new ReportAdminVo();
		BeanUtils.copyProperties(report, reportAdminVo);

		reportAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(report.getUserId())));
		Long visitUserId = report.getVisitUserId();
		if (visitUserId != null) {
			reportAdminVo.setVisitUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(visitUserId)));
		}
		reportAdminVo.setCreatedTimeLabel(formatDate(report.getCreatedTime(), TIME_PATTERN));
		reportAdminVo.setAppliedTimeLabel(formatDate(report.getCreatedTime(), TIME_PATTERN));
		reportAdminVo.setConfirmedTimeLabel(formatDate(report.getConfirmedTime(), TIME_PATTERN));
		reportAdminVo.setPreConfirmedTimeLabel(formatDate(report.getPreConfirmedTime(), TIME_PATTERN));
		reportAdminVo.setReportedDateLabel(formatDate(report.getReportedDate(), "yyyy-MM-dd"));
		List<String> images = Arrays.stream(report.getImage().split(",")).map(v -> v).collect(Collectors.toList());
		reportAdminVo.setImages(images);
		reportAdminVo.setImageBigs(images.stream().map(v -> getThumbnail(v, 640, 640)).collect(Collectors.toList()));
		reportAdminVo.setImageThumbnails(images.stream().map(v -> getThumbnail(v)).collect(Collectors.toList()));
		
		if(report.getAreaId() != null){
			AreaDto area = areaService.findOneDto(report.getAreaId());
			reportAdminVo.setProvince(area.getProvince());
			reportAdminVo.setCity(area.getCity());
			reportAdminVo.setDistrict(area.getDistrict());
		}
		if(report.getJobId() != null){
			reportAdminVo.setJobName(jobService.findOne(report.getJobId()).getJobName());
		}
		if(report.getProductId() != null){
			reportAdminVo.setProduct(productComponent.buildListVo(productService.findOne(report.getProductId())));
		}
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
		if(report.getProductId() != null){
			reportVo.setProduct(productComponent.buildListVo(productService.findOne(report.getProductId())));
		}
		reportVo.setCreatedTimeLabel(formatDate(report.getCreatedTime(), TIME_PATTERN));
		reportVo.setAppliedTimeLabel(formatDate(report.getAppliedTime(), TIME_PATTERN));
		reportVo.setReportedDateLabel(formatDate(report.getReportedDate(), "yyyy-MM-dd"));
		List<String> images = Arrays.stream(report.getImage().split(",")).map(v -> v).collect(Collectors.toList());
		reportVo.setImages(images);
		reportVo.setImageBigs(images.stream().map(v -> getThumbnail(v, 640, 640)).collect(Collectors.toList()));
		reportVo.setImageThumbnails(images.stream().map(v -> getThumbnail(v)).collect(Collectors.toList()));
		Date confirmedTime = report.getConfirmedTime();
		if(confirmedTime != null) {
			reportVo.setConfirmedTimeLabel(formatDate(confirmedTime, TIME_PATTERN));
		}
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
		if(report.getProductId() != null){
			reportVo.setProductTitle(productService.findOne(report.getProductId()).getTitle());
		}
		reportVo.setCreatedTimeLabel(formatDate(report.getCreatedTime(), TIME_PATTERN));
		reportVo.setAppliedTimeLabel(formatDate(report.getAppliedTime(), TIME_PATTERN));
		reportVo.setReportedDateLabel(formatDate(report.getReportedDate(), "yyyy-MM-dd"));
		
		return reportVo;
	}
	
	public ReportExportVo buildExportVo(Report report) {
		ReportExportVo reportExportVo = new ReportExportVo();
		BeanUtils.copyProperties(report, reportExportVo);

		Long userId = report.getUserId();
		if(userId != null) {
			User user = cacheComponent.getUser(report.getUserId());
			reportExportVo.setUserNickname(user.getNickname());
			reportExportVo.setUserPhone(user.getPhone());
		}
		if(report.getAreaId() != null){
			AreaDto area = areaService.findOneDto(report.getAreaId());
			reportExportVo.setProvince(area.getProvince());
			reportExportVo.setCity(area.getCity());
			reportExportVo.setDistrict(area.getDistrict());
		}
		if(report.getJobId() != null){
			reportExportVo.setJobName(jobService.findOne(report.getJobId()).getJobName());
		}
		reportExportVo.setReportedDateLabel(formatDate(report.getReportedDate(), "yyyy-MM-dd"));
		reportExportVo.setAppliedTimeLabel(GcUtils.formatDate(report.getAppliedTime(), TIME_PATTERN));
		reportExportVo.setPreConfirmedTimeLabel(GcUtils.formatDate(report.getPreConfirmedTime(), TIME_PATTERN));
		reportExportVo.setConfirmedTimeLabel(GcUtils.formatDate(report.getConfirmedTime(), TIME_PATTERN));
		return reportExportVo;
	}
}
