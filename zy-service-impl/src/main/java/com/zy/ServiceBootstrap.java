package com.zy;

import com.alibaba.dubbo.container.Main;
import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;

public class ServiceBootstrap {

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("dubbo.application.logger", "slf4j");
        System.setProperty("container", "spring");
        System.setProperty("dubbo.shutdown.hook", "true");
        org.slf4j.LoggerFactory.getLogger(this.getClass()).info("started,.......");

        Main.main(args);
    }

}
