package com.zy;

import com.alibaba.dubbo.container.Main;

public class ServiceBootstrap {

	public static void main(String[] args) {
		//start jetty server  on port 0.0.0.0:8888
		//localhost:8888
		System.setProperty("dubbo.jetty.port","8889");
		System.setProperty("dubbo.application.logger","slf4j");
		System.setProperty("dubbo.container", "spring,jetty");
		Main.main(args);
	}

}
