package com.abledenthusiast.emento.scheduling.ServiceBus;

import com.abledenthusiast.emento.dao.connectionfactory.ConnectionFactory;
import com.abledenthusiast.emento.scheduling.notifications.Notification;
import com.abledenthusiast.emento.scheduling.tasks.TaskFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microsoft.azure.servicebus.*;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class ServiceBusProducer {
    private static final Logger log = LoggerFactory.getLogger(ServiceBusProducer.class);

    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                                                .registerModule(new JavaTimeModule());
    private ConnectionFactory connectionFactory;

    private QueueClient queueClient;

    public ServiceBusProducer(QueueClient queueClient) {
        this.queueClient = queueClient;

    }


    public void push(Notification notification) {
        log.info("Sending message {},  {}", toJson(notification).get(), Instant.now());

        Message message = new Message(toJson(notification).get());
        log.info(notification.getTimestamp().toString());
        //message.setScheduledEnqueueTimeUtc(notification.getTimestamp().minus(Duration.ofMinutes(5)));
        //message.setScheduledEnqueueTimeUtc(Instant.now());
        queueClient.scheduleMessageAsync(message, notification.getTimestamp().minus(Duration.ofMinutes(5)));
        //queueClient.sendAsync(message).thenRunAsync(() -> { log.info("Sent message ");});
    }

    public void push(String json) {

    }

    protected Optional<String> toJson(Object object) {
        if (object != null) {
            try {
                return Optional.ofNullable(objectMapper.writeValueAsString(object));
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        }
        return Optional.empty();
    }

}
