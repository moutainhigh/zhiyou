package com.zy.common.support.shengpay;

/**
 * Created by Administrator on 2016/12/26.
 */
public class BatchPaymentNotifyResult {

	public static String ok() {
		return
		"<result>"
		+ "<sign></sign>"
		+ "<signType>MD5</signType>"
		+ "<resultCode>ok</resultCode>"
		+ "<resultMessage>ok</resultMessage>"
		+ "</result>";
	}

	public static String error() {
		return
		"<result>"
		+ "<sign></sign>"
		+ "<signType>MD5</signType>"
		+ "<resultCode>error</resultCode>"
		+ "<resultMessage>error</resultMessage>"
		+ "</result>";
	}


}
