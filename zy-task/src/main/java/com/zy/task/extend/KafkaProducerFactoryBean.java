package com.zy.task.extend;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Properties;

/**
 * Created by freeman on 16/7/15.
 * The producer is <i>thread safe</i> and sharing a single producer instance across threads will generally be faster than
 * having multiple instances.
 */

public class KafkaProducerFactoryBean implements InitializingBean, FactoryBean<KafkaProducer<String,String>>, DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(KafkaProducerFactoryBean.class);

    private KafkaProducer<String, String> producer;
    private Properties properties;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.producer = new KafkaProducer<>(properties);
        logger.info("start kafka producer success , kafka instance :[{}]", this.producer);
    }

    @Override
    public KafkaProducer<String,String> getObject() throws Exception {
        return producer;
    }

    @Override
    public Class<KafkaProducer> getObjectType() {
        return KafkaProducer.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void destroy() throws Exception {
        this.producer.close();

    }
}
