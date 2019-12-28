package com.abledenthusiast.emento.scheduling.ServiceBus;

import com.abledenthusiast.emento.dao.connectionfactory.ConnectionFactory;
import com.microsoft.azure.servicebus.MessageHandlerOptions;
import com.microsoft.azure.servicebus.QueueClient;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;

import java.time.Duration;

public class ServiceBusManager {

    private ConnectionFactory<QueueClient> connectionFactory;
    private QueueClient queueClient;

    private MessageHandler messageHandler;
    private ServiceBusProducer serviceBusProducer;

    public ServiceBusManager(ConnectionFactory<QueueClient> connectionFactory) {
        this.connectionFactory = connectionFactory;
        queueClient = connectionFactory.connect();
        serviceBusProducer = new ServiceBusProducer(queueClient);
    }

    public ServiceBusProducer getServiceBusProducer() {
        return serviceBusProducer;
    }

}
