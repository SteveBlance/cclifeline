package com.codaconsultancy.cclifeline.view;

import com.codaconsultancy.cclifeline.domain.Payment;
import com.codaconsultancy.cclifeline.domain.Prize;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MemberViewBeanTest {
    private MemberViewBean memberViewBean;

    @Before
    public void setUp() throws ParseException {
        memberViewBean = new MemberViewBean();
        memberViewBean.setId(99L);
        memberViewBean.setMembershipNumber(1818L);
        memberViewBean.setForename("Hamish");
        memberViewBean.setSurname("Petrie");
        memberViewBean.setComments("Migrated memberViewBean");
        memberViewBean.setEmail("hp@email.com");
        memberViewBean.setMembershipType("Lifeline");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date joinDate = sdf.parse("31-08-2014");
        Date leaveDate = sdf.parse("31-08-2015");
        Date cardRequestDate = sdf.parse("13-08-2014");
        Date cardIssuedDate = sdf.parse("30-08-2014");
        Date welcomeLetterIssuedDate = sdf.parse("31-08-2014");
        Date lastPaymentDate = sdf.parse("31-05-2015");
        memberViewBean.setJoinDate(joinDate);
        memberViewBean.setLeaveDate(leaveDate);
        Payment lastPayment = new Payment(lastPaymentDate, 20.00F, "BL123", "12354678", "PETRIE", true);
        memberViewBean.setLastPayment(lastPayment);
        memberViewBean.setCardRequestDate(cardRequestDate);
        memberViewBean.setCardIssuedDate(cardIssuedDate);
        memberViewBean.setWelcomeLetterIssuedDate(welcomeLetterIssuedDate);
        memberViewBean.setLandlineNumber("01383 665544");
        memberViewBean.setMobileNumber("07766554433");
        memberViewBean.setPayerType("Monthly");
        memberViewBean.setStatus("Closed");
        memberViewBean.setEligibleForDrawStored(true);
        List<AddressViewBean> addresses = new ArrayList<>();
        AddressViewBean address1 = new AddressViewBean();
        address1.setId(23L);
        AddressViewBean address2 = new AddressViewBean();
        address2.setId(84L);
        addresses.add(address1);
        addresses.add(address2);
        memberViewBean.setAddresses(addresses);
    }

    @Test
    public void getId() {
        assertEquals(99L, memberViewBean.getId().longValue());
    }

    @Test
    public void getMembershipNumber() {
        assertEquals(1818L, memberViewBean.getMembershipNumber().longValue());
        assertEquals(1818L, memberViewBean.toEntity().getMembershipNumber().longValue());
    }

    @Test
    public void getMembershipType() {
        assertEquals("Lifeline", memberViewBean.getMembershipType());
    }

    @Test
    public void getStatus() {
        assertEquals("Closed", memberViewBean.getStatus());
    }

    @Test
    public void getForename() {
        assertEquals("Hamish", memberViewBean.getForename());
    }

    @Test
    public void getSurname() {
        assertEquals("Petrie", memberViewBean.getSurname());
    }

    @Test
    public void getPayerType() {
        assertEquals("Monthly", memberViewBean.getPayerType());
    }

    @Test
    public void getJoinDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date joinDate = memberViewBean.getJoinDate();
        String dateString = sdf.format(joinDate);
        assertEquals("31-08-2014", dateString);
    }

    @Test
    public void getLeaveDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date leaveDate = memberViewBean.getLeaveDate();
        String dateString = sdf.format(leaveDate);
        assertEquals("31-08-2015", dateString);
    }

    @Test
    public void getComments() {
        assertEquals("Migrated memberViewBean", memberViewBean.getComments());
    }

    @Test
    public void getEmail() {
        assertEquals("hp@email.com", memberViewBean.getEmail());
    }

    @Test
    public void getLandlineNumber() {
        assertEquals("01383 665544", memberViewBean.getLandlineNumber());
    }

    @Test
    public void getMobileNumber() {
        assertEquals("07766554433", memberViewBean.getMobileNumber());
    }

    @Test
    public void getCardRequestDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date cardRequestDate = memberViewBean.getCardRequestDate();
        String dateString = sdf.format(cardRequestDate);
        assertEquals("13-08-2014", dateString);
    }

    @Test
    public void getCardIssuedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date cardIssuedDate = memberViewBean.getCardIssuedDate();
        String dateString = sdf.format(cardIssuedDate);
        assertEquals("30-08-2014", dateString);
    }

    @Test
    public void getWelcomeLetterIssuedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date welcomeLetterIssuedDate = memberViewBean.getWelcomeLetterIssuedDate();
        String dateString = sdf.format(welcomeLetterIssuedDate);
        assertEquals("31-08-2014", dateString);
    }

    @Test
    public void getAddresses() {
        assertEquals(2, memberViewBean.getAddresses().size());
        assertEquals(23L, memberViewBean.getAddresses().get(0).getId().longValue());
        assertEquals(84L, memberViewBean.getAddresses().get(1).getId().longValue());
    }

    @Test
    public void getPrizeWins() {
        List<Prize> wins = new ArrayList<>();
        wins.add(new Prize());
        wins.add(new Prize());
        wins.add(new Prize());
        memberViewBean.setPrizeWins(wins);
        assertEquals(3, memberViewBean.getPrizeWins().size());
    }

    @Test
    public void isEligibleForDrawStored() {
        assertTrue(memberViewBean.isEligibleForDrawStored());
    }

    @Test
    public void getLastPayment() {
        Payment lastPayment = memberViewBean.getLastPayment();
        assertEquals(20.00F, lastPayment.getPaymentAmount(), 0.00F);
        assertEquals("PETRIE", lastPayment.getName());
        assertEquals("BL123", lastPayment.getCreditReference());
    }

}