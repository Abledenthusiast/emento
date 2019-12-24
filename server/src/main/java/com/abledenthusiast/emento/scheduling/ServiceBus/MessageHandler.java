package com.abledenthusiast.emento.scheduling.ServiceBus;

import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageHandler;
import com.microsoft.azure.servicebus.IQueueClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class MessageHandler implements IMessageHandler {
    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);
    private IQueueClient client;

    public MessageHandler(IQueueClient client) {
        this.client = client;
    }

    @Override
    public CompletableFuture<Void> onMessageAsync(IMessage iMessage) {
        log.info("Received message with sq#: %d and lock token: %s.", iMessage.getSequenceNumber(), iMessage.getLockToken());
        return this.client.completeAsync(iMessage.getLockToken()).thenRunAsync(() -> {
            log.info("Completed message sq#: %d and locktoken: %s", iMessage.getSequenceNumber(), iMessage.getLockToken());
        });
    }

    @Override
    public void notifyException(Throwable throwable, ExceptionPhase exceptionPhase) {
        log.info(exceptionPhase + "-" + throwable.getMessage());
    }
}
