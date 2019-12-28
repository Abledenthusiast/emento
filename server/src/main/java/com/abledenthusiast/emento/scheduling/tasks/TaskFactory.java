package com.abledenthusiast.emento.scheduling.tasks;


import com.abledenthusiast.emento.dao.NotificationDao;
import com.abledenthusiast.emento.dao.connectionfactory.ConnectionFactory;
import com.abledenthusiast.emento.dto.Schedule;
import com.abledenthusiast.emento.scheduling.ServiceBus.ServiceBusManager;
import com.abledenthusiast.emento.scheduling.ServiceBus.ServiceBusProducer;
import com.abledenthusiast.emento.scheduling.notifications.Notification;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;


public class TaskFactory {

    private NotificationDao notificationDao;



    public TaskFactory(ConnectionFactory connectionFactory) {
        notificationDao = new NotificationDao(connectionFactory);

    }

    public ExecutionTask newExecutableTask(Schedule schedule) {
        Notification notification = Notification.of(schedule);
        if (notification.getTimestamp().isAfter(Instant.now().plus(Duration.ofHours(4)))) {
            notificationDao.insert(notification);
        }

        return new ExecutionTask(schedule.timeToExecute(), notification);
    }

    public ExecutionTask newExecutableTask(Notification notification) {
        return new ExecutionTask(notification.getTimestamp(), notification);
    }

    public ExecutionTask newExecutableTask(UUID notificationId) {
        Notification notification = notificationDao.selectNotification(notificationId);
        return new ExecutionTask(notification.getTimestamp(), notification);
    }


}

