package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.Payment;
import com.codaconsultancy.cclifeline.repositories.PaymentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@EntityScan("com.codaconsultancy.cclifeline.domain")
@SpringBootTest(classes = PaymentService.class)
public class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @MockBean
    private PaymentRepository paymentRepository;

    @Test
    public void findAllPayments() throws Exception {

        List<Payment> payments = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        Payment payment1 = new Payment(paymentDate, 20.00F, "FPS CREDIT 0101 THOMAS", "83776900435093BZ");
        Payment payment2 = new Payment(paymentDate, 240.00F, "FPS CREDIT 0155 HARRIS", "83776900435093BZ");
        Payment payment3 = new Payment(paymentDate, 20.00F, "FPS CREDIT 0111 MCDONNELL", "83776900435093BZ");
        payments.add(payment1);
        payments.add(payment2);
        payments.add(payment3);
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> foundPayments = paymentService.findAllPayments();

        assertEquals(3, foundPayments.size());
        assertEquals("FPS CREDIT 0101 THOMAS", foundPayments.get(0).getCreditReference());
    }

}