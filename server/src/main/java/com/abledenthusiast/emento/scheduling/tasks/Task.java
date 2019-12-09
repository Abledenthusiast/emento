package com.abledenthusiast.emento.scheduling.tasks;


import com.abledenthusiast.emento.scheduling.notifications.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.concurrent.Delayed;

public abstract class Task {
    protected  final Logger log = LoggerFactory.getLogger(getClass());
    protected Instant timeToExecute;
    protected Notification notification;

    public Task(Instant timeToExecute, Notification notification) {
        this.timeToExecute = timeToExecute;
        this.notification = notification;
    }

    public Duration executionTime() {
        return Duration.between(Instant.now(), timeToExecute);
    }



    public Calendar timeAsCalendar() {
        return new Calendar.Builder().setInstant(timeToExecute.getEpochSecond()).build();
    }
}
