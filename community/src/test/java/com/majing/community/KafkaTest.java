package com.majing.community;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author majing
 * @date 2023-11-14 12:19
 * @Description
 */
@SpringBootTest
//指定配置文件
@ContextConfiguration(classes = CommunityApplication.class)
public class KafkaTest {
    @Autowired
    private KafkaProducer kafkaProducer;
    @Test
    public void testKafka() throws InterruptedException {
        kafkaProducer.sendMessage("test", "你好");
        kafkaProducer.sendMessage("test", "hello");
        Thread.sleep(5000);
    }
}

@Component
class KafkaProducer{
    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;
    public void sendMessage(String topic, String content){
        kafkaTemplate.send(topic, content);
    }
}
@Component
class KafkaConsumer{
    @KafkaListener(topics = {"test"})
    public void getMessage(ConsumerRecord<Object, Object> consumerRecord){
        System.out.println(consumerRecord.value());
    }
}
