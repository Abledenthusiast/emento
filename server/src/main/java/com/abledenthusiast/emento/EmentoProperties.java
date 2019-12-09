package com.abledenthusiast.emento;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Value;

import javax.inject.Singleton;

@Singleton
public class EmentoProperties {

    @Property(name = "sendgrid.api-key")
    protected String sendGridApiKey;


    public String sendGridAPIKey() {
        System.out.println("sendgrid api key 1 " + System.getProperty("sendgrid.api"));
        return sendGridApiKey;
    }
}
