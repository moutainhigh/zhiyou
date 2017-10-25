package com.zy.task.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.common.util.DateUtil;
import com.zy.entity.act.ActivityApply;
import com.zy.entity.sys.InviteNumber;
import com.zy.entity.sys.SystemCode;
import com.zy.model.query.ActivityApplyQueryModel;
import com.zy.model.query.InviteNumberQueryModel;
import com.zy.service.ActivityApplyService;
import com.zy.service.InviteNumberService;
import com.zy.service.SystemCodeService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by it001 on 2017-10-24.
 */
public class DntInviteNumberJob implements Job {
    private Logger logger = LoggerFactory.getLogger(DntInviteNumberJob.class);

    @Autowired
    private InviteNumberService inviteNumberService;

    @Autowired
    private ActivityApplyService activityApplyService;

    @Autowired
    private SystemCodeService systemCodeService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("DntInviteNumberJob begin.......");
        this.disposeInviteNumber();
        logger.info("DntInviteNumberJob end.......");
    }

    /**
     * 开始处理逻辑
     */
    private void disposeInviteNumber() {
        try {
            List<SystemCode> largeAreaTypes = systemCodeService.findByType("INVITENUMBER");//查询需要邀请码的活动
            if (largeAreaTypes == null || largeAreaTypes.isEmpty()) {
                return;
            }
            SystemCode systemCode = largeAreaTypes.get(0);
            if (systemCode == null || systemCode.getSystemValue() == null) {
                return;
            }
            List<InviteNumber> inviteNumberList = inviteNumberService.findList(InviteNumberQueryModel.builder().flageEQ(1).build()); //取到所有的已经绑定的邀请码
            Map<Long, List<InviteNumber>> inviteNumberMap = inviteNumberList.stream().collect(Collectors.groupingBy(InviteNumber::getUserId));
            List<ActivityApply> activityApplyList = activityApplyService.findAll(ActivityApplyQueryModel.builder()
                    .activityIdEQ(Long.valueOf(systemCode.getSystemValue())).build());
            List<ActivityApply> activityApplyList0 = activityApplyList.stream().filter(v -> v.getActivityApplyStatus() == ActivityApply.ActivityApplyStatus.已报名).collect(Collectors.toList());
            List<ActivityApply> activityApplyList1 = activityApplyList.stream().filter(v -> v.getActivityApplyStatus() == ActivityApply.ActivityApplyStatus.已支付).collect(Collectors.toList());
            Map<Long, ActivityApply> activityApplyMap1 = new HashMap<>();

            if (activityApplyList1 != null) {//以付款
                activityApplyMap1 = activityApplyList1.stream().collect(Collectors.toMap(v -> v.getUserId(), v -> v));
            }
            for (ActivityApply activityApply : activityApplyList0) {
                List<InviteNumber> inviteNumberList0 = inviteNumberMap.get(activityApply.getUserId());
                if (inviteNumberList0 == null || inviteNumberList0.isEmpty()) {//填邀请码不存在直接结束这个
                    return;
                }
                ActivityApply activityApply1 = activityApplyMap1.get(activityApply.getUserId()); //取到用户报名成功的
                if (inviteNumberList0.size() == 1) {//只有一条
                    if (activityApply1 == null) {//没有成功的 需要重置数据
                        InviteNumber inviteNumber = inviteNumberList0.get(0);
                        Date pastDate = DateUtil.getMonthData(inviteNumber.getUpdateTime(), 0, 1);
                        if (pastDate.getTime() >= new Date().getTime()) {//已经过期
                            inviteNumber.setUpdateTime(null);
                            inviteNumber.setUserId(null);
                            inviteNumber.setFlage(null);
                            inviteNumberService.update(inviteNumber);
                        }
                    }
                } else {//多条时
                    if (activityApply1 == null) {//没有成功的 需要重置数据
                        for (InviteNumber inviteNumber0 : inviteNumberList0) {
                            Date pastDate = DateUtil.getMonthData(inviteNumber0.getUpdateTime(), 0, 1);
                            if (pastDate.getTime() >= new Date().getTime()) {//已经过期
                                inviteNumber0.setUpdateTime(null);
                                inviteNumber0.setUserId(null);
                                inviteNumber0.setFlage(null);
                                inviteNumberService.update(inviteNumber0);
                            }
                        }
                    } else {//如果有这跳过一条
                        for (int i = 1; i < inviteNumberList0.size(); i++) {
                            InviteNumber inviteNumber0 = inviteNumberList0.get(i);
                            Date pastDate = DateUtil.getMonthData(inviteNumber0.getUpdateTime(), 0, 1);
                            if (pastDate.getTime() >=new Date().getTime()) {//已经过期
                                inviteNumber0.setUpdateTime(null);
                                inviteNumber0.setUserId(null);
                                inviteNumber0.setFlage(null);
                                inviteNumberService.update(inviteNumber0);
                            }
                        }

                    }
                }
            }
        } catch (ConcurrentException e) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e1) {
            }
            this.disposeInviteNumber();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }
}
