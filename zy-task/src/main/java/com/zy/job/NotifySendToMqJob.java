package com.zy.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by freeman on 16/9/14.
 */
@Component
public class NotifySendToMqJob {

	Logger logger = LoggerFactory.getLogger(NotifySendToMqJob.class);

	private final static ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
		Thread thread = new Thread(r);
		thread.setName("task confirm consumer");
		thread.setDaemon(true);
		return thread;
	});
	private AtomicBoolean isRunning = new AtomicBoolean(true);
	private static final Runnable runnable = () -> {


	};

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
