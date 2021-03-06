package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.Notification;
import com.codaconsultancy.cclifeline.repositories.NotificationRepository;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NotificationService.class)
public class NotificationServiceTest extends LifelineServiceTest {

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
        ArgumentCaptor<Notification> notificationArgumentCaptor = ArgumentCaptor.forClass(Notification.class);

        notificationService.logNewMemberAdded();

        verify(notificationRepository, times(1)).save(notificationArgumentCaptor.capture());
        assertEquals("NewMember", notificationArgumentCaptor.getValue().getEventType());
        assertEquals(DateTime.now().getDayOfYear(), new DateTime(notificationArgumentCaptor.getValue().getEventDate()).getDayOfYear());
        assertEquals("New Member Added", notificationArgumentCaptor.getValue().getDescription());
    }

    @Test
    public void logManualPayment() {
        ArgumentCaptor<Notification> notificationArgumentCaptor = ArgumentCaptor.forClass(Notification.class);

        notificationService.logPayment(1);

        verify(notificationRepository, times(1)).save(notificationArgumentCaptor.capture());
        assertEquals("Payment", notificationArgumentCaptor.getValue().getEventType());
        assertEquals("Payment Entered Manually", notificationArgumentCaptor.getValue().getDescription());
        assertEquals(DateTime.now().getDayOfYear(), new DateTime(notificationArgumentCaptor.getValue().getEventDate()).getDayOfYear());
    }

    @Test
    public void logImportedPayments() {
        ArgumentCaptor<Notification> notificationArgumentCaptor = ArgumentCaptor.forClass(Notification.class);

        notificationService.logPayment(2478);

        verify(notificationRepository, times(1)).save(notificationArgumentCaptor.capture());
        assertEquals(DateTime.now().getDayOfYear(), new DateTime(notificationArgumentCaptor.getValue().getEventDate()).getDayOfYear());
        assertEquals("Payment", notificationArgumentCaptor.getValue().getEventType());
        assertEquals("2478 Payments Imported", notificationArgumentCaptor.getValue().getDescription());
    }
}