package com.zy.common.support.shengpay;

import com.shengpay.mcl.bp.api.ApplyInfoDetail;
import com.shengpay.mcl.bp.api.BatchPayment;
import com.shengpay.mcl.bp.api.BatchPaymentService;
import com.shengpay.mcl.bp.api.DirectApplyRequest;
import com.shengpay.mcl.btc.response.DirectApplyResponse;
import com.zy.common.util.Digests;
import com.zy.common.util.Encodes;

import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Administrator on 2016/12/28.
 */
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
	public DirectApplyResponse directApply(String sn, BigDecimal amount, String province, String city, String bankName, String branchName, Boolean isEnterprise, String realname, String cardNumber) {
		DirectApplyRequest directApplyRequest = new DirectApplyRequest();
		directApplyRequest.setBatchNo(sn);
		directApplyRequest.setTotalAmount(amount);
		directApplyRequest.setCustomerNo(merchantId);
		directApplyRequest.setCharset("utf-8");
		directApplyRequest.setSign("MD5");


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


	private String getDirectApplySign(DirectApplyRequest directApplyRequest) {
		// TODO
		//签名原始串拼写(“+”表示连接，”(”,”)”左括号右括号表示范围，不包含于签名原始串，totalAmount，amount需要格式化成“#.00”格式，如100.5，格式化后为100.50)
		//signStr：      charset+signType+customerNo+batchNo+callbackUrl+totalAmount+循环拼明细（id+province+city+branchName+bankName+accountType+bankUserName+bankAccount+amount+remark）+md5key
		ApplyInfoDetail applyInfoDetail = directApplyRequest.getDetails().get(0);
		//String forSign =
		String seperator = "+";
		StringBuilder forSign = new StringBuilder();
		forSign.append(directApplyRequest.getCharset());
		forSign.append(seperator);
		forSign.append(directApplyRequest.getSignType());
		forSign.append(seperator);
		forSign.append(directApplyRequest.getCustomerNo());
		forSign.append(seperator);
		forSign.append(directApplyRequest.getBatchNo());
		forSign.append(seperator);
		forSign.append(directApplyRequest.getCallbackUrl());
		forSign.append(seperator);
		forSign.append(directApplyRequest.getTotalAmount());
		forSign.append(seperator);
		forSign.append(applyInfoDetail.getId());
		forSign.append(seperator);
		forSign.append(applyInfoDetail.getProvince());
		forSign.append(seperator);
		forSign.append(applyInfoDetail.getCity());
		forSign.append(seperator);
		forSign.append(applyInfoDetail.getBranchName());
		forSign.append(seperator);
		forSign.append(applyInfoDetail.getBankName());
		forSign.append(seperator);
		forSign.append(applyInfoDetail.getAccountType());
		forSign.append(seperator);
		forSign.append(applyInfoDetail.getBankUserName());
		forSign.append(seperator);
		forSign.append(applyInfoDetail.getBankAccount());
		forSign.append(seperator);
		forSign.append(applyInfoDetail.getAmount());
		forSign.append(seperator);
		forSign.append(applyInfoDetail.getRemark());
		forSign.append(seperator);

		forSign.append(key);

		return Encodes.encodeHex(Digests.md5(forSign.toString().getBytes(Charset.forName("UTF-8")))).toUpperCase();

	}

}
