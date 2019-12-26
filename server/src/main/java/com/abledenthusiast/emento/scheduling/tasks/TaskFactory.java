package com.abledenthusiast.emento.scheduling.tasks;


import com.abledenthusiast.emento.dao.NotificationDao;
import com.abledenthusiast.emento.dao.connectionfactory.ConnectionFactory;
import com.abledenthusiast.emento.dto.Schedule;
import com.abledenthusiast.emento.scheduling.ServiceBus.ServiceBusProducer;
import com.abledenthusiast.emento.scheduling.notifications.Notification;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.UUID;


public class TaskFactory {

    private NotificationDao notificationDao;

    private ServiceBusProducer serviceBusProducer;

    private ConnectionFactory queueConnectionFactory;

    public TaskFactory(ConnectionFactory connectionFactory, ConnectionFactory queueConnectionFactory) {
        notificationDao = new NotificationDao(connectionFactory);
        serviceBusProducer = new ServiceBusProducer(queueConnectionFactory);
    }

    public ExecutionTask newExecutableTask(Schedule schedule) {
        Notification notification = Notification.of(schedule);
        if (notification.isRecurrent() || notification.timestamp().isAfter(Instant.now().plus(Duration.ofHours(4)))) {
            notificationDao.insert(notification);
            serviceBusProducer.push(notification);
        }

        serviceBusProducer.push(notification);
        return new ExecutionTask(schedule.timeToExecute(), notification);
    }

    public ExecutionTask newExecutableTask(UUID notificationId) {
        Notification notification = notificationDao.selectNotification(notificationId);
        return new ExecutionTask(notification.timestamp(), notification);
    }


}

