package com.zy.common.support.shengpay;

import com.shengpay.mcl.bp.api.ApplyInfoDetail;
import com.shengpay.mcl.bp.api.BatchPayment;
import com.shengpay.mcl.bp.api.BatchPaymentService;
import com.shengpay.mcl.bp.api.DirectApplyRequest;
import com.shengpay.mcl.btc.response.DirectApplyResponse;
import com.zy.common.util.Digests;
import com.zy.common.util.Encodes;
import com.zy.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Administrator on 2016/12/28.
 */
@Slf4j
public class BatchPaymentClient {

	private static final QName SERVICE_NAME = new QName("http://api.bp.mcl.shengpay.com/", "BatchPaymentService");

	private BatchPayment port;

	private String merchantId;

	private String key;

	public BatchPaymentClient(String merchantId, String key) {

		URL wsdlURL = BatchPaymentService.WSDL_LOCATION;

		BatchPaymentService ss = new BatchPaymentService(wsdlURL, SERVICE_NAME);
		port = ss.getBatchPaymentPort();
		this.merchantId = merchantId;
		this.key = key;
	}

	// 工商银行，建设银行，农业银行，招商银行，交通银行, 平安银行，广发银行，光大银行，民生银行，中信银行，浦东发展银行，北京银行
	//	交通银行，建设银行，招商银行
	//	光大银行，民生银行
	public DirectApplyResponse directApply(String sn, BigDecimal amount, String province, String city, String bankName, String branchName, Boolean isEnterprise, String realname, String cardNumber, String callbackUrl) {
		DirectApplyRequest directApplyRequest = new DirectApplyRequest();
		directApplyRequest.setBatchNo(sn);
		directApplyRequest.setCallbackUrl(callbackUrl);
		directApplyRequest.setTotalAmount(amount);
		directApplyRequest.setCustomerNo(merchantId);
		directApplyRequest.setCharset("utf-8");
		directApplyRequest.setSignType("MD5");


		ApplyInfoDetail applyInfoDetail = new ApplyInfoDetail();
		applyInfoDetail.setId(sn);
		applyInfoDetail.setProvince(province);
		applyInfoDetail.setCity(city);
		applyInfoDetail.setAccountType(isEnterprise ? "B" : "C");
		applyInfoDetail.setBankAccount(cardNumber);
		applyInfoDetail.setBankUserName(realname);
		applyInfoDetail.setBranchName(branchName);
		applyInfoDetail.setBankName(bankName);
		applyInfoDetail.setAmount(amount);


		directApplyRequest.getDetails().add(applyInfoDetail);

		String sign = getDirectApplySign(directApplyRequest);
		directApplyRequest.setSign(sign);
		return port.directApply(directApplyRequest);
	}

	public boolean isDirectApplyResponseSuccess(DirectApplyResponse directApplyResponse) {
		return directApplyResponse.getResultCode().equals("00");
	}


	private String getDirectApplySign(DirectApplyRequest directApplyRequest) {
		//签名原始串拼写(“+”表示连接，”(”,”)”左括号右括号表示范围，不包含于签名原始串，totalAmount，amount需要格式化成“#.00”格式，如100.5，格式化后为100.50)
		//signStr：      charset+signType+customerNo+batchNo+callbackUrl+totalAmount+循环拼明细（id+province+city+branchName+bankName+accountType+bankUserName+bankAccount+amount+remark）+md5key
		ApplyInfoDetail applyInfoDetail = directApplyRequest.getDetails().get(0);
		StringBuilder forSign = new StringBuilder();
		forSign.append(directApplyRequest.getCharset());
		forSign.append(directApplyRequest.getSignType());
		forSign.append(directApplyRequest.getCustomerNo());
		forSign.append(directApplyRequest.getBatchNo());
		forSign.append(directApplyRequest.getCallbackUrl());
		forSign.append(directApplyRequest.getTotalAmount());

		forSign.append(applyInfoDetail.getId());
		forSign.append(applyInfoDetail.getProvince() == null ? "" : applyInfoDetail.getProvince());
		forSign.append(applyInfoDetail.getCity() == null ? "" : applyInfoDetail.getCity());
		forSign.append(applyInfoDetail.getBranchName() == null ? "" : applyInfoDetail.getBranchName());
		forSign.append(applyInfoDetail.getBankName());
		forSign.append(applyInfoDetail.getAccountType());
		forSign.append(applyInfoDetail.getBankUserName());
		forSign.append(applyInfoDetail.getBankAccount());
		forSign.append(applyInfoDetail.getAmount());
		forSign.append(applyInfoDetail.getRemark() == null ? "" : applyInfoDetail.getRemark());

		forSign.append(key);
		log.warn("directApplyRequest = " + JsonUtils.toJson(directApplyRequest));
		log.warn("forSign = " + forSign.toString());
		return Encodes.encodeHex(Digests.md5(forSign.toString().getBytes(Charset.forName("UTF-8")))).toUpperCase();

	}

	public boolean isBatchPaymentNotifySuccess(BatchPaymentNotify batchPaymentNotify) {
		return batchPaymentNotify.getResultCode().equals("S001") || batchPaymentNotify.getResultCode().equals("S002");
	}


	public boolean checkBatchPaymentNotifySign(BatchPaymentNotify batchPaymentNotify) {
		// 签名原始串拼写({param} 替换成param对应的值, “+”表示连接，不包含于签名原始串，“=”是原始串的一部分, 空值不参与签名)
		// signStr：charset={charset}batchNo={batchNo}statusCode={statusCode}statusName=
		//		{statusName}fileName={fileName}resultCode={resultCode}resultName=
		//		{resultName}resultMemo={resultMemo}+md5key
		String forSign = "";
		forSign += "charset=" + batchPaymentNotify.getCharset();
		forSign += "batchNo=" + batchPaymentNotify.getBatchNo();
		forSign += "statusCode=" + batchPaymentNotify.getStatusCode();


		forSign += batchPaymentNotify.getStatusName() == null ? "" : "statusName=" + batchPaymentNotify.getStatusName();
		forSign += batchPaymentNotify.getFileName() == null ? "" : "fileName=" + batchPaymentNotify.getFileName();
		forSign += batchPaymentNotify.getResultCode() == null ? "" : "resultCode=" + batchPaymentNotify.getResultCode();
		forSign += batchPaymentNotify.getResultName() == null ? "" : "resultName=" + batchPaymentNotify.getResultName();
		forSign += batchPaymentNotify.getResultMemo() == null ? "" : "resultMemo=" + batchPaymentNotify.getResultMemo();

		forSign += key;

		String signed = Encodes.encodeHex(Digests.md5(forSign.toString().getBytes(Charset.forName("UTF-8")))).toUpperCase();
		if (signed.equals(batchPaymentNotify.getSign())) {
			return true;
		} else {
			return false;
		}

	}

}
