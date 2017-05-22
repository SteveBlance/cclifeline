package com.codaconsultancy.cclifeline.domain;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class NotificationTest {

    private Notification notification;

    @Before
    public void setUp() throws Exception {
        notification = new Notification();
        notification.setId(92L);
        notification.setEventType("DRAW");
        Long drawId = 88L;
        notification.setEventId(drawId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date eventDate = sdf.parse("2017-05-13");
        notification.setEventDate(eventDate);
    }

    @Test
    public void getId() throws Exception {
        assertEquals(92L, notification.getId().longValue());
    }

    @Test
    public void getEventDate() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        assertEquals("2017-05-13", sdf.format(notification.getEventDate()));
    }

    @Test
    public void getEventType() throws Exception {
        assertEquals("DRAW", notification.getEventType());
    }

    @Test
    public void getEventId() throws Exception {
        assertEquals(88L, notification.getEventId().longValue());
    }

}