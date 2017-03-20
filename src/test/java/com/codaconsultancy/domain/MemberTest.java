package com.codaconsultancy.domain;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class MemberTest {

    private Member member;

    @Before
    public void setUp() throws ParseException {
        member = new Member();
        member.setId(27L);
        member.setMembershipNumber(1818L);
        member.setForename("Hamish");
        member.setSurname("Petrie");
        member.setComments("Migrated member");
        member.setEmail("hp@email.com");
        member.setMembershipType("Lifeline");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateInString = "31-08-2014";
        Date date = sdf.parse(dateInString);
        member.setJoinDate(date);
        member.setLandlineNumber("01383 665544");
        member.setMobileNumber("07766554433");
        member.setPayerType("Monthly");
        member.setStatus("Open");
    }

    @Test
    public void getId() {
        assertEquals(27L, member.getId().longValue());
    }

    @Test
    public void getMembershipNumber() {
        assertEquals(1818L, member.getMembershipNumber().longValue());
    }

    @Test
    public void getMembershipType() {
        assertEquals("Lifeline", member.getMembershipType());
    }

    @Test
    public void getStatus() {
        assertEquals("Open", member.getStatus());
    }

    @Test
    public void getForename() {
        assertEquals("Hamish", member.getForename());
    }

    @Test
    public void getSurname() {
        assertEquals("Petrie", member.getSurname());
    }

    @Test
    public void getPayerType() {
        assertEquals("Monthly", member.getPayerType());
    }

    @Test
    public void getJoinDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date joinDate = member.getJoinDate();
        String dateString = sdf.format(joinDate);
        assertEquals("31-08-2014", dateString);
    }

    @Test
    public void getComments() {
        assertEquals("Migrated member", member.getComments());
    }

    @Test
    public void getEmail() {
        assertEquals("hp@email.com", member.getEmail());
    }

    @Test
    public void getLandlineNumber() {
        assertEquals("01383 665544", member.getLandlineNumber());
    }

    @Test
    public void getMobileNumber() {
        assertEquals("07766554433", member.getMobileNumber());
    }

}