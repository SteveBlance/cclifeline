package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.EventLog;
import com.codaconsultancy.cclifeline.repositories.EventLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LifelineService {

    @Autowired
    private EventLogRepository eventLogRepository;

    public void logMessage(String message) {
        eventLogRepository.save(new EventLog(message));
    }
}
