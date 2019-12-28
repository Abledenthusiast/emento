package com.abledenthusiast.emento.scheduling;

import com.abledenthusiast.emento.EmentoProperties;
import com.abledenthusiast.emento.client.EmailHandler;
import com.abledenthusiast.emento.client.Handler;
import com.abledenthusiast.emento.dao.connectionfactory.ConnectionFactory;
import com.abledenthusiast.emento.scheduling.ServiceBus.ServiceBusManager;
import com.abledenthusiast.emento.scheduling.ServiceBus.ServiceBusProducer;
import com.abledenthusiast.emento.scheduling.tasks.ExecutionTask;
import com.abledenthusiast.emento.scheduling.tasks.Task;
import com.microsoft.azure.servicebus.QueueClient;

import java.time.Duration;
import java.time.Instant;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * FIXME: Instance scheduler should not rely upon queue producer
 */
public class InstanceScheduler implements Scheduler {
    private final int POOL_SIZE = 8;
    private ScheduledExecutorService executor;
    private Handler emailHandler;
    private EmentoProperties ementoProperties;
    private ServiceBusProducer serviceBusProducer;


    public InstanceScheduler(EmentoProperties ementoProperties, ServiceBusProducer serviceBusProducer) {
        executor = new ScheduledThreadPoolExecutor(POOL_SIZE);
        emailHandler = new EmailHandler(ementoProperties);
        this.serviceBusProducer = serviceBusProducer;
    }

    @Override
    public boolean schedule(ExecutionTask task) {
        if (task.executionInstant().isAfter(Instant.now().plus(Duration.ofMinutes(10)))) {
            serviceBusProducer.push(task.getNotification());
            return true;
        }

        executor.schedule(() -> task.execute(emailHandler), task.executionTime().getSeconds(), TimeUnit.SECONDS);
        return true;
    }


    public static class SchedulerBuilder {
        private int maxSize;
        private EmentoProperties props;

        public SchedulerBuilder() {}

        public SchedulerBuilder setMaxQueueSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public SchedulerBuilder withProperties(EmentoProperties properties) {
            this.props = properties;
            return this;
        }

        public Scheduler build() {
            InstanceScheduler scheduler = new InstanceScheduler(props, null);
            /*
             Room for more fun!
            */
            return scheduler;
        }
    }

}

