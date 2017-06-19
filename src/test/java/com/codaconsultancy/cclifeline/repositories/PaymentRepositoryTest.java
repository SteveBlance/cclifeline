package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.common.TestHelper;
import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.domain.Payment;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest(classes = Payment.class)
public class PaymentRepositoryTest extends BaseTest {

    @Autowired
    private PaymentRepository paymentRepository;

    private Member member;
    private Payment payment1;
    private Payment payment2;
    private Payment payment3;

    @Before
    public void setUp() throws Exception {
        member = TestHelper.newMember(5566L, "Jim", "Saunders", "jimbo@email.com", "01383 226655", "0778 866 5544", "Monthly", "Lifeline", "New member", "Open");
        entityManager.persist(member);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = simpleDateFormat.parse("23/11/2014");
        Date date2 = simpleDateFormat.parse("30/11/2014");
        payment1 = new Payment(date1, 20.00F, "FPS CREDIT 0299 SMITH", "Lifeline Current Account");
        payment1.setMember(member);
        payment2 = new Payment(date2, 8.66F, "FPS CREDIT 0222 LINDSAY", "Legacy Current Account");
        payment3 = new Payment(date2, 20.00F, "FPS CREDIT 0299 SMITH", "Lifeline Current Account");
        payment3.setMember(member);
        entityManager.persist(payment1);
        entityManager.persist(payment2);
        entityManager.persist(payment3);
    }

    @After
    public void tearDown() throws Exception {
        entityManager.remove(payment1);
        entityManager.remove(payment2);
        entityManager.remove(payment3);
        entityManager.remove(member);
    }

    @Test
    public void findAll() throws Exception {
        assertEquals(3, paymentRepository.findAll().size());
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
    public void findByMemberIsNull() throws Exception {
        List<Payment> unmatchedPayments = paymentRepository.findByMemberIsNull();

        assertEquals(1, unmatchedPayments.size());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        assertEquals("30/11/2014", simpleDateFormat.format(unmatchedPayments.get(0).getPaymentDate()));
        assertEquals(8.66F, unmatchedPayments.get(0).getPaymentAmount(), 0.001F);
        assertEquals("Legacy Current Account", unmatchedPayments.get(0).getCreditedAccount());
        assertNull(unmatchedPayments.get(0).getMember());
    }

    @Test
    public void getTotalPaymentSince() {
        DateTime dateTime = new DateTime(2014, 11, 23, 0, 0);
        Double totalPayment = paymentRepository.getTotalPaymentSince(dateTime.toDate(), member.getId());
        assertEquals(40.00D, totalPayment, 0.001D);
        totalPayment = paymentRepository.getTotalPaymentSince(new DateTime().toDate(), member.getId());
        assertEquals(0.00D, totalPayment, 0.001D);
    }

}