package com.abledenthusiast.emento.scheduling.tasks;


import com.abledenthusiast.emento.dto.ScheduleDTO;
import com.abledenthusiast.emento.scheduling.notifications.Notification;

public class TaskFactory {

    public ExecutionTask build(ScheduleDTO scheduleDTO) {
        Notification notification = Notification.of(scheduleDTO);
        return new ExecutionTask(scheduleDTO.timeToExecute(), notification);
    }


}

