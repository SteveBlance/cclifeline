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
        notification.setDescription("New Lottery Draw");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date eventDate = sdf.parse("2011-05-13");
        notification.setEventDate(eventDate);
    }

    @Test
    public void getId() throws Exception {
        assertEquals(92L, notification.getId().longValue());
    }

    @Test
    public void getEventDate() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        assertEquals("2011-05-13", sdf.format(notification.getEventDate()));
        assertEquals("7 years ago", notification.getPrettyTime());
    }

    @Test
    public void getEventType() throws Exception {
        assertEquals("DRAW", notification.getEventType());
    }

    @Test
    public void getDescription() throws Exception {
        assertEquals("New Lottery Draw", notification.getDescription());
    }

    @Test
    public void getFontAwesomeIcon() {
        notification.setEventType("Announcement");
        assertEquals("<i class=\"fa fa-bullhorn\" aria-hidden=\"true\"></i>", notification.getFontAwesomeIcon());
        notification.setEventType("Draw");
        assertEquals("<i class=\"fa fa-ticket\" aria-hidden=\"true\"></i>", notification.getFontAwesomeIcon());
        notification.setEventType("NewMember");
        assertEquals("<i class=\"fa fa-user\" aria-hidden=\"true\"></i>", notification.getFontAwesomeIcon());
        notification.setEventType("Twitter");
        assertEquals("<i class=\"fa fa-twitter\" aria-hidden=\"true\"></i>", notification.getFontAwesomeIcon());
        notification.setEventType("Facebook");
        assertEquals("<i class=\"fa fa-facebook-official\" aria-hidden=\"true\"></i>", notification.getFontAwesomeIcon());
        notification.setEventType("Payment");
        assertEquals("<i class=\"fa fa-money\" aria-hidden=\"true\"></i>", notification.getFontAwesomeIcon());
        notification.setEventType("Warning");
        assertEquals("<i class=\"fa fa-exclamation-triangle\" aria-hidden=\"true\"></i>", notification.getFontAwesomeIcon());
        notification.setEventType("Wibble");
        assertEquals("<i class=\"fa fa-comment fa-fw\"></i>", notification.getFontAwesomeIcon());
    }

}