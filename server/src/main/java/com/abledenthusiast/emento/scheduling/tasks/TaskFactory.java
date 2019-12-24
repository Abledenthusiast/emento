package com.abledenthusiast.emento.scheduling.tasks;


import com.abledenthusiast.emento.dao.NotificationDao;
import com.abledenthusiast.emento.dao.connectionfactory.ConnectionFactory;
import com.abledenthusiast.emento.dto.Schedule;
import com.abledenthusiast.emento.scheduling.ServiceBus.ServiceBusProducer;
import com.abledenthusiast.emento.scheduling.notifications.Notification;


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
        if (notification.isRecurrent()) {
            notificationDao.insert(notification);
        }

        serviceBusProducer.push(schedule.toJson().get());

        return new ExecutionTask(schedule.timeToExecute(), notification);
    }


}

