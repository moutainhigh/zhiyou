package com.zy.admin.model;

import java.util.HashMap;
import java.util.Map;

public class AdminConstants {

	
	public static final String[] IMAGE_UPLOAD_ALLOWED_EXTENTIONS = new String[] {"jpg", "png", "jpeg", "bmp", "gif"};
	public static final long IMAGE_UPLOAD_MAX_SIZE = 5 * 1024 *1024;
	
	public static final Map<String, Map<String, String>> SETTING_PERMISSION_MAP;
	
	static {
		
		SETTING_PERMISSION_MAP = new HashMap<>();
		Map<String, String> sub;

		{
			sub = new HashMap<>();
			sub.put("user:view", "用户信息(查看)");
			sub.put("user:edit", "用户信息(编辑)");
			sub.put("user:addVip", "用户信息(加VIP)");
			sub.put("user:freeze", "用户信息(冻结账号)");
			sub.put("user:export", "用户信息(导出)");
			sub.put("portrait:view", "用户画像(查看)");
			sub.put("appearance:view", "实名认证(查看)");
			sub.put("appearance:confirm", "实名认证(审核)");
			sub.put("tag:view", "标签管理(查看)");
			sub.put("tag:edit", "标签管理(编辑)");
			sub.put("tag:delete", "标签管理(删除)");
			sub.put("job:view", "职位管理(查看)");
			sub.put("job:edit", "职位管理(编辑)");
			sub.put("address:view", "地址管理(查看)");
	 		SETTING_PERMISSION_MAP.put("用户中心", sub);
		}
		{
			sub = new HashMap<>();
			sub.put("product:view", "商品查看");
			sub.put("product:edit", "商品编辑");
			sub.put("order:view", "订单查看");
			sub.put("order:deliver", "订单发货");
			sub.put("agent:view", "代理信息(查看)");
			sub.put("agent:edit", "代理信息(编辑)");
			sub.put("agent:addVip", "代理信息(加VIP)");
			sub.put("agent:freeze", "代理信息(冻结账号)");
			sub.put("agent:export", "代理信息(导出)");
	 		SETTING_PERMISSION_MAP.put("下单中心", sub);
		}
		{
			sub = new HashMap<>();
			sub.put("activity:view", "活动查看");
			sub.put("activity:edit", "活动编辑");
			sub.put("report:view", "检测报告查看");
			sub.put("report:confirm", "检测报告最终审核");
			sub.put("report:preConfirm", "检测报告预审核");
			SETTING_PERMISSION_MAP.put("活动管理", sub);
		}
		{
			sub = new HashMap<>();
			sub.put("banner:view", "banner管理(查看)");
			sub.put("banner:edit", "banner管理(编辑)");
			sub.put("notice:view", "公告管理(查看)");
			sub.put("notice:edit", "公告管理(编辑)");
			sub.put("article:view", "新闻查看");
			sub.put("article:edit", "新闻编辑");
			sub.put("feedback:view", "反馈管理");
			sub.put("help:view", "帮助中心(查看)");
			sub.put("help:edit", "帮助中心(编辑)");
	 		SETTING_PERMISSION_MAP.put("内容管理", sub);
		}
		{
			sub = new HashMap<>();
			sub.put("account:view", "账户查看");
			sub.put("account:deposit", "账户充值");
			sub.put("deposit:view", "充值查看");
			sub.put("deposit:confirmPaid", "充值单确认支付");
			sub.put("deposit:export", "充值导出");
			sub.put("withdraw:view", "提现查看");
			sub.put("withdraw:confirm", "操作提现");
			sub.put("profit:view", "收益查看");
			sub.put("accountLog:view", "流水查看");
			sub.put("accountLog:export", "流水导出");
			sub.put("bank:view", "银行信息(查看)");
			sub.put("bank:edit", "银行信息(编辑)");
			sub.put("bankCard:view", "绑定银行信息(查看)");
			sub.put("bankCard:confirm", "绑定银行信息(审核)");
			sub.put("payment:view", "支付单查看");
			sub.put("payment:confirmPaid", "支付单确认支付");
			sub.put("transfer:view", "转账单管理");
	 		SETTING_PERMISSION_MAP.put("财务管理", sub);
		}
		{
			sub = new HashMap<>();
			sub.put("admin:*", "管理员管理");
			sub.put("role:*", "角色管理");
			sub.put("site:*", "站点管理");
			sub.put("message:view", "消息管理");
			sub.put("message:edit", "消息编辑");
			sub.put("setting:*", "系统设置");
			sub.put("area:*", "区域管理");
			SETTING_PERMISSION_MAP.put("系统管理", sub);
		}
	}
}
