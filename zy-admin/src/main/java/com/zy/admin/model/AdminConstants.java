package com.zy.admin.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AdminConstants {

	
	public static final String[] IMAGE_UPLOAD_ALLOWED_EXTENTIONS = new String[] {"jpg", "png", "jpeg", "bmp", "gif"};
	public static final long IMAGE_UPLOAD_MAX_SIZE = 5 * 1024 *1024;
	
	public static final Map<String, Map<String, String>> SETTING_PERMISSION_MAP;
	
	static {
		
		SETTING_PERMISSION_MAP = new HashMap<>();
		Map<String, String> sub;

		{
			sub = new LinkedHashMap<>();
			sub.put("user:view", "用户信息(查看)");
			sub.put("user:edit", "用户信息(编辑)");
			sub.put("user:addVip", "用户信息(加VIP)");
			sub.put("user:freeze", "用户信息(冻结账号)");
			sub.put("user:modifyParent", "用户信息(修改推荐人)");
			sub.put("user:setDirector", "用户信息(升级董事)");
			sub.put("user:setShareholder", "用户信息(升级股东)");
			sub.put("user:setBoss", "用户信息(设置总经理)");
			sub.put("user:export", "用户信息(导出)");
			sub.put("userInfo:view", "用户认证(查看)");
			sub.put("userInfo:edit", "用户认证(编辑)");
			sub.put("userInfo:confirm", "用户认证(审核)");
			sub.put("address:view", "地址管理(查看)");
	 		SETTING_PERMISSION_MAP.put("用户中心", sub);
		}
		{
			sub = new LinkedHashMap<>();
			sub.put("product:view", "商品查看");
			sub.put("product:edit", "商品编辑");
			sub.put("product:modifyPrice", "商品价格修改");
			sub.put("product:on", "商品上架");
			sub.put("order:view", "订单查看");
			sub.put("order:deliver", "平台订单发货");
			sub.put("order:refund", "订单退款");
			sub.put("orderFillUser:view", "用户补单管理(查看)");
			sub.put("orderFillUser:edit", "用户补单管理(编辑)");
			sub.put("productReplacement:view", "换货管理(查看)");
			sub.put("productReplacement:deliver", "换货管理(发货)");
			sub.put("productReplacement:reject", "换货管理(驳回)");
	 		SETTING_PERMISSION_MAP.put("下单中心", sub);
		}
		{
			sub = new LinkedHashMap<>();
			sub.put("activity:view", "活动查看");
			sub.put("activity:edit", "活动编辑");
			sub.put("activityReport:view", "活动报表查看");
			sub.put("activityReport:export", "活动报表导出");
			sub.put("activityTicket:view", "活动票务管理(查看)");
			sub.put("activityTicket:edit", "活动票务管理(编辑)");
			sub.put("activityApply:view", "活动报名管理(查看)");
			sub.put("activityApply:edit", "活动报名管理(新增)");
			sub.put("policy:view", "保险单管理(查看)");
			sub.put("policy:export", "保险单管理(导出)");
			sub.put("policy:modify", "保险单管理(编辑)");
			sub.put("policyCode:view", "保险单号管理(查看)");
			sub.put("policyCode:edit", "保险单号管理(编辑)");
			sub.put("policyCode:export", "保险单号管理(导出)");
			sub.put("report:view", "检测报告查看");
			sub.put("report:edit", "检测报告编辑");
			sub.put("report:preConfirm", "检测报告预审");
			sub.put("report:confirm", "检测报告终审");
			sub.put("report:export", "检测报告导出");
			sub.put("report:checkReportResult", "检测报告(客服检测)");
			sub.put("report:visitUser", "检测报告(回访客服分配)");
			sub.put("reportVisitedLog:view", "回访日志(查看)");
			sub.put("reportVisitedLog:edit", "回访日志(编辑)");
			sub.put("lesson:view", "课程管理");
			sub.put("lesson:edit", "课程编辑");
			sub.put("lessonUser:view", "用户课程管理");
			sub.put("lessonUser:edit", "用户课程编辑");
			SETTING_PERMISSION_MAP.put("活动管理", sub);
		}
		{
			sub = new LinkedHashMap<>();
			sub.put("banner:view", "banner管理(查看)");
			sub.put("banner:edit", "banner管理(编辑)");
			sub.put("notice:view", "公告管理(查看)");
			sub.put("notice:edit", "公告管理(编辑)");
			sub.put("article:view", "新闻查看");
			sub.put("article:edit", "新闻编辑");
			sub.put("article:release", "新闻发布");
			sub.put("feedback:view", "反馈管理");
			sub.put("help:view", "帮助中心(查看)");
			sub.put("help:edit", "帮助中心(编辑)");
	 		SETTING_PERMISSION_MAP.put("内容管理", sub);
		}
		{
			sub = new LinkedHashMap<>();
			sub.put("account:view", "账户查看");
			sub.put("account:deposit", "账户充值");
			sub.put("deposit:view", "充值查看");
			sub.put("deposit:confirmPaid", "充值单确认支付");
			sub.put("deposit:export", "充值导出");
			sub.put("withdraw:view", "提现查看");
			sub.put("withdraw:confirm", "操作提现");
			sub.put("withdraw:push", "提现推送");
			sub.put("profit:view", "收益查看");
			sub.put("profit:grant", "收益发放");
			sub.put("profit:cancel", "收益取消");
			sub.put("accountLog:view", "流水查看");
			sub.put("accountLog:export", "流水导出");
			sub.put("bankCard:view", "绑定银行信息(查看)");
			sub.put("bankCard:confirm", "绑定银行信息(审核)");
			sub.put("payment:view", "支付单查看");
			sub.put("payment:confirmPaid", "支付单确认支付");
			sub.put("transfer:view", "转账单管理");
	 		SETTING_PERMISSION_MAP.put("财务管理", sub);
		}
		{
			sub = new LinkedHashMap<>();
			sub.put("userTreeReport:view", "用户树");
			sub.put("v4TreeReport:view", "特级树");
			sub.put("orderReport:view", "服务商进货报表");
			sub.put("orderReport:export", "服务商进货报表导出");
			sub.put("teamReport:view", "特级服务商人数下线报表");
			sub.put("teamReport:export", "特级服务商人数下线报表导出");
			sub.put("userUpgradeReport:view", "服务商数量统计报表");
			sub.put("userUpgradeReport:export", "服务商数量统计报表导出");
			sub.put("financeReport:view", "财务报表");
			sub.put("financeReport:export", "财务报表(导出)");
			sub.put("orderQuantity:view", "订单核算报表");
			sub.put("orderQuantity:export", "订单核算报表(导出)");
			sub.put("cityAgent:view", "服务商活跃数量排行");
			sub.put("profitMOM:view", "收入环比");
			sub.put("v4Activity:view", "特级服务商团队活跃度报表-总经理");
			sub.put("teamOrderMonth:view", "团队月销量及环比-总经理");
			sub.put("teamOrderDaily:view", "团队日销量及环比-总经理");
			sub.put("activitySummaryReport:view", "活动汇总报表");
			sub.put("activitySummaryReport:export", "活动汇总报表导出");
			/*sub.put("financeReport:export", "财务导出");*/
	 		SETTING_PERMISSION_MAP.put("报表管理", sub);
		}
		{
			sub = new LinkedHashMap<>();
			sub.put("admin:*", "管理员管理");
			sub.put("role:*", "角色管理");
			sub.put("site:*", "站点管理");
			sub.put("message:view", "消息管理");
			sub.put("message:edit", "消息编辑");
			sub.put("setting:*", "系统设置");
			sub.put("area:*", "区域管理");
			sub.put("bank:view", "银行信息(查看)");
			sub.put("bank:edit", "银行信息(编辑)");
			sub.put("tag:view", "标签管理(查看)");
			sub.put("tag:edit", "标签管理(编辑)");
			sub.put("tag:delete", "标签管理(删除)");
			sub.put("job:view", "职位管理(查看)");
			sub.put("job:edit", "职位管理(编辑)");
			SETTING_PERMISSION_MAP.put("系统管理", sub);
		}
	}
}
