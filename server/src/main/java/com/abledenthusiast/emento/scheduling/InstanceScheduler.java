package com.abledenthusiast.emento.scheduling;

import com.abledenthusiast.emento.EmentoProperties;
import com.abledenthusiast.emento.client.EmailHandler;
import com.abledenthusiast.emento.client.Handler;
import com.abledenthusiast.emento.scheduling.tasks.ExecutionTask;
import com.abledenthusiast.emento.scheduling.tasks.Task;

import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class InstanceScheduler implements Scheduler {
    private final int POOL_SIZE = 8;
    private ScheduledExecutorService executor;
    private Handler emailHandler;
    private EmentoProperties ementoProperties;


    public InstanceScheduler(EmentoProperties ementoProperties) {
        executor = new ScheduledThreadPoolExecutor(POOL_SIZE);
        emailHandler = new EmailHandler(ementoProperties);
    }

    @Override
    public boolean schedule(ExecutionTask task) {
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
            InstanceScheduler scheduler = new InstanceScheduler(props);
            /*
             Room for more fun!
            */
            return scheduler;
        }
    }

}

