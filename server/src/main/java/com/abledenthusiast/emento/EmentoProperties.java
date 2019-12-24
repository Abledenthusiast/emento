package com.abledenthusiast.emento;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Property;

import javax.inject.Singleton;

@Singleton
public class EmentoProperties {

    @Property(name = "emento.sendgrid.api-key")
    protected String sendgridApiKey;

    @Property(name = "emento.cassandra.host")
    protected String cassandraHost;

    @Property(name = "emento.cassandra.port")
    protected int cassandraPort;

    @Property(name = "emento.cassandra.username")
    protected String cassandraUsername;

    @Property(name = "emento.cassandra.password")
    protected String cassandraPassword;

    @Property(name = "emento.queue.connection-string")
    protected String queueConnectionString;

    @Property(name = "emento.queue.name")
    protected String queueName;

    public String sendgridApiKey() {
        return sendgridApiKey;
    }

    public String cassandraHost() {
        return cassandraHost;
    }

    public int cassandraPort() {
        return cassandraPort;
    }

    public String cassandraUsername() {
        return cassandraUsername;
    }

    public String cassandraPassword() {
        return cassandraPassword;
    }

    public String queueConnectionString() {
        return queueConnectionString;
    }

    public String queueName() {
        return queueName;
    }

}
