package com.zy.common.support.weixinpay.res;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xml")
public class WxOrderNotifyRes {

	private String returnCode; // SUCCESS/FAIL SUCCESS表示商户接收通知成功并校验成功
	private String returnMsg; // 返回信息，如非空，为错误原因
	
	@XmlElement(name = "return_code")
	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	@XmlElement(name = "return_msg")
	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

}
