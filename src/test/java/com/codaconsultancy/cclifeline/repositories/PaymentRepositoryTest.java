package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.common.TestHelper;
import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.domain.Payment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = Payment.class)
public class PaymentRepositoryTest extends BaseTest {

    @Autowired
    private PaymentRepository paymentRepository;

    private Member member;
    private Payment payment1;
    private Payment payment2;

    @Before
    public void setUp() throws Exception {
        member = TestHelper.newMember(5566L, "Jim", "Saunders", "jimbo@email.com", "01383 226655", "0778 866 5544", "Monthly", "Lifeline", "New member", "Open");
        entityManager.persist(member);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = simpleDateFormat.parse("23/11/2014");
        Date date2 = simpleDateFormat.parse("30/11/2014");
        payment1 = new Payment(date1, 20.00F, "Lifeline Current Account");
        payment1.setMember(member);
        payment2 = new Payment(date2, 8.66F, "Legacy Current Account");
        entityManager.persist(payment1);
        entityManager.persist(payment2);
    }

    @After
    public void tearDown() throws Exception {
        entityManager.remove(payment1);
        entityManager.remove(payment2);
        entityManager.remove(member);
    }

    @Test
    public void findAll() throws Exception {
        assertEquals(2, paymentRepository.findAll().size());
    }

    @Test
    public void findByMember() throws Exception {
        List<Payment> paymentsByMember = paymentRepository.findByMember(member);

        assertEquals(1, paymentsByMember.size());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        assertEquals("23/11/2014", simpleDateFormat.format(paymentsByMember.get(0).getPaymentDate()));
        assertEquals(20.00F, paymentsByMember.get(0).getPaymentAmount(), 0.001F);
        assertEquals("Lifeline Current Account", paymentsByMember.get(0).getCreditedAccount());

    }

}