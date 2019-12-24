package com.abledenthusiast.emento;

import com.abledenthusiast.emento.dto.Schedule;
import com.abledenthusiast.emento.scheduling.Scheduler;
import com.abledenthusiast.emento.scheduling.tasks.ExecutionTask;
import com.abledenthusiast.emento.scheduling.tasks.TaskFactory;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller("/api")
@Secured(SecurityRule.IS_AUTHENTICATED)
@Validated
public class EmentoController {
    private final Logger log = LoggerFactory.getLogger(EmentoController.class);

    private TaskFactory taskFactory;
    private Scheduler scheduler;
    private final EmentoProperties ementoProperties;

    public EmentoController(TaskFactory taskFactory, Scheduler scheduler, EmentoProperties ementoProperties) {
        this.taskFactory = taskFactory;
        this.scheduler = scheduler;
        this.ementoProperties = ementoProperties;
    }

    @Get("/ping")
    @Produces(MediaType.APPLICATION_JSON)
    public String ping() {
        return "{ \"status\": \"OK\", \"apiKeyDefaultValue\": \"" + ementoProperties.sendgridApiKey().equals("iamnotasecret") + "\"}";
    }

    @Secured({"ROLE_ADMIN", "ROLE_X"})
    @Post("/schedule")
    public Schedule schedule(@Header("X-MS-CLIENT-PRINCIPAL-NAME") String authPrincipleName, @Body Schedule schedule) {
        log.info(schedule.toString());

        ExecutionTask task = taskFactory.newExecutableTask(schedule);

        scheduler.schedule(task);
        return schedule;
    }


}