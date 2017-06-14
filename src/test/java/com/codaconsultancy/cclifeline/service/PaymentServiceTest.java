package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.Payment;
import com.codaconsultancy.cclifeline.domain.PaymentReference;
import com.codaconsultancy.cclifeline.repositories.MemberRepository;
import com.codaconsultancy.cclifeline.repositories.PaymentReferenceRepository;
import com.codaconsultancy.cclifeline.repositories.PaymentRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@EntityScan("com.codaconsultancy.cclifeline.domain")
@SpringBootTest(classes = PaymentService.class)
public class PaymentServiceTest {

    private static final String EXAMPLE_STATEMENT =
            "Transaction Date,Transaction Type,Sort Code,Account Number,Transaction Description,Debit Amount,Credit Amount,Balance\r\n" +
                    "30/05/2017,FPI,'82-62-19,00175999CA,MR JAMIE SMITH 1234 25142449225024000R ,,20,2283.61\n" +
                    "30/05/2017,FPI,'82-62-19,00175999CA,MARSHALL 1578 51194945265316000R            28MAY17 ,,20,1943.61\n" +
                    "28/04/2017,FPI,'82-62-19,00175999CA,WILSON 3344 26115814798763000R ,,20,2577.95\n" +
                    "27/04/2017,FPI,'82-62-19,00175999CA,MR S BLANCE 1244 2908082010027288CN ,,120,3683.52\n" +
                    "24/04/2017,FPI,'82-62-19,00175999CA,MR S PETRIE 1245 5230447564213244CN            23APR17 ,,20,2799.23\n" +
                    "06/04/2017,FPI,'82-62-19,00175999CA,BOB MARSHALL 1570 48222518364181000R ,,20,11345.37\n" +
                    "28/03/2017,FPI,'82-62-19,00175999CA,MIKE REID 1568 17202458533839000R ,,20,2964.67\n" +
                    "17/03/2017,FPI,'82-62-19,00175999CA,SANDY JAMIESON 1566 6131765340517141CN ,,20,6340.3\n" +
                    "17/03/2017,DBT,'82-62-19,00175999CA,SANDY JAMIESON 1566 6131765340517141CN ,2888,,6340.3\n" +
                    "17/03/2017,DBT,'82-62-19,00175999CA,SANDY JAMIESON 1566 6131765340517141CN ,2888,,6340.3\n" +
                    "17/03/2017,FPI,'82-62-19,00175999CA,DAVE SPENCE 6769810200517177CN ,,120,3840.3\n";
    private static final String BAD_STATEMENT =
            "Transaction Date,Transaction Type,Sort Code,Account Number,Transaction Description,Debit Amount,Credit Amount,Balance\r\n" +
                    "30/05/2017,FPI,'82-62-19,00175999CA,MR JAMIE SMITH 1234 25142449225024000R ,,BAD,2283.61\n";

    @Autowired
    private PaymentService paymentService;

    @MockBean
    private PaymentRepository paymentRepository;

    @MockBean
    private PaymentReferenceRepository paymentReferenceRepository;

    @MockBean
    private MemberRepository memberRepository;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

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

    @Test
    public void savePayment() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        Payment payment = new Payment(paymentDate, 20.09F, "FPS CREDIT 0101 THOMAS", "83776900435093BZ");
        when(paymentRepository.save(payment)).thenReturn(payment);

        Payment savedPayment = paymentService.savePayment(payment);

        verify(paymentRepository, times(1)).save(payment);

        assertSame(payment, savedPayment);
    }

    @Test
    public void savePaymentReference() {
        PaymentReference paymentReference = new PaymentReference("FPS CREDIT 1234 H PETRIE", "Hamish Petrie", true, null);
        when(paymentReferenceRepository.save(paymentReference)).thenReturn(paymentReference);

        PaymentReference saveReference = paymentService.savePaymentReference(paymentReference);

        verify(paymentReferenceRepository, times(1)).save(paymentReference);
        assertSame(saveReference, paymentReference);
    }

    @Test
    public void parsePayments_success() throws Exception {
        List<Payment> payments = paymentService.parsePayments(EXAMPLE_STATEMENT);
        assertEquals(9, payments.size());
    }

    @Test
    public void parsePayments_badCreditAmount() throws Exception {
        expectedException.expect(NumberFormatException.class);
        List<Payment> payments = paymentService.parsePayments(BAD_STATEMENT);
        assertEquals(0, payments.size());
    }

}