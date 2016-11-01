package com.zy.consumer.component;

import com.zy.Config;
import com.zy.common.support.sms.SmsSupport;
import com.zy.consumer.extend.AbstractConsumer;
import com.zy.entity.fnc.BankCard;
import com.zy.entity.fnc.Deposit;
import com.zy.entity.fnc.PayType;
import com.zy.entity.mal.Order;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.entity.usr.UserSetting;
import com.zy.service.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.zy.entity.fnc.PayType.银行汇款;
import static com.zy.model.Constants.*;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.slf4j.LoggerFactory.getLogger;

/* 消息consumer */
@Component
public class SmsConsumer extends AbstractConsumer {

    private Logger logger = getLogger(SmsConsumer.class);


    @Autowired
    private UserSettingService userSettingService;

    @Autowired
    private UserService userService;


    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private SmsSupport smsSupport;

    @Autowired
    private BankCardService bankCardService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private DepositService depositService;

    @Autowired
    private Config config;

    // String TOPIC_DEPOSIT_SUCCESS = "deposit-success";
    //String TOPIC_DEPOSIT_OFFLINE_REJECTED = "deposit-offline-rejected";


    /**
     * String TOPIC_USER_INFO_CONFIRMED = "user-info-confirmed"; // 实名认证通过
     * String TOPIC_USER_INFO_REJECTED = "user-info-rejected"; // 实名认证未通过
     * String TOPIC_BANKCARD_CONFIRMED = "bankCard-confirmed"; // 银行卡审核通过
     * String TOPIC_BANKCARD_REJECTED = "bankCard-rejected"; // 银行卡审核未通过
     * String TOPIC_ORDER_PAID = "order-paid"; // 订单已支付
     * String TOPIC_ORDER_DELIVERED = "order-delivered"; // 订单已发货
     */

    public SmsConsumer() {
        super(SmsConsumer.class.getSimpleName()
                , TOPIC_USER_INFO_CONFIRMED
                , TOPIC_USER_INFO_REJECTED
                , TOPIC_BANKCARD_CONFIRMED
                , TOPIC_BANKCARD_REJECTED
                , TOPIC_ORDER_PAID
                , TOPIC_ORDER_DELIVERED
                , TOPIC_DEPOSIT_SUCCESS
                , TOPIC_DEPOSIT_OFFLINE_REJECTED
        );
    }

    @Override
    protected void doHandle(String topic, long refId, String token, String version) {
        switch (topic) {
            case TOPIC_USER_INFO_CONFIRMED: {//实名认证通过
                handleAppearance(topic, refId, token, version, "恭喜,您提交的实名认证请求【审核通过】");
                break;
            }
            case TOPIC_USER_INFO_REJECTED: {
                handleAppearance(topic, refId, token, version, "抱歉,您提交的实名认证请求【审核未通过】,请前往平台完善信息吧");
                break;
            }
            case TOPIC_BANKCARD_CONFIRMED: {
                handleBankCard(topic, refId, token, version, "恭喜,您提交的银行卡认证请求【审核通过】");
                break;
            }
            case TOPIC_BANKCARD_REJECTED: {
                handleBankCard(topic, refId, token, version, "抱歉,您提交的银行卡认证请求【审核未通过】，请前往平台完善信息吧");
                break;
            }
            case TOPIC_ORDER_PAID: {
                final Order order = orderService.findOne(refId);
                if (isNull(order)) {
                    warn(topic, refId, token, version);
                } else {
                    Long sellerId = order.getSellerId();
                    if (!sellerId.equals(config.getSysUserId())) {
                        if (sellerId != null && sellerId > 0) {
                            doSendSms(sellerId, format("订单【%s】已经完成付款,请尽快发货", order.getSn()));
                        }
                    }
                }
                break;
            }
            case TOPIC_ORDER_DELIVERED: {
                final Order order = orderService.findOne(refId);
                if (isNull(order)) {
                    warn(topic, refId, token, version);
                } else {
                    doSendSms(order.getUserId(), format("订单【%s】已经发货", order.getSn()));
                }

                break;
            }
            case TOPIC_DEPOSIT_SUCCESS: {
                Deposit deposit = depositService.findOne(refId);
                if (isNull(deposit)) {
                    warn(topic, refId, token, version);
                }
                if (deposit.getPayType() == 银行汇款) {
                    doSendSms(deposit.getUserId(), format("恭喜,您提交的银行汇款充值单已被【审核通过】,单号【%s】", deposit.getSn()));
                }
                break;
            }
            case TOPIC_DEPOSIT_OFFLINE_REJECTED: {
                Deposit deposit = depositService.findOne(refId);
                if (isNull(deposit)) {
                    warn(topic, refId, token, version);
                }
                if (deposit.getPayType() == 银行汇款) {
                    doSendSms(deposit.getUserId(), format("抱歉,您提交的银行汇款充值单【审核未通过】,单号【%s】", deposit.getSn()));
                }
                break;
            }
        }
    }

    private void handleBankCard(String topic, long refId, String token, String version, String message) {
        final BankCard bankCard = bankCardService.findOne(refId);
        if (isNull(bankCard)) {
            warn(topic, refId, token, version);
        } else {
            doSendSms(bankCard.getUserId(), message);
        }
    }

    private void handleAppearance(String topic, long refId, String token, String version, String message) {
        UserInfo userInfo = userInfoService.findOne(refId);
        if (isNull(userInfo)) {
            warn(topic, refId, token, version);
        } else {
            final Long userId = userInfo.getUserId();
            doSendSms(userId, message);
        }
    }

    private void doSendSms(Long userId, String message) {
        final UserSetting userSetting = userSettingService.createIfAbsent(userId);
        if (userSetting.getIsReceiveTaskSms()) {
            final User user = userService.findOne(userId);
            final String phone = user.getPhone();
            if (isNotBlank(phone)) {
                smsSupport.send(phone, message, "智优生物");
            }
        }
    }


}

