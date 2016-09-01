package com.zy.model;

import com.zy.common.model.result.ResultCode;

public interface BizCode extends ResultCode {
	
	int INSUFFICIENT_BALANCE = 10001; // 余额不足, 请充值 返回值需要带上
	
	int SETTING_ERROR = 10002; // 系统参数错误
	
	int RED_PACKET_IS_EMPTY = 10003; // 红包被抢空

}
