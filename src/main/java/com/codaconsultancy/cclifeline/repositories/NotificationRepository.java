package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    public List<Notification> findFirst10ByOrderByEventDateDesc();

}

