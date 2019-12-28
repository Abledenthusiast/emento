package com.abledenthusiast.emento;

import com.abledenthusiast.emento.dao.connectionfactory.ConnectionFactory;
import com.abledenthusiast.emento.dto.Schedule;
import com.abledenthusiast.emento.scheduling.Scheduler;
import com.abledenthusiast.emento.scheduling.ServiceBus.MessageHandler;
import com.abledenthusiast.emento.scheduling.ServiceBus.ServiceBusManager;
import com.abledenthusiast.emento.scheduling.notifications.Notification;
import com.abledenthusiast.emento.scheduling.tasks.ExecutionTask;
import com.abledenthusiast.emento.scheduling.tasks.TaskFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageHandler;
import com.microsoft.azure.servicebus.QueueClient;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Controller("/api")
@Secured(SecurityRule.IS_AUTHENTICATED)
@Validated
public class EmentoController {
    private final Logger log = LoggerFactory.getLogger(EmentoController.class);

    private TaskFactory taskFactory;
    private Scheduler scheduler;
    private final EmentoProperties ementoProperties;
    private ServiceBusManager serviceBusManager;
    private QueueClient queueClient;

    public EmentoController(TaskFactory taskFactory, Scheduler scheduler, EmentoProperties ementoProperties,
                            @Named("queueConnectionFactory") ConnectionFactory<QueueClient> connectionFactory) {
        this.taskFactory = taskFactory;
        this.scheduler = scheduler;
        this.ementoProperties = ementoProperties;
        this.queueClient = connectionFactory.connect();
        registerHandler();
    }

    @Get("/ping")
    @Produces(MediaType.APPLICATION_JSON)
    public String ping() {
        return "{ \"status\": \"OK\", \"apiKeyDefaultValue\": \"" + ementoProperties.sendgridApiKey().equals("iamnotasecret") + "\"}";
    }

    @Secured({"ROLE_ADMIN", "ROLE_X"})
    @Post("/schedule")
    public Schedule schedule(@Header("X-MS-CLIENT-PRINCIPAL-NAME") String authPrincipleName, @Body Schedule schedule) {
        log.info(schedule.toString());

        ExecutionTask task = taskFactory.newExecutableTask(schedule);

        scheduler.schedule(task);
        return schedule;
    }

    private class MessageHandler implements IMessageHandler {
        private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                                                    .registerModule(new JavaTimeModule());
        private final Logger log = LoggerFactory.getLogger(com.abledenthusiast.emento.scheduling.ServiceBus.MessageHandler.class);
        @Override
        public CompletableFuture<Void> onMessageAsync(IMessage iMessage) {
            log.info("Received message with sequence number: {} and lock token: {}.", iMessage.getSequenceNumber(), iMessage.getLockToken());
            return queueClient.completeAsync(iMessage.getLockToken()).thenRunAsync(() -> {
                List<byte[]> bytes = iMessage.getMessageBody().getBinaryData();
                String json = bytes.stream()
                                   .map(String::new)
                                   .collect(Collectors.joining());
                Notification notification = toValue(json, Notification.class).orElse(null);
                ExecutionTask task = taskFactory.newExecutableTask(notification);
                scheduler.schedule(task);
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

    private void registerHandler() {
        try {
            queueClient.registerMessageHandler(new MessageHandler());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ServiceBusException e) {
            e.printStackTrace();
        }
    }




}