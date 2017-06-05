package com.codaconsultancy.cclifeline.view;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.domain.PaymentReference;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PaymentReferenceViewBeanTest {

    private PaymentReferenceViewBean paymentReferenceViewBean;

    @Before
    public void setUp() {
        paymentReferenceViewBean = new PaymentReferenceViewBean();
        paymentReferenceViewBean.setId(56L);
        paymentReferenceViewBean.setReference("FPS CREDIT 1234 H PETRIE");
        paymentReferenceViewBean.setName("H Petrie");
        paymentReferenceViewBean.setIsActive(true);
        Member member = new Member();
        member.setForename("Hamish");
        member.setSurname("Petrie");
        paymentReferenceViewBean.setMember(member);
    }

    @Test
    public void getId() {
        assertEquals(56L, paymentReferenceViewBean.getId().longValue());
        assertEquals(56L, paymentReferenceViewBean.toEntity().getId().longValue());
    }

    @Test
    public void getReference() {
        assertEquals("FPS CREDIT 1234 H PETRIE", paymentReferenceViewBean.getReference());
    }

    @Test
    public void getActive() {
        assertTrue(paymentReferenceViewBean.getIsActive());
    }

    @Test
    public void getName() {
        assertEquals("H Petrie", paymentReferenceViewBean.getName());
    }

    @Test
    public void getMember() {
        assertEquals("Hamish", paymentReferenceViewBean.getMember().getForename());
        assertEquals("Petrie", paymentReferenceViewBean.getMember().getSurname());
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