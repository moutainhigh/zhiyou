package com.zy;

import com.alibaba.dubbo.container.Main;

public class ServiceBootstrap {

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("dubbo.application.logger", "slf4j");
        System.setProperty("container", "spring");
        System.setProperty("dubbo.shutdown.hook", "true");
        org.slf4j.LoggerFactory.getLogger(ServiceBootstrap.class.getClass()).info("started,.......");

        Main.main(args);
    }

}
