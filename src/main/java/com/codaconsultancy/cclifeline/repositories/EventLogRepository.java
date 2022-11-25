package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.domain.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLogRepository extends JpaRepository<EventLog, Long> {
}

