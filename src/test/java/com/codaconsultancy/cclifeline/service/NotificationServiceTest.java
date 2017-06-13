package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.Notification;
import com.codaconsultancy.cclifeline.repositories.NotificationRepository;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@EntityScan("com.codaconsultancy.cclifeline.domain")
@SpringBootTest(classes = NotificationService.class)
public class NotificationServiceTest {


    @Autowired
    private NotificationService notificationService;

    @MockBean
    NotificationRepository notificationRepository;

    @Test
    public void saveNotification() throws Exception {

        Notification notification = new Notification(DateTime.now().toDate(), "Announcement", "New draw announced");
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification savedNotification = notificationService.saveNotification(notification);

        verify(notificationRepository, times(1)).save(notification);
        assertEquals("New draw announced", savedNotification.getDescription());
    }

    @Test
    public void fetchLatestNotifications() {
        List<Notification> notifications = new ArrayList<>();
        when(notificationRepository.findFirst10ByOrderByEventDateDesc()).thenReturn(notifications);

        List<Notification> fetchedNotifications = notificationService.fetchLatestNotifications();

        verify(notificationRepository, times(1)).findFirst10ByOrderByEventDateDesc();
        assertSame(notifications, fetchedNotifications);
    }

    @Test
    public void logLotteryDraw_success_noName() {
        ArgumentCaptor<Notification> notificationArgumentCaptor = ArgumentCaptor.forClass(Notification.class);

        notificationService.logLotteryDraw("");

        verify(notificationRepository, times(1)).save(notificationArgumentCaptor.capture());
        assertEquals("Draw", notificationArgumentCaptor.getValue().getEventType());
        assertEquals(DateTime.now().getDayOfYear(), new DateTime(notificationArgumentCaptor.getValue().getEventDate()).getDayOfYear());
        assertEquals("Lottery Draw Made", notificationArgumentCaptor.getValue().getDescription());
    }

    @Test
    public void logLotteryDraw_success_withName() {
        ArgumentCaptor<Notification> notificationArgumentCaptor = ArgumentCaptor.forClass(Notification.class);

        notificationService.logLotteryDraw("May Madness");

        verify(notificationRepository, times(1)).save(notificationArgumentCaptor.capture());
        assertEquals("Draw", notificationArgumentCaptor.getValue().getEventType());
        assertEquals(DateTime.now().getDayOfYear(), new DateTime(notificationArgumentCaptor.getValue().getEventDate()).getDayOfYear());
        assertEquals("Lottery Draw Made - May Madness", notificationArgumentCaptor.getValue().getDescription());
    }

    @Test
    public void logNewMemberAdded() {
        DateTime dateTime = DateTime.parse("21/12/2011", DateTimeFormat.forPattern("dd/mm/yyyyy"));
        Date joinDate = dateTime.toDate();
        ArgumentCaptor<Notification> notificationArgumentCaptor = ArgumentCaptor.forClass(Notification.class);

        notificationService.logNewMemberAdded(joinDate);

        verify(notificationRepository, times(1)).save(notificationArgumentCaptor.capture());
        assertEquals("NewMember", notificationArgumentCaptor.getValue().getEventType());
        assertEquals(joinDate, notificationArgumentCaptor.getValue().getEventDate());
        assertEquals("New Member Added", notificationArgumentCaptor.getValue().getDescription());
    }

}