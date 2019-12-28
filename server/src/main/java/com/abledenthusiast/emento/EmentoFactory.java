package com.abledenthusiast.emento;

import com.abledenthusiast.emento.client.EmailHandler;
import com.abledenthusiast.emento.client.Handler;
import com.abledenthusiast.emento.dao.connectionfactory.CassandraConnectionFactory;
import com.abledenthusiast.emento.dao.connectionfactory.ConnectionFactory;
import com.abledenthusiast.emento.dao.connectionfactory.ServiceBusConnectionFactory;
import com.abledenthusiast.emento.scheduling.InstanceScheduler;
import com.abledenthusiast.emento.scheduling.Scheduler;
import com.abledenthusiast.emento.scheduling.ServiceBus.ServiceBusManager;
import com.abledenthusiast.emento.scheduling.ServiceBus.ServiceBusProducer;
import com.abledenthusiast.emento.scheduling.tasks.TaskFactory;
import com.datastax.driver.core.Session;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microsoft.azure.servicebus.QueueClient;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Factory
public class EmentoFactory {

    @Inject
    protected EmentoProperties ementoProperties;

    private static final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                                                       .registerModule(new JavaTimeModule());

    @Bean
    Scheduler scheduler() {
        return new InstanceScheduler(emailHandler(), serviceBusProducer());
    }

    @Bean
    TaskFactory taskFactory() {
        return new TaskFactory(connectionFactory());
    }

    @Bean
    ConnectionFactory<Session> connectionFactory() {
        return new CassandraConnectionFactory(ementoProperties.cassandraHost(), ementoProperties.cassandraPort(),
                                              ementoProperties.cassandraUsername(), ementoProperties.cassandraPassword());
    }


    @Bean
    Handler emailHandler() {
        return new EmailHandler(ementoProperties);
    }




    @Bean
    ServiceBusProducer serviceBusProducer() {
        return new ServiceBusProducer(queueConnectionFactory().connect());
    }

    @Singleton
    @Named("queueConnectionFactory")
    ConnectionFactory<QueueClient> queueConnectionFactory() {
        return new ServiceBusConnectionFactory(ementoProperties.queueConnectionString(), ementoProperties.queueName);
    }


    public static ObjectMapper objectMapper() {
        return objectMapper;
    }



}
