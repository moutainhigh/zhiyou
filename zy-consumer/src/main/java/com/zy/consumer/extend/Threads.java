package com.zy.consumer.extend;

import com.alibaba.dubbo.common.utils.NamedThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by freeman on 16/7/21.
 */
public class Threads {
	public static final ExecutorService exe = Executors.newCachedThreadPool(new NamedThreadFactory("kafka-consumer",true));

}
