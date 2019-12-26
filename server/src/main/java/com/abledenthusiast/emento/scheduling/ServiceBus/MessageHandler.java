package com.abledenthusiast.emento.scheduling.ServiceBus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageHandler;
import com.microsoft.azure.servicebus.IQueueClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MessageHandler implements IMessageHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);
    private IQueueClient client;

    public MessageHandler(IQueueClient client) {
        this.client = client;
    }

    @Override
    public CompletableFuture<Void> onMessageAsync(IMessage iMessage) {
        log.info("Received message with sequence number: {} and lock token: {}.", iMessage.getSequenceNumber(), iMessage.getLockToken());
        return this.client.completeAsync(iMessage.getLockToken()).thenRunAsync(() -> {
            log.info(iMessage.getMessageBody().getValueData().toString());
            log.info("Completed message {}:and locktoken: {}", iMessage.getSequenceNumber(), iMessage.getLockToken());
        });
    }

    @Override
    public void notifyException(Throwable throwable, ExceptionPhase exceptionPhase) {
        log.info(exceptionPhase + "-" + throwable.getMessage());
    }

    protected <T> Optional<T> toValue(String json, Class<T> clazz) {
        if (json != null && clazz != null) {
            try {
                return Optional.ofNullable(objectMapper.readValue(json, clazz));
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        return Optional.empty();
    }
}
