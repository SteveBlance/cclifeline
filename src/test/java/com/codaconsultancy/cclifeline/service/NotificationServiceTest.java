package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.Notification;
import com.codaconsultancy.cclifeline.repositories.NotificationRepository;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
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

}