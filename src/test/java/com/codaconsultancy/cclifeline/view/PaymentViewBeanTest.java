package com.codaconsultancy.cclifeline.view;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.domain.Payment;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class PaymentViewBeanTest {
    private PaymentViewBean paymentViewBean;

    @Before
    public void setUp() throws ParseException {
        paymentViewBean = new PaymentViewBean();
        paymentViewBean.setId(44L);
        paymentViewBean.setPaymentAmount(23.89F);
        paymentViewBean.setCreditReference("FPS CREDIT 0299");
        paymentViewBean.setName("SMITH");
        paymentViewBean.setCreditedAccount("800599 0011223344");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = "30/09/2017";
        Date paymentDate = dateFormat.parse(dateInString);
        paymentViewBean.setPaymentDate(paymentDate);
        paymentViewBean.setStoreReferenceForMatching(true);
        Member member = new Member();
        member.setId(67L);
        paymentViewBean.setMemberId(member.getId());
        paymentViewBean.setLotteryPayment(true);
        paymentViewBean.setComments("Paid at meeting");
    }

    @Test
    public void getId() {
        assertEquals(44L, paymentViewBean.getId().longValue());
        assertEquals(44L, paymentViewBean.toEntity().getId().longValue());

    }

    @Test
    public void getPaymentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date paymentDate = paymentViewBean.getPaymentDate();
        String dateString = dateFormat.format(paymentDate);
        assertEquals("30/09/2017", dateString);
    }

    @Test
    public void getPaymentAmount() {
        assertEquals(23.89F, paymentViewBean.getPaymentAmount(), 0.001);
        assertEquals(23.89F, paymentViewBean.toEntity().getPaymentAmount(), 0.001);
    }

    @Test
    public void getCreditReference() {
        assertEquals("FPS CREDIT 0299", paymentViewBean.getCreditReference());
    }

    @Test
    public void getName() {
        assertEquals("SMITH", paymentViewBean.getName());
    }

    @Test
    public void getCreditedAccount() {
        assertEquals("800599 0011223344", paymentViewBean.getCreditedAccount());
    }

    @Test
    public void getMemberId() {
        assertEquals(67L, paymentViewBean.getMemberId().longValue());
    }

    @Test
    public void isStoreReferenceForMatching() {
        assertTrue(paymentViewBean.isStoreReferenceForMatching());
    }

    @Test
    public void isLotteryPayment() {
        assertTrue(paymentViewBean.isLotteryPayment());
        paymentViewBean.setLotteryPayment(false);
        assertFalse(paymentViewBean.isLotteryPayment());
    }

    @Test
    public void comments() {
        assertEquals("Paid at meeting", paymentViewBean.getComments());
    }

    @Test
    public void testConstructor() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = simpleDateFormat.parse("26/03/2016");
        PaymentViewBean paymentViewBean = new PaymentViewBean(date, 20.00F, "FPS CREDIT 9888 MONK", "Lifeline Account", "BOB SMITH", true);
        assertEquals("26/03/2016", simpleDateFormat.format(paymentViewBean.getPaymentDate()));
        assertEquals(20.00F, paymentViewBean.getPaymentAmount(), 0.001);
        assertEquals("FPS CREDIT 9888 MONK", paymentViewBean.getCreditReference());
        assertEquals("Lifeline Account", paymentViewBean.getCreditedAccount());
    }

    @Test
    public void toEntity() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = simpleDateFormat.parse("26/03/2016");
        PaymentViewBean paymentViewBean = new PaymentViewBean(date, 20.00F, "FPS CREDIT 9888 MONK", "Lifeline Account", "BOB SMITH", true);
        paymentViewBean.setComments("Paid by cheque");
        Payment payment = paymentViewBean.toEntity();
        assertTrue(payment.isLotteryPayment());
        assertEquals("Paid by cheque", payment.getComments());
    }

}

