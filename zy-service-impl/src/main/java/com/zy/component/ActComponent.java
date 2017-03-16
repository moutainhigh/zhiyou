package com.zy.component;

import com.zy.entity.act.ActivityApply;
import com.zy.entity.act.Report;
import com.zy.entity.act.ReportLog;
import com.zy.mapper.ActivityApplyMapper;
import com.zy.mapper.ReportLogMapper;
import com.zy.mapper.ReportMapper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

/**
 * Created by freeman on 16/8/3.
 */
@Component
@Validated
@Slf4j
public class ActComponent {

	@Autowired
	private ReportMapper reportMapper;

	@Autowired
	private ReportLogMapper reportLogMapper;

	@Autowired
	private ActivityApplyMapper activityApplyMapper;

	public void recordReportLog(@NotNull Long reportId, @NotBlank String remark) {
		Report report = reportMapper.findOne(reportId);
		validate(report, NOT_NULL, "report id " + reportId + " not found");

		ReportLog reportLog = new ReportLog();
		reportLog.setReportId(reportId);
		reportLog.setReportPreConfirmStatus(report.getPreConfirmStatus());
		reportLog.setReportConfirmStatus(report.getConfirmStatus());
		reportLog.setRemark(remark);
		reportLog.setCreatedTime(new Date());
		reportLogMapper.insert(reportLog);
	}

	public void successActivityApply(@NotNull Long activityApplyId) {
		ActivityApply activityApply = activityApplyMapper.findOne(activityApplyId);
		validate(activityApply, NOT_NULL, "activity apply id " + activityApplyId + " not found");
		if (activityApply.getActivityApplyStatus() == ActivityApply.ActivityApplyStatus.已支付) {
			return;
		}

		activityApply.setActivityApplyStatus(ActivityApply.ActivityApplyStatus.已支付);
		activityApplyMapper.update(activityApply);
	}
}
