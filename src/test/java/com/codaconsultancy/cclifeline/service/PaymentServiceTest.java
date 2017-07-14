package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.common.TestHelper;
import com.codaconsultancy.cclifeline.domain.Member;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@EntityScan("com.codaconsultancy.cclifeline.domain")
@SpringBootTest(classes = PaymentService.class)
public class PaymentServiceTest {

    private static final String EXAMPLE_STATEMENT =
            "20170428,,82621900174982CA,,DR,CHQ,Cheque,200,CHEQUE 003033,,,GBP\n" +
                    "20170428,,82621900174982CA,,CR,BGC,Bank Giro Credit,20,BANK GIRO CREDIT 3830,MRS MARGARET ANDERSON R,8.26219E+13,GBP\n" +
                    "20170428,,82621900174982CA,,CR,BGC,Bank Giro Credit,20,BANK GIRO CREDIT 3791,MR ANDREW LENNIE,8.26518E+13,GBP\n" +
                    "20170428,,82621900174982CA,,CR,BGC,Bank Giro Credit,20,FPS CREDIT POTY  SYME MURDOCH,I Syme,8.77054E+13,GBP\n" +
                    "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,20,BANK GIRO CREDIT MEM NO 3959,PETER KELVIN SMITH,8.26725E+13,GBP\n" +
                    "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,20,BANK GIRO CREDIT 3176,HENRY FULTON NISBE,8.26726E+13,GBP\n" +
                    "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,20,BANK GIRO CREDIT 0093,R F MCKINLAY,8.26725E+13,GBP\n" +
                    "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,20,BANK GIRO CREDIT 4175,GORDON JOHN SCOTLA,8.26726E+13,GBP\n" +
                    "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,20,BANK GIRO CREDIT PARS CENTENARY CLB,DIARMID BLYTH,8.26825E+13,GBP\n" +
                    "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,20,BANK GIRO CREDIT ID 4577,MR DIARMID MCDIARM,8.26825E+13,GBP\n" +
                    "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,20,BANK GIRO CREDIT 2406,CAMPBELL J,8.26831E+13,GBP\n" +
                    "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,20,BANK GIRO CREDIT MEMBERSID 3884,MR C MARTIN,8.26915E+13,GBP\n" +
                    "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,20,BANK GIRO CREDIT 2013,MR & MRS DUFFY,8.27019E+13,GBP\n" +
                    "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,20,BANK GIRO CREDIT 2010,MR & MRS DUFFY,8.27019E+13,GBP\n" +
                    "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,20,FPS CREDIT 4061,MR MATTHEW LAFFERT,4.0642E+13,GBP\n" +
                    "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,20,FPS CREDIT ID - 2592,MR FRASER DRYBURGH,4.0642E+13,GBP\n" +
                    "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,20,FPS CREDIT 4367,HOGG W & J/CA,8.333E+13,GBP\n" +
                    "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,20,FPS CREDIT 2823 STEPHENGORDON,GORDON STEPHEN/ROY,8.333E+13,GBP\n" +
                    "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,20,FPS CREDIT 4319,J TIMMONS ESQ,8.00885E+13,GBP\n" +
                    "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,20,FPS CREDIT ID4340,C ADDISON,8.04567E+13,GBP\n" +
                    "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,8.66,FPS CREDIT 00192,JOHN SYME,9.01287E+12,GBP\n" +
                    "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,8.66,FPS CREDIT 600,DAVID PHILLIPS,9.01284E+12,GBP\n" +
                    "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,8.66,FPS CREDIT 0379,BRUCE DRUMMOND,9.01278E+12,GBP\n" +
                    "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,2,FPS CREDIT 338,HALLYBURTON ESQ,8.00676E+13,GBP\n" +
                    "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,800,CREDIT 000988,,,GBP";

    private static final String BAD_STATEMENT__INVALID_AMOUNT =
            "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,BAD,FPS CREDIT 4061,MR MATTHEW LAFFERT,4.0642E+13,GBP\n";

    private static final String MATCH__3_DIGIT_MEMBERSHIP_NUMBER =
            "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,20,BANK GIRO CREDIT 379,R F MCKINLAY,8.26725E+13,GBP\n";

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date paymentDate = sdf.parse("2017/01/03 ");
        Payment payment1 = new Payment(paymentDate, 20.00F, "FPS CREDIT 0101 THOMAS", "83776900435093BZ", "BOB SMITH");
        Payment payment2 = new Payment(paymentDate, 240.00F, "FPS CREDIT 0155 HARRIS", "83776900435093BZ", "BOB SMITH");
        Payment payment3 = new Payment(paymentDate, 20.00F, "FPS CREDIT 0111 MCDONNELL", "83776900435093BZ", "BOB SMITH");
        payments.add(payment1);
        payments.add(payment2);
        payments.add(payment3);
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> foundPayments = paymentService.findAllPayments();

        assertEquals(3, foundPayments.size());
        assertEquals("FPS CREDIT 0101 THOMAS", foundPayments.get(0).getCreditReference());
    }

    @Test
    public void findAllUnmatchedPayments() throws Exception {
        List<Payment> unmatchedPayments = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        Payment payment1 = new Payment(paymentDate, 20.00F, "FPS CREDIT 0101 THOMAS", "83776900435093BZ", "BOB SMITH");
        Payment payment2 = new Payment(paymentDate, 240.00F, "FPS CREDIT 0155 HARRIS", "83776900435093BZ", "BOB SMITH");
        Payment payment3 = new Payment(paymentDate, 20.00F, "FPS CREDIT 0111 MCDONNELL", "83776900435093BZ", "BOB SMITH");
        unmatchedPayments.add(payment1);
        unmatchedPayments.add(payment2);
        unmatchedPayments.add(payment3);
        when(paymentRepository.findByMemberIsNull()).thenReturn(unmatchedPayments);

        List<Payment> foundPayments = paymentService.findAllUnmatchedPayments();

        assertEquals(3, foundPayments.size());
        assertEquals("FPS CREDIT 0101 THOMAS", foundPayments.get(0).getCreditReference());
    }

    @Test
    public void findAllMatchedPayments() throws Exception {
        List<Payment> matchedPayments = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        Member member = new Member();
        member.setMembershipNumber(1234L);
        Payment payment1 = new Payment(paymentDate, 20.00F, "FPS CREDIT 0101 THOMAS", "83776900435093BZ", "BOB SMITH");
        payment1.setMember(member);
        Payment payment2 = new Payment(paymentDate, 240.00F, "FPS CREDIT 0155 HARRIS", "83776900435093BZ", "BOB SMITH");
        payment2.setMember(member);
        matchedPayments.add(payment1);
        matchedPayments.add(payment2);
        when(paymentRepository.findByMemberIsNotNull()).thenReturn(matchedPayments);

        List<Payment> foundPayments = paymentService.findAllMatchedPayments();

        assertEquals(2, foundPayments.size());
        assertEquals("FPS CREDIT 0101 THOMAS", foundPayments.get(0).getCreditReference());
        assertEquals(1234L, foundPayments.get(0).getMember().getMembershipNumber().longValue());
    }

    @Test
    public void savePayment() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        Payment payment = new Payment(paymentDate, 20.09F, "FPS CREDIT 0101 THOMAS", "83776900435093BZ", "BOB SMITH");
        when(paymentRepository.save(payment)).thenReturn(payment);

        Payment savedPayment = paymentService.savePayment(payment);

        verify(paymentRepository, times(1)).save(payment);

        assertSame(payment, savedPayment);
    }

    @Test
    public void savePayments() {
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment());
        payments.add(new Payment());
        when(paymentRepository.save(payments)).thenReturn(payments);

        List<Payment> savedPayments = paymentService.savePayments(payments);

        verify(paymentRepository, times(1)).save(payments);
        assertSame(payments, savedPayments);
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
        List<Member> members = new ArrayList<>();
        Member member1 = TestHelper.newMember(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        Member member2 = TestHelper.newMember(3830L, "Margaret", "Anderson", "ma@email.com", "0131999877", null, "Monthly", "Lifeline", null, "Open");
        members.add(member1);
        members.add(member2);
        when(memberRepository.findAllByStatusOrderBySurnameAscForenameAsc("Open")).thenReturn(members);
        List<Payment> payments = paymentService.parsePayments(EXAMPLE_STATEMENT, "test.csv");
        assertEquals(24, payments.size());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        assertEquals("20170428", sdf.format(payments.get(0).getPaymentDate()));
        assertEquals("82621900174982CA", payments.get(0).getCreditedAccount());
        assertEquals(20.00F, payments.get(0).getPaymentAmount(), 0.002);
        assertEquals("BANK GIRO CREDIT 3830", payments.get(0).getCreditReference());
        assertEquals("MRS MARGARET ANDERSON R", payments.get(0).getName());
        assertEquals(member2.getId(), payments.get(0).getMember().getId());
    }

    @Test
    public void parsePayments_successWIth3DigitMembershipNumber() throws Exception {
        List<Member> members = new ArrayList<>();
        Member member1 = TestHelper.newMember(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        Member member2 = TestHelper.newMember(379L, "Ross", "McKinlay", "ma@email.com", "0131999877", null, "Monthly", "Lifeline", null, "Open");
        members.add(member1);
        members.add(member2);
        when(memberRepository.findAllByStatusOrderBySurnameAscForenameAsc("Open")).thenReturn(members);
        List<Payment> payments = paymentService.parsePayments(MATCH__3_DIGIT_MEMBERSHIP_NUMBER, "test.csv");
        assertEquals(1, payments.size());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        assertEquals("20170403", sdf.format(payments.get(0).getPaymentDate()));
        assertEquals("82621900174982CA", payments.get(0).getCreditedAccount());
        assertEquals(20.00F, payments.get(0).getPaymentAmount(), 0.002);
        assertEquals("BANK GIRO CREDIT 379", payments.get(0).getCreditReference());
        assertEquals("R F MCKINLAY", payments.get(0).getName());
        assertEquals(member2.getId(), payments.get(0).getMember().getId());
    }

    @Test
    public void parsePayments_noMatchingName() throws Exception {
        List<Member> members = new ArrayList<>();
        Member member1 = TestHelper.newMember(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        Member member2 = TestHelper.newMember(3830L, "Ann", "Smith", "ma@email.com", "0131999877", null, "Monthly", "Lifeline", null, "Open");
        members.add(member1);
        members.add(member2);
        when(memberRepository.findAllByStatusOrderBySurnameAscForenameAsc("Open")).thenReturn(members);
        List<Payment> payments = paymentService.parsePayments(EXAMPLE_STATEMENT, "test.csv");
        assertEquals(24, payments.size());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        assertEquals("20170428", sdf.format(payments.get(0).getPaymentDate()));
        assertEquals("82621900174982CA", payments.get(0).getCreditedAccount());
        assertEquals(20.00F, payments.get(0).getPaymentAmount(), 0.002);
        assertEquals("BANK GIRO CREDIT 3830", payments.get(0).getCreditReference());
        assertEquals("MRS MARGARET ANDERSON R", payments.get(0).getName());
        assertNull(payments.get(0).getMember());
    }

    @Test
    public void parsePayments_badCreditAmount() throws Exception {
        expectedException.expect(NumberFormatException.class);
        List<Payment> payments = paymentService.parsePayments(BAD_STATEMENT__INVALID_AMOUNT, "test.csv");
        assertEquals(0, payments.size());
    }

    @Test
    public void findById() {
        Payment payment = new Payment();
        payment.setId(2865L);
        when(paymentRepository.findOne(payment.getId())).thenReturn(payment);

        Payment foundPayment = paymentService.findById(payment.getId());

        verify(paymentRepository, times(1)).findOne(payment.getId());
        assertEquals(payment.getId(), foundPayment.getId());
    }

    @Test
    public void findPaymentsForMember() {
        Member member = new Member();
        member.setId(34L);
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment());
        payments.add(new Payment());
        payments.add(new Payment());
        when(paymentRepository.findByMember(member)).thenReturn(payments);

        List<Payment> foundPayments = paymentService.findPaymentsForMember(member);

        verify(paymentRepository, times(1)).findByMember(member);
        assertEquals(3, foundPayments.size());
        assertSame(payments, foundPayments);
    }

    @Test
    public void findLatestPaymentForMember() {
        Payment payment = new Payment();
        payment.setId(2865L);
        Member member = new Member();
        member.setId(34L);
        when(paymentRepository.findTopByMemberOrderByPaymentDateDesc(member)).thenReturn(payment);

        Payment foundPayment = paymentService.findLatestPayment(member);

        verify(paymentRepository, times(1)).findTopByMemberOrderByPaymentDateDesc(member);
        assertEquals(2865L, foundPayment.getId().longValue());
    }

    @Test
    public void findPossiblePayersForPayment() {
        Payment payment = new Payment();
        payment.setCreditReference("FPS CREDIT FOR MEMBID 4445");
        payment.setName("MR DAVE WILSON");
        Member member4445 = new Member();
        member4445.setMembershipNumber(4445L);
        when(memberRepository.findByMembershipNumber(4445L)).thenReturn(member4445);
        List<Member> daveMembers = new ArrayList<>();
        daveMembers.add(new Member());
        daveMembers.add(new Member());
        daveMembers.add(new Member());
        List<Member> wilsonMembers = new ArrayList<>();
        wilsonMembers.add(new Member());
        wilsonMembers.add(new Member());
        when(memberRepository.findAllBySurnameIgnoreCaseAndStatusOrderByForename("DAVE", "Open")).thenReturn(daveMembers);
        when(memberRepository.findAllBySurnameIgnoreCaseAndStatusOrderByForename("WILSON", "Open")).thenReturn(wilsonMembers);

        List<Member> possiblePayers = paymentService.findPossiblePayers(payment);

        verify(memberRepository, times(1)).findByMembershipNumber(4445L);
        verify(memberRepository, times(1)).findAllBySurnameIgnoreCaseAndStatusOrderByForename("DAVE", "Open");
        verify(memberRepository, times(1)).findAllBySurnameIgnoreCaseAndStatusOrderByForename("WILSON", "Open");
        assertEquals(6, possiblePayers.size());
        assertEquals(4445L, possiblePayers.get(0).getMembershipNumber().longValue());
    }


}