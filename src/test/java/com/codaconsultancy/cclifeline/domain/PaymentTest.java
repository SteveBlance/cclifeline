package com.codaconsultancy.cclifeline.domain;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class PaymentTest {

    private Payment payment;

    @Before
    public void setUp() throws ParseException {
        payment = new Payment();
        payment.setId(44L);
        payment.setPaymentAmount(23.89F);
        payment.setCreditedAccount("800599 0011223344");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = "30/09/2017";
        Date paymentDate = dateFormat.parse(dateInString);
        payment.setPaymentDate(paymentDate);
        Member member = new Member();
        member.setForename("Hamish");
        member.setSurname("Petrie");
        payment.setMember(member);
    }

    @Test
    public void getId() {
        assertEquals(44L, payment.getId().longValue());
    }

    @Test
    public void getPaymentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date paymentDate = payment.getPaymentDate();
        String dateString = dateFormat.format(paymentDate);
        assertEquals("30/09/2017", dateString);
    }

    @Test
    public void getPaymentAmount() {
        assertEquals(23.89F, payment.getPaymentAmount(), 0.001);
    }

    @Test
    public void getCreditedAccount() {
        assertEquals("800599 0011223344", payment.getCreditedAccount());
    }

    @Test
    public void getMember() {
        assertEquals("Hamish", payment.getMember().getForename());
        assertEquals("Petrie", payment.getMember().getSurname());
    }

    @Test
    public void testConstructor() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = simpleDateFormat.parse("26/03/2016");
        Payment payment = new Payment(date, 20.00F, "Lifeline Account");
        assertEquals("26/03/2016", simpleDateFormat.format(payment.getPaymentDate()));
        assertEquals(20.00F, payment.getPaymentAmount(), 0.001);
        assertEquals("Lifeline Account", payment.getCreditedAccount());
    }

}