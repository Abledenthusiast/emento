package com.abledenthusiast.emento.scheduling.tasks;

import com.abledenthusiast.emento.client.Handler;
import com.abledenthusiast.emento.scheduling.notifications.Notification;

import java.time.Instant;

public class ExecutionTask extends Task {

    public ExecutionTask(Instant timeToExecute, Notification notification) {
        super(timeToExecute, notification);
    }

    public void execute(Handler handler) {
        try {
            handler.handleNotification(notification);
        } catch (Exception e) {
            log.warn("failed to execute send operation");
            log.warn(e.getMessage());
        }
    }


}
