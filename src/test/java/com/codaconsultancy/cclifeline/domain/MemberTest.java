package com.codaconsultancy.cclifeline.domain;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        Date joinDate = sdf.parse("31-08-2014");
        Date leaveDate = sdf.parse("31-08-2015");
        Date cardRequestDate = sdf.parse("13-08-2014");
        Date cardIssuedDate = sdf.parse("30-08-2014");
        Date welcomeLetterIssuedDate = sdf.parse("31-08-2014");
        member.setJoinDate(joinDate);
        member.setLeaveDate(leaveDate);
        member.setCardRequestDate(cardRequestDate);
        member.setCardIssuedDate(cardIssuedDate);
        member.setWelcomeLetterIssuedDate(welcomeLetterIssuedDate);
        member.setLandlineNumber("01383 665544");
        member.setMobileNumber("07766554433");
        member.setPayerType("Monthly");
        member.setStatus("Closed");
        member.setEligibleForDrawStored(true);
        List<Address> addresses = new ArrayList<>();
        Address address1 = new Address();
        address1.setId(23L);
        Address address2 = new Address();
        address2.setId(84L);
        addresses.add(address1);
        addresses.add(address2);
        member.setAddresses(addresses);
    }

    @Test
    public void getId() {
        assertEquals(27L, member.getId().longValue());
        assertEquals(27L, member.toViewBean().getId().longValue());

    }

    @Test
    public void getMembershipNumber() {
        assertEquals(1818L, member.getMembershipNumber().longValue());
        assertEquals(1818L, member.toViewBean().getMembershipNumber().longValue());
    }

    @Test
    public void getMembershipType() {
        assertEquals("Lifeline", member.getMembershipType());
    }

    @Test
    public void getStatus() {
        assertEquals("Closed", member.getStatus());
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
    public void getLeaveDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date leaveDate = member.getLeaveDate();
        String dateString = sdf.format(leaveDate);
        assertEquals("31-08-2015", dateString);
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

    @Test
    public void getCardRequestDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date cardRequestDate = member.getCardRequestDate();
        String dateString = sdf.format(cardRequestDate);
        assertEquals("13-08-2014", dateString);
    }

    @Test
    public void getCardIssuedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date cardIssuedDate = member.getCardIssuedDate();
        String dateString = sdf.format(cardIssuedDate);
        assertEquals("30-08-2014", dateString);
    }

    @Test
    public void getWelcomeLetterIssuedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date welcomeLetterIssuedDate = member.getWelcomeLetterIssuedDate();
        String dateString = sdf.format(welcomeLetterIssuedDate);
        assertEquals("31-08-2014", dateString);
    }

    @Test
    public void getAddresses() {
        assertEquals(2, member.getAddresses().size());
        assertEquals(23L, member.getAddresses().get(0).getId().longValue());
        assertEquals(84L, member.getAddresses().get(1).getId().longValue());
    }

    @Test
    public void getPrizeWins() {
        List<Prize> wins = new ArrayList<>();
        wins.add(new Prize());
        wins.add(new Prize());
        member.setPrizeWins(wins);
        assertEquals(2, member.getPrizeWins().size());
    }

    @Test
    public void isElegibleForDrawStored() {
        assertTrue(member.isEligibleForDrawStored());
    }
}