package com.zy.task.job;

import com.zy.Config;
import com.zy.entity.sys.Notify;
import com.zy.service.NotifyService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.zy.model.query.NotifyQueryModel.builder;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by freeman on 16/9/14.
 */
@Component
public class NotifySendToMqJob {

	Logger logger = getLogger(NotifySendToMqJob.class);

	@Autowired
	private KafkaProducer<String, String> kafkaProducer;

	@Autowired
	private NotifyService notifyService;

	@Autowired
	private Config config;

	private AtomicBoolean isDev = new AtomicBoolean(false);

	private final static ExecutorService executorService = newSingleThreadExecutor(r -> {
		Thread thread = new Thread(r);
		thread.setName(NotifySendToMqJob.class.getName());
		thread.setDaemon(true);
		return thread;
	});

	private final AtomicBoolean isRunning = new AtomicBoolean(true);

	private final Runnable runnable = () -> {
		while (isRunning.get()) {
			notifyService.findAll(builder().isSentEQ(false).build()).forEach(this::sendToMq);
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		}
	};

	@PostConstruct
	public void started() {
		logger.info("started success");
		isDev.set(config.isDev());
		logger.info("isDev {}", isDev.get());
		executorService.execute(runnable);
	}

	@PreDestroy
	public void destroy() {
		logger.info("destroy..");
		isRunning.set(false);
		this.executorService.shutdown();
		this.kafkaProducer.close();
		logger.info("destroy..success...");
	}

	private void sendToMq(Notify notify) {
		String version = notify.getVersion();
		if (isDev.get()) {
			version += "-dev";
		}
		final String message = String.format("%s,%s,%s", notify.getRefId(), notify.getToken(), version);
		logger.info("send message {}", message);
		kafkaProducer.send(new ProducerRecord<String, String>(notify.getTopic(), message));
		this.notifyService.sendSuccess(notify.getId());
	}

}
