package com.abledenthusiast.emento.scheduling.ServiceBus;

import com.abledenthusiast.emento.dao.connectionfactory.ConnectionFactory;
import com.abledenthusiast.emento.dto.Schedule;
import com.abledenthusiast.emento.scheduling.notifications.Notification;
import com.microsoft.azure.servicebus.*;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public class ServiceBusProducer {
    private static final Logger log = LoggerFactory.getLogger(ServiceBusProducer.class);

    private QueueClient queueClient;
    private ConnectionFactory<QueueClient> connectionFactory;

    public ServiceBusProducer(ConnectionFactory<QueueClient> connectionFactory) {
        this.connectionFactory = connectionFactory;
        queueClient = connectionFactory.connect();
        registerHandler();
    }


    public void push(Notification notification) {
        log.info("Sending message {}", Instant.now());
        Message message = new Message(notification.toJson().get());
        message.setScheduledEnqueueTimeUtc(notification.timestamp().minus(Duration.ofMinutes(10)));
        queueClient.sendAsync(message).thenRunAsync(() -> { log.info("Sent message ");});
    }

    public void push(String json) {

    }


    public void registerHandler() {
        try {
            queueClient.registerMessageHandler(new MessageHandler(queueClient), new MessageHandlerOptions(8, false, Duration.ofMinutes(1)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ServiceBusException e) {
            e.printStackTrace();
        }
    }
}
