package com.abledenthusiast.emento.scheduling.ServiceBus;

import com.abledenthusiast.emento.scheduling.notifications.Notification;
import com.abledenthusiast.emento.scheduling.tasks.ExecutionTask;
import com.abledenthusiast.emento.scheduling.tasks.TaskFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Utf8;
import com.microsoft.azure.servicebus.*;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class MessageHandler implements IMessageHandler {
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                                                .registerModule(new JavaTimeModule());
    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);
    private IQueueClient client;
    private TaskFactory taskFactory;

    public MessageHandler(IQueueClient client, TaskFactory taskFactory) {
        this.client = client;
        this.taskFactory = taskFactory;
    }

    @Override
    public CompletableFuture<Void> onMessageAsync(IMessage iMessage) {
        log.info("Received message with sequence number: {} and lock token: {}.", iMessage.getSequenceNumber(), iMessage.getLockToken());
        return this.client.completeAsync(iMessage.getLockToken()).thenRunAsync(() -> {
            List<byte[]> bytes = iMessage.getMessageBody().getBinaryData();
            String json = bytes.stream()
                 .map(String::new)
                 .collect(Collectors.joining());
            Notification notification = toValue(json, Notification.class).orElse(null);
            ExecutionTask task = taskFactory.newExecutableTask(notification);

            log.info("Completed message {} and locktoken: {}", iMessage.getSequenceNumber(), iMessage.getLockToken());
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
