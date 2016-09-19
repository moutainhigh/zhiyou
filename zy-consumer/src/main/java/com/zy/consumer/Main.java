package com.zy.consumer;

/**
 * 启动类
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("dubbo.application.logger", "slf4j");
//        System.setProperty("dubbo.jetty.port", "8889");
        System.setProperty("dubbo.container", "spring");
        com.alibaba.dubbo.container.Main.main(args);
    }

}
