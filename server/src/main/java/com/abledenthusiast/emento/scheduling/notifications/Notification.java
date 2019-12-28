package com.abledenthusiast.emento.scheduling.notifications;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.abledenthusiast.emento.dto.Schedule;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Notification {

    private static final Logger log = LoggerFactory.getLogger(Notification.class);
    private UUID id;
    private Instant timestamp;
    private String messageTitle;
    private String message;
    private Email creator;
    private List<Email> destinations;

    private static final String DEFAULT_TITLE = "Emento New Notification";
    private static final String DEFAULT_MESSAGE = "This is a reminder for %s";


    private Notification() {}

    public Notification(Instant timestamp, String messageTitle, String message, Email creator, List<Email> destinations) {
        this.messageTitle = messageTitle;
        this.timestamp = timestamp;
        this.message = message;
        this.creator = creator;
        this.destinations = destinations;
    }

    public Notification(UUID id, Instant timestamp, String messageTitle, String message, Email creator, List<Email> destinations) {
        this.id = id;
        this.timestamp = timestamp;
        this.messageTitle = messageTitle;
        this.message = message;
        this.creator = creator;
        this.destinations = destinations;
    }

    public static Notification of(Schedule schedule) {
        if (schedule.messageTitle() != null && schedule.messageBody() != null) {
            return fromSchedule(schedule);
        }
        return defaultNotification(schedule.timeToExecute(), schedule.getCreator(), schedule.getDestinations());
    }

    /**
     * Should only be used when schedule contains a message title and body
     * @param schedule
     */
    public static Notification fromSchedule(Schedule schedule) {
        Instant timestamp = schedule.timeToExecute();
        String messageTitle = schedule.messageTitle();
        String message = schedule.messageBody();
        Email creator = schedule.getCreator();
        List<Email> destinations = schedule.getDestinations();

        return new Notification(timestamp, messageTitle, message, creator, destinations);

    }

    /**
     * @param timestamp
     * @param creator
     * @param destinations
     * @return
     */
    public static Notification defaultNotification(Instant timestamp, Email creator, List<Email> destinations) {
        return new Notification(timestamp, DEFAULT_TITLE, DEFAULT_MESSAGE, creator, destinations);
    }


    public UUID getId() {
        return id;
    }

    public Email getCreator() {
        return creator;
    }

    public List<Email> getDestinations() {
        return destinations;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }


}
