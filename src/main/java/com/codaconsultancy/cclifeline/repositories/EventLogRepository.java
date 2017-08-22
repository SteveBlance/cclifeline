package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.domain.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventLogRepository extends JpaRepository<EventLog, Long> {
}

