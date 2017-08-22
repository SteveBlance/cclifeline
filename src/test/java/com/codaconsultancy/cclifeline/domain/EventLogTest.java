package com.codaconsultancy.cclifeline.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventLogTest {

    private EventLog eventLog;

    @Before
    public void setup() {
        eventLog = new EventLog("User logged in");
    }

    @Test
    public void getId() throws Exception {
        eventLog.setId(9L);
        assertEquals(9L, eventLog.getId().longValue());
    }

    @Test
    public void getMessage() throws Exception {
        assertEquals("User logged in", eventLog.getMessage());
        eventLog.setMessage("User logged out");
        assertEquals("User logged out", eventLog.getMessage());
    }

}