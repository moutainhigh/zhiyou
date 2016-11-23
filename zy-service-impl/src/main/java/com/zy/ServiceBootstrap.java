package com.zy;

import com.alibaba.dubbo.container.Main;

public class ServiceBootstrap {

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("dubbo.application.logger", "slf4j");
        System.setProperty("container", "spring");
        System.setProperty("dubbo.shutdown.hook", "true");
        Main.main(args);
    }

}
