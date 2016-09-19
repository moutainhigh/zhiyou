package com.zy.task;

import org.slf4j.LoggerFactory;

/**
 * 启动类
 */
public class Main {

	public static void main(String[] args) {
		System.setProperty("dubbo.application.logger", "slf4j");
		System.setProperty("dubbo.container", "spring");
		LoggerFactory.getLogger(Main.class).info("started.... task");
		com.alibaba.dubbo.container.Main.main(args);
	}
}
