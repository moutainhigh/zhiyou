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
		return generateSn("R");
	}
	
	public static String generatePaymentSn() {
		return generateSn("P");
	}

	public static String generateWithdrawSn() {
		return generateSn("W");
	}

	public static String generateDepositSn() {
		return generateSn("D");
	}

	public static String generateProfitSn() {
		return generateSn("R");
	}
	
	private static String generateSn(String prefix) {
		return prefix + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + StringUtils.leftPad(String.valueOf(random.nextInt(1000000)), 6, '0');
	}
	
	public static String hashPassword(String plainPassword) {
		return Digests.md5Hex(plainPassword.getBytes(Charset.forName("UTF-8")));
		
	}
	
	public static String hashPayPassword(String plainPayPassword) {
		return Digests.md5Hex(plainPayPassword.getBytes(Charset.forName("UTF-8")));
	}


}
