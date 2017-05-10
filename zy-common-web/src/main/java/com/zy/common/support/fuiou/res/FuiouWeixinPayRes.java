package com.zy.common.support.fuiou.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuiouWeixinPayRes {

	private String result_msg;
	private String result_code;
	private String sdk_noncestr;
	private String sdk_appid;
	private String sdk_partnerid;
	private String sdk_timestamp;
	private String sdk_package;
	private String sdk_paysign;
	private String sdk_signtype;
	private String ins_cd;
	private String txn_begin_ts;
	private String session_id;
	private String term_id;
	private String sub_appid;
	private String mchnt_cd;
	private String sub_openid;
	private String reserved_fy_order_no;
	private String reserved_fy_settle_dt;
	private String mchnt_order_no;
	private String sub_mer_id;
	private String qr_code;
	private String random_str;
	private String reserved_transaction_id;

}
