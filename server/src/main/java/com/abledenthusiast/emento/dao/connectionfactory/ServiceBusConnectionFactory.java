package com.abledenthusiast.emento.dao.connectionfactory;

import com.abledenthusiast.emento.scheduling.ServiceBus.ServiceBusProducer;
import com.microsoft.azure.servicebus.QueueClient;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

public class ServiceBusConnectionFactory implements ConnectionFactory<QueueClient> {
    private static final Logger log = LoggerFactory.getLogger(ServiceBusConnectionFactory.class);

    private String connectionString;
    private String queueName;

    public ServiceBusConnectionFactory(String connectionString, String queueName) {
        this.connectionString = connectionString;
        this.queueName = queueName;
    }

    @Nullable
    @Override
    public QueueClient connect() {
        try {
            log.info(connectionString);
            return new QueueClient(new ConnectionStringBuilder(connectionString, queueName), ReceiveMode.PEEKLOCK);
        } catch(ServiceBusException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }

}
