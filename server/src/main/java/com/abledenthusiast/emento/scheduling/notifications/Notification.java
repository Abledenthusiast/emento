package com.abledenthusiast.emento.scheduling.notifications;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import com.abledenthusiast.emento.dto.ScheduleDTO;
import com.sendgrid.helpers.mail.objects.Email;

public class Notification {
    private Instant timestamp;
    private String htmlBody;
    private Email creator;
    private List<Email> destinations;

    private static final String DEFAULT_HTML = "";

    public Notification(Instant timestamp, String htmlBody, Email creator, List<Email> destinations) {
        this.timestamp = timestamp;
        this.htmlBody = htmlBody;
        this.creator = creator;
        this.destinations = destinations;
    }

    public static Notification of(ScheduleDTO scheduleDTO) {
        return defaultNotification(scheduleDTO.timeToExecute(), scheduleDTO.getCreator(), scheduleDTO.getDestinations());
    }

    /**
     * @param timestamp
     * @param creator
     * @param destinations
     * @return
     */
    public static Notification defaultNotification(Instant timestamp, Email creator, List<Email> destinations) {
        return new Notification(timestamp, DEFAULT_HTML, creator, destinations);
    }

    public Email getCreator() {
        return creator;
    }

    public List<Email> getDestinations() {
        return destinations;
    }

    public String getHtmlBody() {
        return htmlBody;
    }
}
