package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.Notification;
import com.codaconsultancy.cclifeline.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }


}
