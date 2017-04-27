package com.zy.consumer.component;

import com.zy.consumer.extend.AbstractConsumer;
import com.zy.entity.act.Policy;
import com.zy.entity.sys.Message;
import com.zy.entity.usr.User;
import com.zy.service.MessageService;
import com.zy.service.PolicyService;
import com.zy.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Date;

import static com.zy.entity.usr.User.UserRank.*;
import static com.zy.model.Constants.*;
import static java.util.Objects.isNull;
import static org.slf4j.LoggerFactory.getLogger;

/* 消息consumer */
@Component
public class MessageConsumer extends AbstractConsumer {

    private Logger logger = getLogger(MessageConsumer.class);


    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private PolicyService policyService;

    public MessageConsumer() {
        super(MessageConsumer.class.getSimpleName()
                , TOPIC_REGISTER_SUCCESS
                , TOPIC_USER_RANK_CHANGED
                , TOPIC_POLICY_EXPIRE_SOON);
    }

    //String TOPIC_REGISTER_SUCCESS = "register-success"; // 注册成功
    //String TOPIC_USER_RANK_CHANGED = "user-rank-changed"; // 代理等级更新
    @Override
    protected void doHandle(String topic, long refId, String token, String version) {
        final String intern = topic.intern();
        if (TOPIC_REGISTER_SUCCESS.intern() == intern) {
            final User user = userService.findOne(refId);
            if (isNull(user)) {
                warn(topic, refId, token, version);
            } else {
                sendMessage(message(user.getId(), "注册成功", "恭喜你已成功注册成为智优生物的一员", token));
            }

        } else if (TOPIC_USER_RANK_CHANGED.intern() == intern) {
            final User user = userService.findOne(refId);
            if (isNull(user)) {
                warn(topic, refId, token, version);
            } else {
                String desc = null;
                final User.UserRank userRank = user.getUserRank();
                if (userRank == V4) {
                    desc = "特级服务商";
                } else if (userRank == V3) {
                    desc = "省级服务商";
                } else if (userRank == V2) {
                    desc = "市级服务商";
                } else if (userRank == V1) {
                    desc = "VIP服务商";
                }
                if (isNotBlank(desc)) {
                    sendMessage(message(user.getId(), "等级变动", String.format("恭喜你已成功升级为【%s】", desc), token));
                }
            }
        } else if (TOPIC_POLICY_EXPIRE_SOON.intern() == intern) {
            final Policy policy = policyService.findOne(refId);
            if (isNull(policy)) {
                warn(topic, refId, token, version);
            } else {
                sendMessage(message(policy.getUserId(), "保单即将过期提醒", "您的保单即将过期, 请重新投保", token));
            }
        }
    }

    private void sendMessage(Message message) {
        try {
            messageService.create(message);
        } catch (Throwable e) {
            if (e instanceof SQLException) {
                if (!e.getMessage().contains("Duplicate"))
                    throw e;
            }else{
                logger.error(e.getMessage(),e);
            }
        }
    }


    Message message(Long userId, String title, String content, String token) {
        final Message message = new Message();
        message.setToken(token);
        message.setIsRead(false);
        message.setCreatedTime(new Date());
        message.setUserId(userId);
        message.setTitle(title);
        message.setContent(content);
        message.setMessageType(Message.MessageType.系统消息);
        return message;
    }


}

