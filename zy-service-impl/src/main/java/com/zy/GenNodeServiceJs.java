package com.zy;

import io.gd.generator.Generator;
import io.gd.generator.handler.NodeHandler;

/**
 * Created by freeman on 16/9/1.
 */
public class GenNodeServiceJs {
    public static void main(String[] args) {
        io.gd.generator.Config config = new io.gd.generator.Config();
        config.setGenLogFile("gd-test.log");
        try {
            Generator.generate(config
                    , new NodeHandler("service.js", "service.doc.json", "com.zy.service")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
