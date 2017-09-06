package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.common.TestHelper;
import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.domain.Payment;
import com.codaconsultancy.cclifeline.view.PaymentViewBean;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest(classes = Payment.class)
public class PaymentRepositoryTest extends BaseTest {

    @Autowired
    private PaymentRepository paymentRepository;

    private Member member;
    private Payment payment1;
    private Payment payment2;
    private Payment payment3;
    private Payment payment4;

    @Before
    public void setUp() throws Exception {
        member = TestHelper.newMember(5566L, "Jim", "Saunders", "jimbo@email.com", "01383 226655", "0778 866 5544", "Monthly", "Lifeline", "New member", "Open");
        entityManager.persist(member);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = simpleDateFormat.parse("23/11/2014");
        Date date2 = simpleDateFormat.parse("30/11/2014");
        payment1 = new Payment(date1, 20.00F, "FPS CREDIT 0299 SMITH", "Lifeline Current Account", "BOB SMITH", true);
        payment1.setMember(member);
        payment2 = new Payment(date2, 8.66F, "FPS CREDIT 0222 LINDSAY", "Legacy Current Account", "BOB SMITH", true);
        payment3 = new Payment(date2, 20.00F, "FPS CREDIT 0299 SMITH", "Lifeline Current Account", "BOB SMITH", true);
        payment3.setMember(member);
        payment4 = new Payment(date2, 500.00F, "POTY SPONSORSHIP", "Lifeline Current Account", "JOHN SMITH", false);
        payment4.setLotteryPayment(false);
        entityManager.persist(payment1);
        entityManager.persist(payment2);
        entityManager.persist(payment3);
        entityManager.persist(payment4);
    }

    @After
    public void tearDown() throws Exception {
        entityManager.remove(payment1);
        entityManager.remove(payment2);
        entityManager.remove(payment3);
        entityManager.remove(member);
        entityManager.remove(payment4);
    }

    @Test
    public void findAll() throws Exception {
        assertEquals(4, paymentRepository.findAll().size());
    }

    @Test
    public void findAllPayments() throws Exception {
        List<PaymentViewBean> allPayments = paymentRepository.findAllPayments();
        assertEquals(4, allPayments.size());
        assertEquals("5566: Jim Saunders", allPayments.get(0).getMemberDisplayText());
        assertNull(allPayments.get(1).getMemberDisplayText());
        assertEquals("5566: Jim Saunders", allPayments.get(2).getMemberDisplayText());
        assertNull(allPayments.get(3).getMemberDisplayText());
    }

    @Test
    public void findByMember() throws Exception {
        List<Payment> paymentsByMember = paymentRepository.findByMember(member);

        assertEquals(2, paymentsByMember.size());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        assertEquals("23/11/2014", simpleDateFormat.format(paymentsByMember.get(0).getPaymentDate()));
        assertEquals(20.00F, paymentsByMember.get(0).getPaymentAmount(), 0.001F);
        assertEquals("Lifeline Current Account", paymentsByMember.get(0).getCreditedAccount());
    }

    @Test
    public void findByPaymentDateAfter() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = simpleDateFormat.parse("29/11/2014");
        List<PaymentViewBean> allPayments = paymentRepository.findAllPayments();
        assertEquals(4, allPayments.size());
        List<PaymentViewBean> payments = paymentRepository.findAllPaymentsAfter(date);
        assertEquals(3, payments.size());
        assertTrue(payments.get(0).getPaymentDate().after(date));
        assertTrue(payments.get(1).getPaymentDate().after(date));
        assertTrue(payments.get(2).getPaymentDate().after(date));
    }

    @Test
    public void findAllPaymentsAfter() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = simpleDateFormat.parse("29/11/2014");
        List<PaymentViewBean> allPayments = paymentRepository.findAllPayments();
        assertEquals(4, allPayments.size());
        List<PaymentViewBean> payments = paymentRepository.findAllPaymentsAfter(date);
        assertEquals(3, payments.size());
        assertTrue(payments.get(0).getPaymentDate().after(date));
        assertTrue(payments.get(1).getPaymentDate().after(date));
        assertTrue(payments.get(2).getPaymentDate().after(date));
    }

    @Test
    public void findLatestPaymentForMember_success() {
        Payment latestPayment = paymentRepository.findTopByMemberAndIsLotteryPaymentOrderByPaymentDateDesc(member, true);
        assertEquals(20.00F, latestPayment.getPaymentAmount(), 0.002F);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        assertEquals("30/11/2014", simpleDateFormat.format(latestPayment.getPaymentDate()));
    }

    @Test
    public void findLatestPaymentForMember_noPayments() {
        Member newMember = TestHelper.newMember(5566L, "Jim", "Saunders", "jimbo@email.com", "01383 226655", "0778 866 5544", "Monthly", "Lifeline", "New member", "Open");
        entityManager.persist(newMember);

        Payment latestPayment = paymentRepository.findTopByMemberAndIsLotteryPaymentOrderByPaymentDateDesc(newMember, true);
        assertNull(latestPayment);
    }

    @Test
    public void findLatestPaymentForMember_noLotteryPayments() {
        Member newMember = TestHelper.newMember(5566L, "Jim", "Saunders", "jimbo@email.com", "01383 226655", "0778 866 5544", "Monthly", "Lifeline", "New member", "Open");
        entityManager.persist(newMember);
        Payment nonLotteryPayment = new Payment(DateTime.now().toDate(), 500.00F, "POTY SPONSORSHIP", "Lifeline Current Account", "JIM SAUNDERS", true);
        nonLotteryPayment.setLotteryPayment(false);
        nonLotteryPayment.setMember(newMember);
        entityManager.persist(nonLotteryPayment);

        Payment latestPayment = paymentRepository.findTopByMemberAndIsLotteryPaymentOrderByPaymentDateDesc(newMember, true);
        assertNull(latestPayment);
    }

    @Test
    public void findByMemberIsNullAndIsLotteryPayment() throws Exception {
        List<Payment> unmatchedPayments = paymentRepository.findByMemberIsNullAndIsLotteryPayment(true);

        assertEquals(1, unmatchedPayments.size());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        assertEquals("30/11/2014", simpleDateFormat.format(unmatchedPayments.get(0).getPaymentDate()));
        assertEquals(8.66F, unmatchedPayments.get(0).getPaymentAmount(), 0.001F);
        assertEquals("Legacy Current Account", unmatchedPayments.get(0).getCreditedAccount());
        assertNull(unmatchedPayments.get(0).getMember());
    }

    @Test
    public void findByMemberIsNotNullAndIsLotteryPayment() throws Exception {
        List<Payment> matchedPayments = paymentRepository.findByMemberIsNotNullAndIsLotteryPayment(true);

        assertEquals(2, matchedPayments.size());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        assertEquals("23/11/2014", simpleDateFormat.format(matchedPayments.get(0).getPaymentDate()));
        assertEquals(20.00F, matchedPayments.get(0).getPaymentAmount(), 0.001F);
        assertEquals("Lifeline Current Account", matchedPayments.get(0).getCreditedAccount());
        assertNotNull(matchedPayments.get(0).getMember());
        assertEquals("Saunders", matchedPayments.get(0).getMember().getSurname());
    }

    @Test
    public void findMatchedLotteryPayments() throws Exception {
        List<PaymentViewBean> matchedPayments = paymentRepository.findMatchedLotteryPayments();

        assertEquals(2, matchedPayments.size());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        assertEquals("23/11/2014", simpleDateFormat.format(matchedPayments.get(0).getPaymentDate()));
        assertEquals(20.00F, matchedPayments.get(0).getPaymentAmount(), 0.001F);
        assertEquals("Lifeline Current Account", matchedPayments.get(0).getCreditedAccount());
        assertNotNull(matchedPayments.get(0).getMemberId());
        assertEquals("5566: Jim Saunders", matchedPayments.get(0).getMemberDisplayText());
    }

    @Test
    public void findUnmatchedLotteryPayments() {
        List<PaymentViewBean> unmatchedLotteryPayments = paymentRepository.findUnmatchedLotteryPayments();
        assertEquals(1, unmatchedLotteryPayments.size());
        assertEquals(0L, unmatchedLotteryPayments.get(0).getMemberId().longValue());
        assertEquals("", unmatchedLotteryPayments.get(0).getMemberDisplayText());
    }

    @Test
    public void getTotalLotteryPaymentSince() {
        DateTime dateTime = new DateTime(2014, 11, 23, 0, 0);
        Double totalPayment = paymentRepository.getTotalLotteryPaymentSince(dateTime.toDate(), member.getId());
        assertEquals(40.00D, totalPayment, 0.001D);
        totalPayment = paymentRepository.getTotalLotteryPaymentSince(new DateTime().toDate(), member.getId());
        assertEquals(0.00D, totalPayment, 0.001D);
    }

    @Test
    public void findByIsLotteryPayment() {
        assertEquals(1, paymentRepository.findByIsLotteryPayment(false).size());
        assertEquals(500F, paymentRepository.findByIsLotteryPayment(false).get(0).getPaymentAmount(), 0.002F);
        assertEquals("POTY SPONSORSHIP", paymentRepository.findByIsLotteryPayment(false).get(0).getCreditReference());
        assertEquals(3, paymentRepository.findByIsLotteryPayment(true).size());
    }

    @Test
    public void findNonLotteryPayments() {
        assertEquals(1, paymentRepository.findAllNonLotteryPayments().size());
        assertEquals(500F, paymentRepository.findAllNonLotteryPayments().get(0).getPaymentAmount(), 0.002F);
        assertEquals("POTY SPONSORSHIP", paymentRepository.findAllNonLotteryPayments().get(0).getCreditReference());
    }

}