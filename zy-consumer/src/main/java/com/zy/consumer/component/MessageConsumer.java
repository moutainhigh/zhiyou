package com.zy.consumer.component;

import com.zy.consumer.extend.AbstractConsumer;
import com.zy.entity.sys.Message;
import com.zy.entity.usr.User;
import com.zy.service.AppearanceService;
import com.zy.service.MessageService;
import com.zy.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

import static com.zy.entity.usr.User.UserRank.*;
import static com.zy.model.Constants.TOPIC_REGISTER_SUCCESS;
import static com.zy.model.Constants.TOPIC_USER_RANK_CHANGED;
import static java.math.BigDecimal.ZERO;
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
	private AppearanceService appearanceService;


	public MessageConsumer() {
		super(MessageConsumer.class.getSimpleName()
				, TOPIC_REGISTER_SUCCESS
				, TOPIC_USER_RANK_CHANGED);
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
				message(user.getId(), "注册成功", "恭喜你已成功注册成为智优生物的一员", token);
			}

		} else if (TOPIC_USER_RANK_CHANGED.intern() == intern) {
			final User user = userService.findOne(refId);
			if (isNull(user)) {
				warn(topic, refId, token, version);
			} else {
				String desc = null;
				final User.UserRank userRank = user.getUserRank();
				if (userRank == V4) {
					desc = "特级代理";
				} else if (userRank == V3) {
					desc = "一级代理";
				} else if (userRank == V2) {
					desc = "二级代理";
				} else if (userRank == V1) {
					desc = "三级代理";
				}
				if (isNotBlank(desc)) {
					message(user.getId(), "注册成功", String.format("恭喜你已成功升级为【%s】", desc), token);
				}
			}
		}
	}



	private static int getAnInt(BigDecimal value) {
		return value.compareTo(ZERO);
	}

	Message message(Long userId, String title, String content, String token) {
		final Message message = new Message();
		message.setToken(token);
		message.setIsRead(false);
		message.setCreatedTime(new Date());
		message.setUserId(userId);
		message.setTitle(title);
		message.setContent(content);
		return message;
	}


}

