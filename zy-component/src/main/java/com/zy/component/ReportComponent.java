package com.zy.component;

import static com.zy.util.GcUtils.formatDate;
import static com.zy.util.GcUtils.getThumbnail;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.act.Report;
import com.zy.util.GcUtils;
import com.zy.util.VoHelper;
import com.zy.vo.ReportAdminVo;
import com.zy.vo.ReportVo;

@Component
public class ReportComponent {

	@Autowired
	private CacheComponent cacheComponent;

	private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final String SIMPLE_TIME_PATTERN = "yyyy-MM-dd";
	
	public ReportAdminVo buildAdminVo(Report report) {
		ReportAdminVo reportAdminVo = new ReportAdminVo();
		BeanUtils.copyProperties(report, reportAdminVo);

		reportAdminVo.setUser(VoHelper.buildUserAdminSimpleVo(cacheComponent.getUser(report.getUserId())));
		reportAdminVo.setCreatedTimeLabel(formatDate(report.getCreatedTime(), TIME_PATTERN));
		reportAdminVo.setDateLabel(formatDate(report.getDate(), SIMPLE_TIME_PATTERN));
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

	public ReportVo buildVo(Report report) {
		ReportVo reportVo = new ReportVo();
		BeanUtils.copyProperties(report, reportVo);

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


}
