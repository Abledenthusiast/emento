package com.abledenthusiast.emento.scheduling.tasks;

import com.abledenthusiast.emento.client.EmailHandler;
import com.abledenthusiast.emento.scheduling.notifications.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.Calendar;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class ExecutionTask extends Task {

    public ExecutionTask(Instant timeToExecute, Notification notification) {
        super(timeToExecute, notification);
    }

    public void execute(EmailHandler emailHandler) {
        try {
            emailHandler.send(notification.getCreator(), notification.getDestinations());
        } catch (IOException e) {
            log.warn("failed to execute send operation");
            log.warn(e.getMessage());
        }
    }


}
