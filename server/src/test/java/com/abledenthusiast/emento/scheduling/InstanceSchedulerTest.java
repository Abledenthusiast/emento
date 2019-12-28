package com.abledenthusiast.emento.scheduling;

import com.abledenthusiast.emento.client.Handler;
import com.abledenthusiast.emento.dto.Schedule;
import com.abledenthusiast.emento.scheduling.ServiceBus.ServiceBusProducer;
import com.abledenthusiast.emento.scheduling.notifications.Notification;
import com.abledenthusiast.emento.scheduling.tasks.ExecutionTask;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

import java.time.Duration;
import java.time.Instant;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class InstanceSchedulerTest {

    private ServiceBusProducer serviceBusProducer;
    private InstanceScheduler instanceScheduler;

    @BeforeClass
    public void initTest() {
        serviceBusProducer = mock(ServiceBusProducer.class);
        Handler handler = mock(Handler.class);
        instanceScheduler = new InstanceScheduler(handler, serviceBusProducer);
    }

    @org.testng.annotations.Test
    public void testQueueScheduling() {
        ExecutionTask task = mock(ExecutionTask.class);
        Notification notification = mock(Notification.class);
        when(task.executionInstant()).thenReturn(Instant.now().plus(Duration.ofMinutes(15)));
        when(task.getNotification()).thenReturn(notification);

        instanceScheduler.schedule(task);
        verify(serviceBusProducer).push(notification);
    }

    @org.testng.annotations.Test
    public void testImmediateScheduling() {
        ExecutionTask task = mock(ExecutionTask.class);
        Notification notification = mock(Notification.class);
        when(task.executionInstant()).thenReturn(Instant.now().plus(Duration.ofSeconds(0)));
        when(task.executionTime()).thenReturn(Duration.ofSeconds(0));

        instanceScheduler.schedule(task);
        verify(task).execute(any(Handler.class));
    }
}