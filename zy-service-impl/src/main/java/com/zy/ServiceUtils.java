package com.zy;

import com.zy.common.util.Digests;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Random;

public class ServiceUtils {
	
	public static Random random = new Random();

	public static String generateOrderSn() {
		return generateSn("DD");
	}
	
	public static String generatePaymentSn() {
		return generateSn("ZF");
	}

	public static String generateWithdrawSn() {
		return generateSn("TX");
	}

	public static String generateDepositSn() {
		return generateSn("CZ");
	}

	public static String generateTransferSn() {
		return generateSn("ZZ");
	}

	public static String generateProfitSn() {
		return generateSn("SY");
	}
	
	private static String generateSn(String prefix) {
		return prefix + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "-" + StringUtils.leftPad(String.valueOf(random.nextInt(10000)), 4, '0');
	}
	
	public static String hashPassword(String plainPassword) {
		return Digests.md5Hex(plainPassword.getBytes(Charset.forName("UTF-8")));
		
	}
	
	public static String hashPayPassword(String plainPayPassword) {
		return Digests.md5Hex(plainPayPassword.getBytes(Charset.forName("UTF-8")));
	}


}
