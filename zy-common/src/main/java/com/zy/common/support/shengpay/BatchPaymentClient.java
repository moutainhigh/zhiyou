package com.zy.common.support.shengpay;

import com.shengpay.mcl.bp.api.BatchPayment;
import com.shengpay.mcl.bp.api.BatchPaymentService;
import com.shengpay.mcl.bp.api.DirectApplyRequest;
import com.shengpay.mcl.btc.response.DirectApplyResponse;
import com.zy.common.util.JsonUtils;

import javax.xml.namespace.QName;
import java.net.URL;

/**
 * Created by Administrator on 2016/12/28.
 */
public class BatchPaymentClient {

	private static final QName SERVICE_NAME = new QName("http://api.bp.mcl.shengpay.com/", "BatchPaymentService");

	private BatchPayment port;

	public BatchPaymentClient() {

		URL wsdlURL = BatchPaymentService.WSDL_LOCATION;

		BatchPaymentService ss = new BatchPaymentService(wsdlURL, SERVICE_NAME);
		port = ss.getBatchPaymentPort();
	}


	public DirectApplyResponse directApply(DirectApplyRequest directApplyRequest) {
		return port.directApply(directApplyRequest);
	}

	public static void main(String[] args) {
		BatchPaymentClient batchPaymentClient = new BatchPaymentClient();
		DirectApplyResponse directApplyResponse = batchPaymentClient.directApply(new DirectApplyRequest());
		System.out.println(JsonUtils.toJson(directApplyResponse));
	}


}
