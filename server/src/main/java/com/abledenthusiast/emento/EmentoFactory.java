package com.abledenthusiast.emento;

import com.abledenthusiast.emento.dao.connectionfactory.CassandraConnectionFactory;
import com.abledenthusiast.emento.dao.connectionfactory.ConnectionFactory;
import com.abledenthusiast.emento.dao.connectionfactory.ServiceBusConnectionFactory;
import com.abledenthusiast.emento.scheduling.InstanceScheduler;
import com.abledenthusiast.emento.scheduling.Scheduler;
import com.abledenthusiast.emento.scheduling.tasks.TaskFactory;
import com.datastax.driver.core.Session;
import com.microsoft.azure.servicebus.QueueClient;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Factory
public class EmentoFactory {

    @Inject
    protected EmentoProperties ementoProperties;

    @Bean
    Scheduler scheduler() {
        return new InstanceScheduler(ementoProperties);
    }

    @Bean
    TaskFactory taskFactory() {
        return new TaskFactory(connectionFactory(), queueConnectionFactory());
    }

    @Bean
    ConnectionFactory<Session> connectionFactory() {
        return new CassandraConnectionFactory(ementoProperties.cassandraHost(), ementoProperties.cassandraPort(),
                                              ementoProperties.cassandraUsername(), ementoProperties.cassandraPassword());
    }

    @Bean
    ConnectionFactory<QueueClient> queueConnectionFactory() {
        return new ServiceBusConnectionFactory(ementoProperties.queueConnectionString(), ementoProperties.queueName);
    }



}
