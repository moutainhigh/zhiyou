package com.zy.task.job;

import com.zy.common.exception.ConcurrentException;
import com.zy.entity.act.Policy;
import com.zy.service.PolicyService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

import static com.zy.model.query.PolicyQueryModel.builder;

/**
 * Created by husky on 2017/2/22.
 */
public class PolicyCheckJob implements Job {

	private Logger logger = LoggerFactory.getLogger(PolicyCheckJob.class);

	@Autowired
	private PolicyService policyService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("policyCheckJob begin...");
		policyService.findAll(builder().policyStatusEQ(Policy.PolicyStatus.已生效).build())
				.stream()
				.map(policy -> policy.getId())
				.forEach(this::checkAndModify);
		logger.info("policyCheckJob end...");
	}

	private void checkAndModify(Long id) {
		try {

			this.policyService.checkAndModify(id);
			logger.info(" policyCheckJob {} success", id);
		} catch (ConcurrentException e) {
			try {
				TimeUnit.SECONDS.sleep(2);} catch (InterruptedException e1) {}
			checkAndModify(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
