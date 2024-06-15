package com.storm.mq.exception;

import com.storm.mq.message.MessageContext;
import lombok.Data;

/**
 *
 */

public class BrokerConfirmException extends MqException {
    private final MessageContext messageContext;

    public BrokerConfirmException(MessageContext messageContext) {
        this.messageContext = messageContext;
    }

    public BrokerConfirmException(MessageContext messageContext, String message) {
        super(message);
        this.messageContext = messageContext;
    }

    public BrokerConfirmException(MessageContext messageContext, String message, Throwable cause) {
        super(message, cause);
        this.messageContext = messageContext;
    }

    public BrokerConfirmException(MessageContext messageContext, Throwable cause) {
        super(cause);
        this.messageContext = messageContext;
    }

    public MessageContext getMessageContext() {
        return this.messageContext;
    }
}
