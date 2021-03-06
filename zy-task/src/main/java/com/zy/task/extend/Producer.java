package com.zy.task.extend;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by freeman on 16/7/15.
 */
@Component
public class Producer {

//    @Resource
    private KafkaProducer<String, String> kafkaProducer;

//    @Resource
//    private NotifyService notifyMapper;

    Logger logger = LoggerFactory.getLogger(Producer.class);

    final static AtomicInteger counter = new AtomicInteger(0);

    final static int partitions = 3;

    public void send(String topic, Object object) {
//        Objects.requireNonNull(object);
//        if(object instanceof String){
//            throw new RuntimeException("cannot send string object");
//        }
//        try {
//            Future<RecordMetadata> send = this.kafkaProducer.send(new ProducerRecord<>(topic,
////				next(), System.currentTimeMillis(), topic + "" + System.currentTimeMillis(),
//                    JsonUtils.toJson(object)), ((metadata, exception) -> {
//                if (metadata != null) {
//                    logger.info(metadata.toString());
//                }
//                if (exception != null) {
//                    Notify notify = new Notify();
//                    notify.setPayload(JsonUtils.toJson(object));
//                    notify.setCreatedTime(new Date());
//                    notify.setIsSent(false);
//                    notify.setTopic(topic);
//                    this.notifyMapper.insert(notify);
//                    logger.error(exception.getMessage(), exception);
//                }
//
//            }));
//            RecordMetadata recordMetadata = send.get(5, SECONDS);
//            if (recordMetadata == null)
//                throw new RuntimeException("send message error");
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage(), e);
//        }

    }

    private int next() {
        counter.compareAndSet(Integer.MAX_VALUE, 0);
        return counter.getAndDecrement() % partitions;
    }


}
