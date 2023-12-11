package com.majing.community.event;

import com.alibaba.fastjson.JSONObject;
import com.majing.community.entity.Event;
import com.majing.community.entity.Message;
import com.majing.community.service.MessageService;
import com.majing.community.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author majing
 * @date 2023-11-15 12:14
 * @Description
 */
@Component
public class EventConsumer implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private final MessageService messageService;
    @Autowired
    public EventConsumer(MessageService messageService) {
        this.messageService = messageService;
    }

    @KafkaListener(topics = {EVENT_COMMENT,EVENT_FOLLOW,EVENT_LIKE})
    public void handelTopics(ConsumerRecord<String, Object> consumerRecord){
        if(consumerRecord == null || consumerRecord.value() == null){
            logger.error("消息为空");
            return;
        }
        Event event = JSONObject.parseObject(consumerRecord.value().toString(), Event.class);
        if(event == null){
            logger.error("消息格式错误");
            return;
        }
        Message message = new Message();
        message.setFromId(1);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());
        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityId", event.getEntityId());
        content.put("entityType", event.getEntityType());
        if(!CollectionUtils.isEmpty(event.getData())){
            content.putAll(event.getData());
        }
        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);
    }
}
