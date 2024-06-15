package com.storm.mq.store;

import com.storm.mq.codec.impl.JsonCodec;
import com.storm.mq.enums.MessageStatus;
import com.storm.mq.enums.MessageType;
import com.storm.mq.message.MessageContext;
import com.storm.mq.message.MessageProp;

import java.util.Base64;
import java.util.List;

public interface MessageRepository {

    JsonCodec JSON_CODEC = new JsonCodec();

    default void save(MessageContext messageContext,long deliveryTag){
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setMessageType(MessageType.RABBIT);
        messageEntity.setId(messageContext.getMessageId());
        messageEntity.setTopic(messageEntity.getTopic());
        messageEntity.setPayload(Base64.getEncoder().encodeToString(messageContext.getPayload()));
        messageEntity.setContentEncoding(messageEntity.getContentEncoding());
        messageEntity.setContentType(messageEntity.getContentType());
        messageEntity.setTimestamp(messageEntity.getTimestamp());
        messageEntity.setDelay(deliveryTag);
        MessageProp messageProp = messageContext.getMessageProp();
        messageEntity.setPersistent(messageEntity.isPersistent());
        messageEntity.setDelay(messageProp.getDelay());
        messageEntity.setAutoRetryTotal(messageProp.getAutoRetryTotal());
        messageEntity.setPublishRetryOnFail(messageProp.getPublishRetryOnFail());
        messageEntity.setExtContext(new String(JSON_CODEC.encode(messageEntity.getExtContext())));
        this.save(messageEntity);
    }

    /**
     * 保存消息
     *
     * @param messageEntity 要保存的消息
     */
    void save(MessageEntity messageEntity);

    /**
     * 根据条件查询指定状态的消息
     *
     * @param topic  topic
     * @param status 消息状态
     * @param offset 起始位置
     * @param limit  查询数量
     * @return {@link List}<{@link MessageEntity}>
     */
    List<MessageEntity> select(String topic, MessageStatus status, int offset, int limit);

    /**
     * 更新消息状态
     *
     * @param topic  消息topic
     * @param id     消息标识
     * @param status 要更新的消息状态
     */
    void updateStatus(String topic, String id, MessageStatus status);

}
