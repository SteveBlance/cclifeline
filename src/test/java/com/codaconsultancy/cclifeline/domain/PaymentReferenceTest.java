package com.codaconsultancy.cclifeline.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PaymentReferenceTest {

    private PaymentReference paymentReference;

    @Before
    public void setUp() {
        paymentReference = new PaymentReference();
        paymentReference.setId(56L);
        paymentReference.setReference("FPS CREDIT 1234 H PETRIE");
        paymentReference.setName("H Petrie");
        paymentReference.setIsActive(true);
        Member member = new Member();
        member.setForename("Hamish");
        member.setSurname("Petrie");
        paymentReference.setMember(member);
    }

    @Test
    public void getId() {
        assertEquals(56L, paymentReference.getId().longValue());
    }

    @Test
    public void getReference() {
        assertEquals("FPS CREDIT 1234 H PETRIE", paymentReference.getReference());
    }

    @Test
    public void getActive() {
        assertTrue(paymentReference.getIsActive());
    }

    @Test
    public void getName() {
        assertEquals("H Petrie", paymentReference.getName());
    }

    @Test
    public void getMember() {
        assertEquals("Hamish", paymentReference.getMember().getForename());
        assertEquals("Petrie", paymentReference.getMember().getSurname());
    }

    @Test
    public void testConstructor() {
        Member member = new Member();
        member.setSurname("Petrie");
        PaymentReference paymentReference = new PaymentReference("FPS CREDIT 9988", "H PETRIE", true, member);
        assertEquals("FPS CREDIT 9988", paymentReference.getReference());
        assertEquals("H PETRIE", paymentReference.getName());
        assertEquals("Petrie", paymentReference.getMember().getSurname());
        assertTrue(paymentReference.getIsActive());
    }

}