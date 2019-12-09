package com.abledenthusiast.emento;

public class EmentoProperties {
    private final static String SENDGRID_API_KEY = "sendgrid_api_key";

    public static String sendGridAPIKey() {
        return System.getProperty(SENDGRID_API_KEY, "I_AM_NOT_A_KEY");
    }
}
