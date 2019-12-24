package com.abledenthusiast.emento.scheduling.notifications;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public enum Recurrence {
    DAILY {public Instant getDifference(Instant start) { return start.atOffset(ZoneOffset.UTC).plusDays(1).toInstant(); }},
    WEEKLY {public Instant getDifference(Instant start) { return start.atOffset(ZoneOffset.UTC).plusWeeks(1).toInstant(); }},
    MONTHLY {public Instant getDifference(Instant start) { return start.atOffset(ZoneOffset.UTC).plusMonths(1).toInstant(); }};

    public abstract Instant getDifference(Instant start);
}
