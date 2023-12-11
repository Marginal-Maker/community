package com.majing.community.event;

import com.alibaba.fastjson.JSONObject;
import com.majing.community.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author majing
 * @date 2023-11-15 12:11
 * @Description 消息队列生产者
 */
@Component
public class EventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    public EventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void fireEvent(Event event){
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }
}
