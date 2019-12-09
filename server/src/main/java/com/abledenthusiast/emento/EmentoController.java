package com.abledenthusiast.emento;

import com.abledenthusiast.emento.dto.ScheduleDTO;
import com.abledenthusiast.emento.scheduling.InstanceScheduler;
import com.abledenthusiast.emento.scheduling.Scheduler;
import com.abledenthusiast.emento.scheduling.tasks.ExecutionTask;
import com.abledenthusiast.emento.scheduling.tasks.Task;
import com.abledenthusiast.emento.scheduling.tasks.TaskFactory;
import io.micronaut.http.HttpMessage;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller("/api")
public class EmentoController {
    private final Logger log = LoggerFactory.getLogger(EmentoController.class);

    private TaskFactory taskFactory;
    private Scheduler scheduler;

    @Get("/ping")
    @Produces(MediaType.TEXT_JSON)
    public String ping() {
        log.info("apiKey is nonNull? {}", EmentoProperties.sendGridAPIKey() != null);
        return "{status:OK, apiKey: " + EmentoProperties.sendGridAPIKey().hashCode() + "}";
    }

    @Post("/schedule")
    public String schedule(ScheduleDTO scheduleDTO) {
        long epochMillis = scheduleDTO.getEpoch();
        ExecutionTask task = taskFactory.build(scheduleDTO);
        scheduler.schedule(task);
        return "status of scheduling {}";
    }
}