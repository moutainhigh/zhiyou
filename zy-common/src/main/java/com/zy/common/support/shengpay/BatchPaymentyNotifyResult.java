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
public class BatchPaymentyNotifyResult {

	String sign;
	String signType = "MD5";
	String resultCode;
	String resultMessage;

	public static BatchPaymentyNotifyResult ok() {
		BatchPaymentyNotifyResult batchPaymentyNotifyResult = new BatchPaymentyNotifyResult();
		batchPaymentyNotifyResult.setResultCode("ok");
		batchPaymentyNotifyResult.setResultMessage("ok");
		return batchPaymentyNotifyResult;
	}

	public static BatchPaymentyNotifyResult error(String errorMessage) {
		BatchPaymentyNotifyResult batchPaymentyNotifyResult = new BatchPaymentyNotifyResult();
		batchPaymentyNotifyResult.setResultCode("error");
		batchPaymentyNotifyResult.setResultMessage(errorMessage);
		return batchPaymentyNotifyResult;
	}

	public static void main(String[] args) {
		System.out.println(XmlUtils.toXml(BatchPaymentyNotifyResult.error("123")));
	}

}
