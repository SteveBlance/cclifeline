package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.repositories.EventLogRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.mock.mockito.MockBean;


@EntityScan("com.codaconsultancy.cclifeline.domain")
public class LifelineServiceTest {

    @MockBean
    protected EventLogRepository eventLogRepository;
}
