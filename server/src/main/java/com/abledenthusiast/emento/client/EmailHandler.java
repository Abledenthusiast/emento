package com.abledenthusiast.emento.client;


import com.abledenthusiast.emento.EmentoProperties;
import com.abledenthusiast.emento.scheduling.notifications.Notification;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class EmailHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(EmailHandler.class);

    private SendGrid sendGrid;


    public EmailHandler(EmentoProperties ementoProperties) {
        sendGrid = new SendGrid(ementoProperties.sendgridApiKey());
    }

    @Override
    public void handleNotification(Notification notification) throws Exception {
        send(notification.getCreator(), notification.getDestinations(), notification.getMessageTitle(), notification.getMessage());
    }


    private void send(Email from, List<Email> recipients, String subject, String messageBody) throws IOException {
        Email fromEmail = from;
        Content content = new Content("text/plain", messageBody);

        Mail mail = new Mail();
        mail.setSubject(subject);
        mail.addContent(content);
        mail.setFrom(fromEmail);

        for (Email to : recipients) {
            Personalization personalization = new Personalization();
            personalization.addTo(to);
            mail.addPersonalization(personalization);
        }

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            log.info("sendGrid response status code: {}", response.getStatusCode());
            log.info(response.getBody());
            log.info("sendGrid response headers: {}", response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }
}


