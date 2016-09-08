package com.zy.model;

import java.math.BigDecimal;

public interface Constants {

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
    String CACHE_NAME_BANK = "bank";

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
    String CACHE_NAME_ACTIVITY = "activity";

    /* weixin pay */
    String WEIXIN_PAY_NOTIFY = "http://www.zhi-you.net/notify/weixinPay";
    int WEIXIN_PAY_EXPIRE_IN_MINUTES = 60 * 2;

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

    /* social login notify url */
    String WEIXIN_LOGIN_NOTIFY = "http://passport.zhi-you.net/notify/weixin";
    String QQ_LOGIN_NOTIFY = "http://passport.zhi-you.net/notify/qq";
    String WEIXIN_MP_LOGIN_NOTIFY = "http://passport.zhi-you.net/notify/weixinMp";

    /* aliyun oss */
    String ALIYUN_BUCKET_NAME_IMAGE = "zy-image";
    String ALIYUN_URL_IMAGE = "http://image.zhi-you.net";
    String ALIYUN_BUCKET_NAME_STATIC = "zy-static";
    String ALIYUN_URL_STATIC = "http://state.zhi-you.net";

    /* domain and url */
    String DOMAIN_ROOT = "zhi-you.net";
    String DOMAIN_MOBILE = "m.zhi-you.net";
    String DOMAIN_PC = "www.zhi-you.net";
    String DOMAIN_COOKIE = ".zhi-you.net";
    String URL_MOBILE = "http://m.zhi-you.net";
    String URL_PC = "http://www.zhi-you.net";
    String URL_PASSPORT = "http://passport.zhi-you.net";

    /* setting */
    String SETTING_SYS_NAME = "智优生物";
    Long SETTING_SUPER_ADMIN_ID = 1L;
    Long SETTING_SETTING_ID = 1L;
    int SETTING_LOGIN_FAILURE_VALIDATE_TIMES = 5;
    String SETTING_DEFAULT_AVATAR = "http://image.zhi-you.net/avatar_default.jpg";

    BigDecimal SETTING_MAX_WITHDRAW_FEE_RATE = new BigDecimal("0.05"); // 提现最大费率 设置不可超过该值
    BigDecimal SETTING_MIN_MERCHANT_DEPOSIT_MONEY = new BigDecimal("100.00"); // 商家充值本金最小金额


    String BIZ_NAME_WITHDRAW = "提现";
    String BIZ_NAME_ORDER_PAY = "订单支付";
    String BIZ_NAME_ORDER_PROFIT = "订单分润";




    /*
     * topic 用于发送mq
     *
     * */
    String TOPIC_REGISTER_SUCCESS = "register-success"; // 注册成功
    String TOPIC_LOGIN_SUCCESS = "login-success"; // 登录成功
    String TOPIC_USER_RANK_CHANGED = "user-rank-changed";

    /* 用户信息 */
    String TOPIC_PORTRAIT_COMPLETED = "portrait-completed"; // 完成自我画像
    String TOPIC_APPEARANCE_CONFIRMED = "appearance-confirmed"; // 当颜值认证通过
    String TOPIC_APPEARANCE_REJECTED = "appearance-rejected"; // 当颜值认证未通过
    String TOPIC_PHONE_BOUND = "phone_bound"; //绑定手机成功

}
