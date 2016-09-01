package com.zy.common.support.weixinpay;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateAdapter extends XmlAdapter<String, Date> {

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	
	@Override
	public Date unmarshal(String v) throws Exception {
		// TODO Auto-generated method stub
		return simpleDateFormat.parse(v);
	}

	@Override
	public String marshal(Date v) throws Exception {
		// TODO Auto-generated method stub
		return simpleDateFormat.format(v);
	}
	
}