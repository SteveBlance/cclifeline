package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    public List<Notification> findFirst10ByOrderByEventDateDesc();

}

