package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.domain.PaymentReference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = PaymentReference.class)
public class PaymentReferenceRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private PaymentReferenceRepository paymentReferenceRepository;

    private Member member;
    private PaymentReference paymentReference1;
    private PaymentReference paymentReference2;

    @Before
    public void setUp() throws Exception {
        member = newMember(5566L, "Jim", "Saunders", "jimbo@email.com", "01383 226655", "0778 866 5544", "Monthly", "Lifeline", "New member", "Open");
        entityManager.persist(member);

        paymentReference1 = new PaymentReference("FPS CREDIT 1578", "H SMITH", false, member);
        paymentReference2 = new PaymentReference("FPS CREDIT 2233", "SMITH", true, member);
        entityManager.persist(paymentReference1);
        entityManager.persist(paymentReference2);
    }

    @After
    public void tearDown() throws Exception {
        entityManager.remove(paymentReference1);
        entityManager.remove(paymentReference1);
        entityManager.remove(member);
    }

    @Test
    public void findPaymentReferencesByMember() throws Exception {
        List<PaymentReference> paymentReferences = paymentReferenceRepository.findByMember(member);
        assertEquals(2, paymentReferences.size());
    }

    @Test
    public void findActivePaymentReferencesByMember() throws Exception {
        List<PaymentReference> paymentReferences = paymentReferenceRepository.findByMemberAndIsActive(member, true);
        assertEquals(1, paymentReferences.size());
        PaymentReference activeReference = paymentReferences.get(0);
        assertEquals("FPS CREDIT 2233", activeReference.getReference());
    }

}