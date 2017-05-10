package com.zy.model;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.asList;

public  interface Constants {

    /* session attribute */
    String SESSION_ATTRIBUTE_CAPTCHA = "captcha";
    String SESSION_ATTRIBUTE_REGISTER_SMS = "registerSms";
    String SESSION_ATTRIBUTE_BIND_PHONE_SMS = "bindPhoneSms";
    String SESSION_ATTRIBUTE_PRINCIPAL = "principal";
    String SESSION_ATTRIBUTE_NEED_CAPTCHA = "needCaptcha";
    String SESSION_ATTRIBUTE_REDIRECT_URL = "redirectUrl";
    String SESSION_ATTRIBUTE_SOCIAL_USER_INFO = "socialUserInfo";
    String SESSION_ATTRIBUTE_FIND_PASSWORD_SMS = "findPasswordSmsCode";
    String SESSION_ATTRIBUTE_FIND_PASSWORD_USER_ID = "findPasswordUserId";
    String SESSION_ATTRIBUTE_AGENT_REGISTER_DTO = "agentRegisterDto";
    String SESSION_ATTRIBUTE_INVITER_ID = "inviterId";

    /* weixin */
    String WEIXIN_STATE_BASE = "XZYabc";
    String WEIXIN_STATE_USERINFO = "abcXZY";

    /* cookie name */
    String COOKIE_NAME_TGT = "__tgt";
    String COOKIE_NAME_INVITATION = "__u";
    String COOKIE_NAME_MOBILE_TOKEN = "__m_token";

    /* request attribute */
    String REQUEST_ATTRIBUTE_INVITER_ID = "__inviterId";

    /* model attribute */
    String MODEL_ATTRIBUTE_RESULT = "result";
    String MODEL_ATTRIBUTE_ERROR_MESSAGE = "errorMessage";

    /* cache name */
    String CACHE_NAME_SUPER_LOGIN_TOKEN = "superLoginToken";
    String CACHE_NAME_LOGIN_FAILURE_TIMES = "loginFailureTimes";
    String CACHE_NAME_REGISTER_SMS_LAST_SEND_TIME = "registerSmsLastSendTime";
    String CACHE_NAME_BIND_PHONE_SMS_LAST_SEND_TIME = "bindPhoneSmsLastSendTime";
    String CACHE_NAME_FIND_PASSWORD_LAST_SEND_TIME = "findPassWordLastSendTime";
    String CACHE_NAME_STATISTICS = "statistics";

    String CACHE_NAME_TGT = "tgt";

    String CACHE_NAME_USER = "user";
    String CACHE_NAME_BANK_CARD = "bankCard";
    String CACHE_NAME_NOTICE = "notice";
    String CACHE_NAME_AREA = "area";
    String CACHE_NAME_HELP_CATEGORY = "helpCategory";
    String CACHE_NAME_HELP = "help";
    String CACHE_NAME_BANNER = "banner";
    String CACHE_NAME_ADDRESS = "address";
    String CACHE_NAME_JOB = "job";
    String CACHE_NAME_TAG = "tag";
    String CACHE_NAME_ACTIVITY = "activity";

	String CACHE_NAME_REGISTER_CHART = "registerChart";
	String CACHE_NAME_ORDER_CHART = "orderChart";
	String CACHE_NAME_AGENT_REGISTER_COUNT = "agentRegisterCount";
	String CACHE_NAME_USER_BANK_INFO_COUNT = "userBankInfoCount";
	String CACHE_NAME_BANK = "bank";
    String CACHE_NAME_USER_INFO_COUNT = "userInfoCount";
    String CACHE_NAME_REPORT_PRE_COUNT = "reportPreCount";
    String CACHE_NAME_REPORT_COUNT = "reportCount";
	String CACHE_NAME_ORDER_PLATFORM_DELIVER_COUNT = "orderPlatformDeliverCount";
	String CACHE_NAME_PROFIT_CHART = "profitChart";
	String CACHE_NAME_WITHDRAW_COUNT = "withdrawCount";
	String CACHE_NAME_PAYMENT_COUNT = "paymentCount";
	String CACHE_NAME_DEPOSIT_COUNT = "depositCount";
	
    /* order */
    int SETTING_ORDER_EXPIRE_IN_MINUTES = 60 * 24 * 30; // 订单过期 30天
    int SETTING_PAYMENT_EXPIRE_IN_MINUTES = 60 * 24 * 7; // 支付单过期时间 7天
    int SETTING_PAYMENT_OFFLINE_EXPIRE_IN_MINUTES = 60 * 24 * 15; // 银行汇款支付单过期时间 15天
    int SETTING_DEPOSIT_OFFLINE_EXPIRE_IN_MINUTES = 60 * 24 * 7; // 银行汇款支付单过期时间 15天
    
    /* weixin pay */
    String WEIXIN_PAY_NOTIFY = "http://www.zhi-you.net/notify/weixinPay";
    int WEIXIN_PAY_EXPIRE_IN_MINUTES = 60 * 2;

    /* fy weixin wap pay */
    String FY_WEIXIN_PAY_NOTIFY = "http://agentsystem.zhi-you.net/notify/fyWeixinPay";

    /* weixin wap pay */
    String WEIXIN_MP_PAY_NOTIFY = "http://m.zhi-you.net/notify/weixinPay";
    String WEIXIN_MP_PAY_STATE = "weixinMpPay";

    /* alipay */
    String ALIPAY_NOTIFY = "http://www.zhi-you.net/notify/alipay";
    String ALIPAY_RETURN = "http://www.zhi-you.net/notify/alipay/payReturn";
    String ALIPAY_SITE_NAME = "智优生物";
    String ALIPAY_SITE_URL = "http://www.zhi-you.net";
    int ALIPAY_EXPIRE_IN_MINUTES = 60 * 10;

    /* alipay wap */
    String ALIPAY_WAP_NOTIFY = "http://m.zhi-you.net/notify/alipay";
    String ALIPAY_WAP_RETURN = "http://m.zhi-you.net/notify/alipay/payReturn";
    String ALIPAY_WAP_SITE_NAME = "智优生物";
    String ALIPAY_WAP_SITE_URL = "http://m.zhi-you.net";

    /* shengPay */
    String SHENGPAY_NOTIFY = "http://agentsystem.zhi-you.net/notify/shengPay/async";
    String SHENGPAY_RETURN = "http://agentsystem.zhi-you.net/notify/shengPay/sync";
    String SHENGPAY_BATCH_PAYMENT_NOTIFY = "http://adminsystem.zhi-you.net/notify/shengPay/batchPayment";

    String SHENGPAY_NOTIFY_MOBILE = "http://agentsystem.zhi-you.net/notify/shengPay/mobile/async";
    String SHENGPAY_RETURN_MOBILE = "http://agentsystem.zhi-you.net/notify/shengPay/mobile/sync";

    /* social login notify url */
    String WEIXIN_LOGIN_NOTIFY = "http://passport.zhi-you.net/notify/weixin";
    String QQ_LOGIN_NOTIFY = "http://passport.zhi-you.net/notify/qq";
    String WEIXIN_MP_LOGIN_NOTIFY = "http://agentsystem.zhi-you.net/notify/weixinMp";

    /* aliyun oss */
    String ALIYUN_BUCKET_NAME_IMAGE = "zy-image";
    String ALIYUN_URL_IMAGE = "http://image.zhi-you.net";
    String ALIYUN_BUCKET_NAME_STATIC = "zy-static";
    String ALIYUN_URL_STATIC = "http://state.zhi-you.net";

    /* domain and url */
    String DOMAIN_ROOT = "zhi-you.net";
    String DOMAIN_MOBILE = "agentsystem.zhi-you.net";
    String URL_MOBILE = "http://agentsystem.zhi-you.net";

    /* setting */
    String SETTING_SYS_NAME = "智优生物";
    Long SETTING_SUPER_ADMIN_ID = 1L;
    Long SETTING_SETTING_ID = 1L;
    int SETTING_LOGIN_FAILURE_VALIDATE_TIMES = 5;
    String SETTING_DEFAULT_AVATAR = "http://image.zhi-you.net/avatar_default.jpg";
    String SETTING_NOTIFY_VERSION = "1.0.0";

    int SETTING_OLD_MIN_QUANTITY = 100;
    int SETTING_NEW_MIN_QUANTITY = 80;


    BigDecimal SETTING_MAX_WITHDRAW_FEE_RATE = new BigDecimal("0.05"); // 提现最大费率 设置不可超过该值
    BigDecimal SETTING_MIN_MERCHANT_DEPOSIT_MONEY = new BigDecimal("100.00"); // 商家充值本金最小金额

    BigDecimal FEE_RATE = new BigDecimal("0.06");

    /*
     * topic 用于发送mq
     *
     * */
    String TOPIC_REGISTER_SUCCESS = "register-success"; // 注册成功
    String TOPIC_USER_RANK_CHANGED = "user-rank-changed"; // 代理等级更新

    String TOPIC_USER_INFO_CONFIRMED = "user-info-confirmed"; // 实名认证通过
    String TOPIC_USER_INFO_REJECTED = "user-info-rejected"; // 实名认证未通过

    String TOPIC_BANKCARD_CONFIRMED = "bankCard-confirmed"; // 银行卡审核通过
    String TOPIC_BANKCARD_REJECTED = "bankCard-rejected"; // 银行卡审核未通过

    String TOPIC_ORDER_OFFLINE_REJECTED = "order-offline-rejected";
    String TOPIC_ORDER_PAID = "order-paid"; // 订单已支付
    String TOPIC_ORDER_DELIVERED = "order-delivered"; // 订单已发货
    String TOPIC_ORDER_RECEIVED = "order-received";

    String TOPIC_DEPOSIT_SUCCESS = "deposit-success";
    String TOPIC_DEPOSIT_OFFLINE_REJECTED = "deposit-offline-rejected";

    String TOPIC_POLICY_EXPIRE_SOON = "policy-expire-soon"; // 保险单即将过期提醒

    List<String> relationshipList = asList(new String[]{"本人", "亲属", "朋友", "其他"});
    List<String> restTimeLabelList = asList(new String[]{"良好(11:00点前)", "一般(11:00-12:00)", "紊乱(12:00以后)", "其他"});
    List<String> sleepQualityList = asList(new String[]{"良好", "一般", "差", "其他"});
    List<String> drinkList = asList(new String[]{"经常", "偶尔", "不喝"});
    List<String> smokeList = asList(new String[]{"经常", "偶尔", "不抽"});
    List<String> exerciseList = asList(new String[]{"是", "没有", "其他"});
    List<String> hobbyList = asList(new String[]{"有", "没有"});
    List<String> causeList = asList(new String[]{"关注身体健康常规检测", "身体不适用于检测健康状况", "用于检测产品的精准度", "其他"});
    List<String> healthList = asList(new String[]{"良好", "一般", "不好"});
    List<String> sicknessList = asList(new String[]{"高血压", "高血脂", "高血糖"});
    List<String> familyHistoryList = asList(new String[]{"是", "否"});
    List<String> healthProductList = asList(new String[]{"是", "否"});
    List<String> monthlyCostList = asList(new String[]{"200元以下", "500-1000元", "200-500元", "1000元以上", "其他"});
    List<String> productSharingList = asList(new String[]{"会", "否", "其他"});
    List<String> toAgentList = asList(new String[]{"是: 有意愿", "否:暂时不考虑"});
    List<String> contactWayList = asList(new String[]{"电话", "微信号", "短信", "其他"});

    List<String> visitedStatusList = asList(new String[]{"回访成功", "电话号码有误", "忙音", "拒访", "无人接听"
        , "无此人", "语言不通", "需二访", "需三访", "关机", "停机空号", "没有使用过", "挂断", "本人检测无需回访", "由其他方式回访"
        , "国际号码", "重复上传", "无检测试剂", "请用产品配套比色卡"});
    List<String> visitContinueCheckList = asList(new String[]{"忙音", "无人接听", "需二访", "需三访", "关机", "挂断"});
    List<String> visitedSuccessList = asList(new String[]{"回访成功", "语言不通", "由其他方式回访", "本人检测无需回访", "国际号码"});

}
