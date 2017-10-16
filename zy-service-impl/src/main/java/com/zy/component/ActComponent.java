package com.zy.component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.zy.common.exception.BizException;
import com.zy.common.exception.ConcurrentException;
import com.zy.common.support.AliyunOssSupport;
import com.zy.entity.act.*;
import com.zy.mapper.ActivityApplyMapper;
import com.zy.mapper.ReportLogMapper;
import com.zy.mapper.ReportMapper;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.service.ActivityService;
import com.zy.service.ActivityTeamApplyService;
import com.zy.service.ActivityTicketService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.imageio.ImageIO;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

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

	@Autowired
	private ActivityTeamApplyService activityTeamApplyService;

	@Autowired
	private ActivityTicketService activityTicketService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private AliyunOssSupport aliyunOssSupport;

	public void recordReportLog(@NotNull Long reportId, Long userId, @NotBlank String remark) {
		Report report = reportMapper.findOne(reportId);
		validate(report, NOT_NULL, "report id " + reportId + " not found");

		ReportLog reportLog = new ReportLog();
		reportLog.setReportId(reportId);
		reportLog.setReportPreConfirmStatus(report.getPreConfirmStatus());
		reportLog.setReportConfirmStatus(report.getConfirmStatus());
		reportLog.setRemark(remark);
		reportLog.setCreatedTime(new Date());
		reportLog.setCreateId(userId);
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

	public void successActivityTeamApply(@NotNull Long activityTeamApplyId)  {
		ActivityTeamApply activityTeamApply = activityTeamApplyService.findOne(activityTeamApplyId);
		validate(activityTeamApply, NOT_NULL, "activityTeamApply team apply id " + activityTeamApplyId + " not found");
		if (activityTeamApply.getPaidStatus() == ActivityTeamApply.PaidStatus.已支付) {
			return;
		}
		activityTeamApply.setPaidTime(new Date());
		activityTeamApply.setPaidStatus(ActivityTeamApply.PaidStatus.已支付);
		if (activityTeamApplyService.update(activityTeamApply) == 0) {
			throw new ConcurrentException();
		}
		ActivityTicket activityTicket = new ActivityTicket();
		for (int i = 0; i < activityTeamApply.getCount(); i++) {
			activityTicket.setTeamApplyId(activityTeamApplyId);
			activityTicket.setIsUsed(0);
            ActivityTicket insertTicket = activityTicketService.insert(activityTicket);
            //生成二维码并OSS存储
            Activity activity = activityService.findOne(activityTeamApply.getActivityId());
            validate(activity, NOT_NULL, "activity id " + activityTeamApply.getActivityId() + " is not found");
			//String qrCodeUrl = "http://192.168.1.93:8080" + "/u/activity/actQrCodeApply?activityId=" + activityTeamApply.getActivityId() + "&ticketId=" + insertTicket.getId() ;
			String qrCodeUrl = Constants.URL_MOBILE + "/u/activity/actQrCodeApply?activityId=" + activityTeamApply.getActivityId() + "&ticketId=" + insertTicket.getId() ;
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            Map<EncodeHintType, String> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            try {
				BitMatrix bitMatrix = multiFormatWriter.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 480, 480, hints);
                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "jpeg", os);
                InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
                byte[] bytes = IOUtils.toByteArray(inputStream);
                String contentType = "image/jpeg";
                String imageUrl = aliyunOssSupport.putPublicObject(Constants.ALIYUN_BUCKET_NAME_IMAGE, "qrCode/", new ByteArrayInputStream(bytes), bytes.length, contentType, Constants.ALIYUN_URL_IMAGE);
                //存储URL路径
                insertTicket.setCodeImageUrl(imageUrl);
                activityTicketService.update(insertTicket);
            }catch (Exception e){
                throw new BizException(BizCode.ERROR, "二维码生成失败");
            }
        }
	}
}
