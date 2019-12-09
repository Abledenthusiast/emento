package com.abledenthusiast.emento.client;


import com.abledenthusiast.emento.EmentoProperties;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class EmailHandler {
    private static final Logger log = LoggerFactory.getLogger(EmailHandler.class);

    private SendGrid sendGrid;

    public EmailHandler() {
        log.info("sendgrid key is non-null: {}", new EmentoProperties().sendGridAPIKey());
        sendGrid = new SendGrid(new EmentoProperties().sendGridAPIKey());
    }


    public void send(Email from, List<Email> recipients) throws IOException {
        Email fromEmail = from;
        String subject = "Sending with Twilio SendGrid is Fun";
        Email to = recipients.get(0);
        Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
        Mail mail = new Mail(fromEmail, subject, to, content);

        Personalization personalization = new Personalization();
        personalization.addTo(to);
        mail.addPersonalization(personalization);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }
}


