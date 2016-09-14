package com.zy.consumer.component;

import com.zy.consumer.extend.AbstractConsumer;
import com.zy.service.AppearanceService;
import com.zy.service.MessageService;
import com.zy.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/* 消息consumer */
@Component
public class MessageConsumer extends AbstractConsumer {

	Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

	@Autowired
	private UserService userService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private AppearanceService appearanceService;


	public MessageConsumer() {
		super(MessageConsumer.class.getSimpleName());
	}

	@Override
	protected void doHandle(String topic, long refId, String token, String version) {

	}



	private static boolean is(BigDecimal value) {
		return value != null && getAnInt(value) != -1 && getAnInt(value) != 0;
	}

	private static int getAnInt(BigDecimal value) {
		return value.compareTo(BigDecimal.ZERO);
	}
}
