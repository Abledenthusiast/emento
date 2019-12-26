package com.abledenthusiast.emento.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * main DTO for schedule requests
 */
public class Schedule {
    private static final Logger log = LoggerFactory.getLogger(Schedule.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @JsonProperty
    private UUID id;

    @JsonProperty
    private String scheduledTime;

    @JsonProperty
    private Email creator;

    @JsonProperty
    private List<Email> destinations;

    @JsonProperty
    private String messageTitle;

    @JsonProperty
    private String messageBody;

    @JsonProperty
    private Instant savedInstant;


    public Schedule() {}

    public Schedule(UUID id, String scheduledTime, Email creator, List<Email> destinations, String messageTitle, String messageBody, Instant savedInstant) {
        this.id = id;
        this.scheduledTime = scheduledTime;
        this.creator = creator;
        this.destinations = destinations;
        this.messageTitle = messageTitle;
        this.messageBody = messageBody;
        this.savedInstant = savedInstant;
    }


    public UUID id() {
        return id;
    }

    public Email getCreator() {
        return creator;
    }

    public List<Email> getDestinations() {
        return destinations;
    }

    public Instant timeToExecute() {
        if (savedInstant != null) {
            return savedInstant;
        }
        savedInstant = ZonedDateTime.parse(scheduledTime, formatter).toInstant();
        return savedInstant;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public String messageBody() {
        return messageBody;
    }

    public String messageTitle() {
        return messageTitle;
    }

    public Optional<String> toJson() {
        try {
            return Optional.ofNullable(objectMapper.writeValueAsString(this));
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return Optional.empty();
    }


    @Override
    public String toString() {
        return "Schedule{" +
            ", id=" + id +
            ", scheduledTime='" + scheduledTime + '\'' +
            ", creator=" + creator +
            ", destinations=" + destinations +
            ", messageTitle='" + messageTitle + '\'' +
            ", messageBody='" + messageBody + '\'' +
            ", savedInstant=" + savedInstant +
            '}';
    }
}
