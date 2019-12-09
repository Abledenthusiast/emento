package com.abledenthusiast.emento.scheduling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class SchedulerFactory {
    private final int DEFAULT_POOL_SIZE = 10;
    private ScheduledExecutorService executor;

    public SchedulerFactory() {
        executor = new ScheduledThreadPoolExecutor(DEFAULT_POOL_SIZE);
    }


    /**
     *
     * @return default scheduler
     */
    public Scheduler getScheduler() {
        return new InstanceScheduler.SchedulerBuilder()
                .setMaxQueueSize(500)
                .build();
    }
}