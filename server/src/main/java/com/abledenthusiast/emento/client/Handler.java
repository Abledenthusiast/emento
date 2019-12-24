package com.abledenthusiast.emento.client;

import com.abledenthusiast.emento.scheduling.notifications.Notification;


public interface Handler {
    void handleNotification(Notification notification) throws Exception;
}
