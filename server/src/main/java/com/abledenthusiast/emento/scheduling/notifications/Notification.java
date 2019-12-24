package com.abledenthusiast.emento.scheduling.notifications;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.abledenthusiast.emento.dto.Schedule;
import com.datastax.driver.mapping.annotations.Table;
import com.sendgrid.helpers.mail.objects.Email;

public class Notification {
    private UUID id;
    private Instant timestamp;
    private String messageTitle;
    private String message;
    private Email creator;
    private List<Email> destinations;
    private Recurrence recurrence;

    private static final String DEFAULT_TITLE = "Emento New Notification";
    private static final String DEFAULT_MESSAGE = "This is a reminder for %d";

    public Notification(Instant timestamp, String messageTitle, String message, Email creator, List<Email> destinations) {
        this.messageTitle = messageTitle;
        this.timestamp = timestamp;
        this.message = message;
        this.creator = creator;
        this.destinations = destinations;
    }

    public Notification(Instant timestamp, String messageTitle, String message, Email creator, List<Email> destinations, Recurrence recurrence) {
        this.timestamp = timestamp;
        this.messageTitle = messageTitle;
        this.message = message;
        this.creator = creator;
        this.destinations = destinations;
        this.recurrence = recurrence;
    }

    public static Notification of(Schedule schedule) {
        return defaultNotification(schedule.timeToExecute(), schedule.getCreator(), schedule.getDestinations());
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

    public UUID id() {
        return id;
    }

    public Email creator() {
        return creator;
    }

    public List<Email> destinations() {
        return destinations;
    }

    public String messageTitle() {
        return messageTitle;
    }

    public String message() {
        return message;
    }

    public void setRecurrent(Recurrence delta) {
        this.recurrence = delta;
    }

    public boolean isRecurrent() {
        return recurrence != null;
    }
}
