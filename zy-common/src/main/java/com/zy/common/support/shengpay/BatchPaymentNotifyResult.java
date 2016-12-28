package com.zy.common.support.shengpay;

import com.zy.common.util.XmlUtils;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Administrator on 2016/12/26.
 */
@Getter
@Setter
@XmlRootElement(name = "result")
public class BatchPaymentNotifyResult {

	String sign;
	String signType = "MD5";
	String resultCode;
	String resultMessage;

	public static BatchPaymentNotifyResult ok() {
		BatchPaymentNotifyResult batchPaymentNotifyResult = new BatchPaymentNotifyResult();
		batchPaymentNotifyResult.setResultCode("ok");
		batchPaymentNotifyResult.setResultMessage("ok");
		return batchPaymentNotifyResult;
	}

	public static BatchPaymentNotifyResult error(String errorMessage) {
		BatchPaymentNotifyResult batchPaymentNotifyResult = new BatchPaymentNotifyResult();
		batchPaymentNotifyResult.setResultCode("error");
		batchPaymentNotifyResult.setResultMessage(errorMessage);
		return batchPaymentNotifyResult;
	}

	public static void main(String[] args) {
		System.out.println(XmlUtils.toXml(BatchPaymentNotifyResult.error("123")));
	}

}
