package com.zy.job;

import com.zy.entity.sys.Notify;
import com.zy.model.query.NotifyQueryModel;
import com.zy.service.NotifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by freeman on 16/9/14.
 */
//@Component
public class NotifySendToMqJob {

	Logger logger = LoggerFactory.getLogger(NotifySendToMqJob.class);

	@Autowired
	private NotifyService notifyService;

	private final static ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
		Thread thread = new Thread(r);
		thread.setName(NotifySendToMqJob.class.getName());
		thread.setDaemon(true);
		return thread;
	});
	private final static AtomicBoolean isRunning = new AtomicBoolean(true);
	private final Runnable runnable = () -> {
		while (isRunning.get()) {
			notifyService.findAll(NotifyQueryModel.builder().isSentEQ(false).build()).forEach(this::sendToMq);
		}
	};

	private void sendToMq(Notify notify) {
//		this.notifyService.
	}

	@PostConstruct
	public void run() {
		logger.info("started ");
		executorService.execute(runnable);
	}

	@PreDestroy
	public void destroy() {
		isRunning.set(false);
		this.executorService.shutdownNow();
	}

}
