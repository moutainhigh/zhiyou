package com.zy.common.support.shengpay;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2016/12/26.
 */
@Getter
@Setter
public class BatchPaymentyNotify {

	String batchNo; // batchNo	String	BN20120818032545	批次号
	String statusCode; // statusCode	String	C	流程状态编码见9.4
	String statusName; // statusName	String	处理完成	流程状态描述
	String fileName; // fileName	String	20120817_20120807030545_R12013.csv.des	文件名
	String charset; // charset	String	utf-8	字符集
	String signType; // signType	String	MD5	报文验名类型,支持MD5，RSA
	String sign; // sign	String	3ADA5A08298023B0596775DF49A3200F
	String resultCode; // resultCode	String	S001	批次结果编码见9.5
	String resultName; // resultName	String	成功	批次结果描述
	String resultMemo; // resultMemo	String	成功	批次备注

}
