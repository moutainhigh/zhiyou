package com.zy.consumer.component;

import com.zy.common.support.sms.SmsSupport;
import com.zy.consumer.extend.AbstractConsumer;
import com.zy.entity.fnc.BankCard;
import com.zy.entity.mal.Order;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.entity.usr.UserSetting;
import com.zy.service.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.zy.model.Constants.*;
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


    /**
     String TOPIC_USER_INFO_CONFIRMED = "user-info-confirmed"; // 实名认证通过
     String TOPIC_USER_INFO_REJECTED = "user-info-rejected"; // 实名认证未通过
     String TOPIC_BANKCARD_CONFIRMED = "bankCard-confirmed"; // 银行卡审核通过
     String TOPIC_BANKCARD_REJECTED = "bankCard-rejected"; // 银行卡审核未通过
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
                    Long parentId = userService.findOne(order.getUserId()).getParentId();
                    if (parentId != null && parentId > 0) {
                        doSendSms(parentId, String.format("订单【%s】已经完成付款,请尽快发货", order.getSn()));
                    }
                }
                break;
            }
            case TOPIC_ORDER_DELIVERED: {
                final Order order = orderService.findOne(refId);
                if (isNull(order)) {
                    warn(topic, refId, token, version);
                } else {
                    doSendSms(order.getUserId(), String.format("订单【%s】已经发货", order.getSn()));
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

