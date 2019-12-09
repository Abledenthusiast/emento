package com.abledenthusiast.emento.dto;

import com.sendgrid.helpers.mail.objects.Email;

import java.time.Instant;
import java.util.List;

public class ScheduleDTO {

    private long epochMillis;
    private Email creator;
    private List<Email> destinations;

    public long getEpoch() {
        return epochMillis;
    }

    public Email getCreator() {
        return creator;
    }

    public List<Email> getDestinations() {
        return destinations;
    }

    public Instant timeToExecute() {
        return Instant.ofEpochMilli(epochMillis);
    }
}
