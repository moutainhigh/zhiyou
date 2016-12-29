package com.zy.admin.controller.notify;

import com.zy.common.exception.BizException;
import com.zy.common.support.shengpay.BatchPaymentClient;
import com.zy.common.support.shengpay.BatchPaymentNotify;
import com.zy.common.support.shengpay.BatchPaymentNotifyResult;
import com.zy.entity.fnc.Withdraw;
import com.zy.model.BizCode;
import com.zy.service.WithdrawService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2016/12/29.
 */
@RequestMapping("/notify/shengPay")
@Controller
@Slf4j
public class ShengPayNotifyController {

	@Autowired
	private BatchPaymentClient batchPaymentClient;

	@Autowired
	private WithdrawService withdrawService;


	@RequestMapping("/batchPayment")
	@ResponseBody
	public String batchPayment(BatchPaymentNotify batchPaymentNotify) {
		if (!batchPaymentClient.checkBatchPaymentNofitySign(batchPaymentNotify)) {
			log.error("验签错误");
			throw new BizException(BizCode.ERROR, "验证签名错误");
		}

		try {
			String sn = batchPaymentNotify.getBatchNo();
			Withdraw withdraw = withdrawService.findBySn(sn);
			Long withdrawId = withdraw.getId();
			if (batchPaymentClient.isBatchPaymentNofitySuccess(batchPaymentNotify)) {
				withdrawService.autoSuccess(withdrawId);
			} else {
				withdrawService.autoFailure(withdrawId, "错误code:" + batchPaymentNotify.getResultCode() + ", 错误名称:" + batchPaymentNotify.getResultName());
			}
			return BatchPaymentNotifyResult.ok();
		} catch (Exception e) {
			log.error("处理回调失败", e);
			return BatchPaymentNotifyResult.error();
		}

	}






}
