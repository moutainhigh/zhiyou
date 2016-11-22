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
			sub.put("user:modifyParent", "用户信息(修改上级)");
			sub.put("user:setRoot", "用户信息(修改子系统)");
			sub.put("user:export", "用户信息(导出)");
			sub.put("userInfo:view", "用户认证(查看)");
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
	 		SETTING_PERMISSION_MAP.put("下单中心", sub);
		}
		{
			sub = new LinkedHashMap<>();
			sub.put("activity:view", "活动查看");
			sub.put("activity:edit", "活动编辑");
			sub.put("policy:view", "保险单管理(查看)");
			sub.put("policy:export", "保险单管理(导出)");
			sub.put("policyCode:view", "保险单号管理(查看)");
			sub.put("policyCode:edit", "保险单号管理(编辑)");
			sub.put("policyCode:export", "保险单号管理(导出)");
			sub.put("report:view", "检测报告查看");
			sub.put("report:edit", "检测报告编辑");
			sub.put("report:preConfirm", "检测报告预审");
			sub.put("report:confirm", "检测报告终审");
			sub.put("report:export", "检测报告导出");
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
			sub.put("orderReport:view", "服务商进货报表");
			sub.put("orderReport:export", "服务商进货报表导出");
			sub.put("teamReport:view", "特级服务商人数下线报表");
			sub.put("teamReport:export", "特级服务商人数下线报表导出");
			sub.put("userUpgradeReport:view", "服务商数量统计报表");
			sub.put("userUpgradeReport:export", "服务商数量统计报表导出");
			sub.put("financeReport:view", "财务报表");
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
