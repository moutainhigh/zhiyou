package com.zy.consumer.extend;

import com.zy.Config;
import com.zy.common.support.sms.LuosimaoSmsSupport;
import com.zy.common.support.sms.SmsSupport;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.zy.consumer.extend.Threads.exe;
import static java.lang.Long.parseLong;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;

/**
 * Created by freeman on 16/7/19.
 */
public abstract class AbstractConsumer implements DisposableBean {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	private AtomicInteger POOL_SEQ = new AtomicInteger(1);
	private AtomicBoolean runing = new AtomicBoolean(true);
	protected AtomicBoolean isDev = new AtomicBoolean(false);


	private java.util.List<String> topics;
	private String group;

	private long timeout = 1000;


	@Autowired
	private SmsSupport smsSupport;

	@Autowired
	private Config config;


	@Resource(name = "consumerConfig")
	private Properties consumerConfig;
	protected KafkaConsumer<String, String> consumer;

	public AbstractConsumer(String group, String... topics) {
		this.topics = asList(topics);
		this.group = group;
	}

	public AbstractConsumer(String group, long timeout, String... topics) {
		this(group, topics);
		this.timeout = timeout;
	}

	@PostConstruct
	public void run() {
		isDev.set(config.isDev());
		consumerConfig.put("group.id", group);
		consumer = new KafkaConsumer<>(consumerConfig);
		logger.info("consumerConfig [{}]", this.consumerConfig);
		consumer.subscribe(this.topics);
		exe.execute(() -> {
			Thread.currentThread().setName(group + "-" + POOL_SEQ.getAndIncrement());
			while (runing.get()) consumer.poll(timeout).forEach(this::handle);
		});
	}

	/**
	 * 处理消息
	 * String.format("%s,%s,%s", notify.getRefId(), notify.getToken(), notify.getVersion())
	 *
	 * @param record
	 */
	private void handle(ConsumerRecord<String, String> record) {
		int reCount = 0;
		while (true) {
			final String topic = record.topic();
			try {
				if (isNotBlank(record.value())) {
					if ("{}".equals(record.value()))
						logger.warn(group + " message body is {}");
					else {
						logger.debug("topic {},{}", topic, record.value());
						final String[] split = record.value().split(",");
						if (split.length == 3) {
							final long refId = parseLong(split[0]);
							final String token = split[1];
							final String version = split[2];
							/*如果不是开发测试环境 版本中有dev 则跳过*/
							if (!isDev.get() && version.contains("-dev")) {
								this.doHandle(topic, refId, token, version);
							} else {
								logger.warn("正式环境 不消费 -dev 版本消息topic {}, refId {}, token {}, version {}", topic, refId, token, version);
							}
						} else {
							logger.error("收到非法数据 {}", record.value());
						}

					}
				} else {
					logger.warn(group + " value is null or {} : " + record);
				}
				final long offset = record.offset() + 1;
				consumer.commitSync(singletonMap(
						new TopicPartition(topic, record.partition()),
						new OffsetAndMetadata(offset))
				);
				logger.warn("comsumer [{}] commit topic [{}] offset [{}]", this.group, topic, offset);
				return;
			} catch (Throwable e) {
				String s = "error message :[" + e.getMessage() + "] consumer topic [" + topic + "] json data 【" + record.value() + "】";
				logger.error(s, e);
				try {
					TimeUnit.SECONDS.sleep(reCount + 2L);
					logger.error("do handle error ,sleep 【{}】s 当前第【{}】次重试", reCount + 2, reCount++);
					if (reCount >= 10) {
						runing.set(false);
						logger.error("重试10次后停止当前线程");
						consumer.close();
						smsSupport.send("18508289695", "consumer error plase handle reset 10 ", "");
					}
				} catch (InterruptedException e1) {
				}
			}
		}
	}

	/**
	 * 此处交给对应具体消息处理器
	 *
	 * @param topic   主题
	 * @param refId   业务id
	 * @param token   幂等令牌
	 * @param version 版本
	 */
	protected abstract void doHandle(String topic, long refId, String token, String version);

	protected boolean isNotBlank(String value) {
		return value != null && !"".equals(value);
	}

	protected void warn(String topic, long refId, String token, String version) {
		logger.warn("业务系统查询不到的消息 topic {}, refId {}, token {}, version {}", topic, refId, token, version);
	}

	@Override
	public void destroy() throws Exception {
		this.runing.set(false);
		consumer.close();
		exe.shutdown();
	}

}
