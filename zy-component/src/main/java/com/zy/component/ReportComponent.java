package com.gc.component;

import com.zy.common.util.BeanUtils;
import com.gc.entity.act.Report;
import com.zy.util.GcUtils;
import com.gc.vo.ReportAdminVo;
import com.gc.vo.ReportVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.zy.util.GcUtils.getThumbnail;
import static org.apache.commons.lang3.time.DateFormatUtils.format;

@Component
public class ReportComponent {

	@Autowired
	private CacheComponent cacheComponent;
	
	private static final String TIME_LABEL = "yyyy-MM-dd HH:mm:ss";
	
	public ReportAdminVo buildAdminVo(Report report) {
		ReportAdminVo reportAdminVo = new ReportAdminVo();
		BeanUtils.copyProperties(report, reportAdminVo);
		
		reportAdminVo.setDateLabel(format(report.getDate(), TIME_LABEL));
		reportAdminVo.setImage1Thumbnail(getThumbnail(report.getImage1()));
		reportAdminVo.setImage2Thumbnail(getThumbnail(report.getImage2()));
		reportAdminVo.setImage3Thumbnail(getThumbnail(report.getImage3()));
		reportAdminVo.setImage4Thumbnail(getThumbnail(report.getImage4()));
		reportAdminVo.setImage5Thumbnail(getThumbnail(report.getImage5()));
		reportAdminVo.setImage6Thumbnail(getThumbnail(report.getImage6()));
		return reportAdminVo;
	}

	public ReportVo buildVo(Report report) {
		ReportVo reportVo = new ReportVo();
		
		reportVo.setImage1Thumbnail(GcUtils.getThumbnail(report.getImage1()));
		reportVo.setImage2Thumbnail(GcUtils.getThumbnail(report.getImage2()));
		reportVo.setImage3Thumbnail(GcUtils.getThumbnail(report.getImage3()));
		reportVo.setImage4Thumbnail(GcUtils.getThumbnail(report.getImage4()));
		reportVo.setImage5Thumbnail(GcUtils.getThumbnail(report.getImage5()));
		reportVo.setImage6Thumbnail(GcUtils.getThumbnail(report.getImage6()));
		
		BeanUtils.copyProperties(report, reportVo);
		return reportVo;
	}


}
