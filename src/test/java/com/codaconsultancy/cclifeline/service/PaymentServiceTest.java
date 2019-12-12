package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.common.TestHelper;
import com.codaconsultancy.cclifeline.domain.Configuration;
import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.domain.Payment;
import com.codaconsultancy.cclifeline.domain.PaymentReference;
import com.codaconsultancy.cclifeline.repositories.MemberRepository;
import com.codaconsultancy.cclifeline.repositories.PaymentReferenceRepository;
import com.codaconsultancy.cclifeline.repositories.PaymentRepository;
import com.codaconsultancy.cclifeline.view.PaymentViewBean;
import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.codaconsultancy.cclifeline.service.LifelineService.ELIGIBILITY_REFRESH_REQUIRED;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaymentService.class)
public class PaymentServiceTest extends LifelineServiceTest {

    private static final String EXAMPLE_STATEMENT =
            "20191101, , 82621900174982CA, , CR, , , 20.00,\"Transfer FPS, Teed G T4E F, 4550\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 2,\"Giro 025\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 2,\"Giro 0090\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 8.66,\"Transfer FPS, Combe G F, 988\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 8.66,\"Transfer FPS, J Harris, 579\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 8.66,\"Transfer FPS, Huggs Sa&Fj B A/C, 617\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 8.66,\"Transfer FPS, Keen I B/J Ba  Sg, 213\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 8.66,\"Transfer FPS, David Jack, 600\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 8.66,\"Transfer FPS, John Voit, 00192\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 8.66,\"Transfer FPS, Karlie Yvonne/I, 0229\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 8.66,\"Transfer FPS, Hunt B & L M, 0018\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 8.66,\"Transfer FPS, Peters Kidd G/Ro, 0111 PETERS\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 8.66,\"Transfer FPS, Grey M/Mrs D, 282\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 8.66,\"Transfer FPS, Alyn Lowe, 585\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 8.66,\"Transfer FPS, Thom Knight\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 8.66,\"Transfer FPS, Miss Kaye P Crisp, 0066\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 8.66,\"Transfer FPS, Bisk B & Mrs S, 711\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 8.66,\"Transfer FPS, D Dee, 0103 DEE\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 8.66,\"Transfer FPS, A & B Bev, 0110 BARR\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 8.66,\"Transfer FPS, Shaws, 88\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 8.66,\"Transfer FPS, Will, 530\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 8.66,\"Transfer FPS, Kitchin F Fug F, 1177\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 8.66,\"Giro 0263\", , , GBP\n" +
                    "20191101, , 82621900174982CA, , CR, , , 8.66,\"Giro 0567\", , , GBP";

    private static final String BAD_STATEMENT__INVALID_AMOUNT =
            "20170403,,82621900174982CA,,CR,BGC,Bank Giro Credit,BAD,FPS CREDIT 4061,MR MATTHEW LAFFERT,4.0642E+13,GBP\n";

    private static final String MATCH__3_DIGIT_MEMBERSHIP_NUMBER =
            "20191101, , 82621900174982CA, , CR, , , 8.66,\"Transfer FPS, R F MCKINLAY, 379\", , , GBP\n";

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
        List<PaymentViewBean> payments = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date paymentDate = sdf.parse("2017/01/03 ");
        PaymentViewBean payment1 = new PaymentViewBean(paymentDate, 20.00F, "FPS CREDIT 0101 THOMAS", "83776900435093BZ", "BOB SMITH", true);
        PaymentViewBean payment2 = new PaymentViewBean(paymentDate, 240.00F, "FPS CREDIT 0155 HARRIS", "83776900435093BZ", "BOB SMITH", true);
        PaymentViewBean payment3 = new PaymentViewBean(paymentDate, 20.00F, "FPS CREDIT 0111 MCDONNELL", "83776900435093BZ", "BOB SMITH", true);
        payments.add(payment1);
        payments.add(payment2);
        payments.add(payment3);
        when(paymentRepository.findAllPayments()).thenReturn(payments);

        List<PaymentViewBean> foundPayments = paymentService.findAllPayments();

        assertEquals(3, foundPayments.size());
        assertEquals("FPS CREDIT 0101 THOMAS", foundPayments.get(0).getCreditReference());
    }

    @Test
    public void findPaymentsForLastMonth() {
        List<PaymentViewBean> payments = new ArrayList<>();
        when(paymentRepository.findAllPaymentsAfter(any(Date.class))).thenReturn(payments);

        List<PaymentViewBean> recentPayments = paymentService.findPaymentsForLastMonth();

        ArgumentCaptor<Date> dateArgumentCaptor = ArgumentCaptor.forClass(Date.class);
        verify(paymentRepository, times(1)).findAllPaymentsAfter(dateArgumentCaptor.capture());
        assertSame(payments, recentPayments);
        Date now = DateTime.now().toDate();
        Date threeWeeksAgo = DateTime.now().minusWeeks(3).toDate();
        Date fiveWeeksAgo = DateTime.now().minusWeeks(5).toDate();
        Date dateFrom = dateArgumentCaptor.getValue();
        assertTrue(now.after(dateFrom));
        assertTrue(threeWeeksAgo.after(dateFrom));
        assertFalse(fiveWeeksAgo.after(dateFrom));
    }

    @Test
    public void findAllUnmatchedPayments() throws Exception {
        List<PaymentViewBean> unmatchedPayments = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        PaymentViewBean payment1 = new PaymentViewBean(paymentDate, 20.00F, "FPS CREDIT 0101 THOMAS", "83776900435093BZ", "BOB SMITH", true);
        PaymentViewBean payment2 = new PaymentViewBean(paymentDate, 240.00F, "FPS CREDIT 0155 HARRIS", "83776900435093BZ", "BOB SMITH", true);
        PaymentViewBean payment3 = new PaymentViewBean(paymentDate, 20.00F, "FPS CREDIT 0111 MCDONNELL", "83776900435093BZ", "BOB SMITH", true);
        unmatchedPayments.add(payment1);
        unmatchedPayments.add(payment2);
        unmatchedPayments.add(payment3);
        when(paymentRepository.findUnmatchedLotteryPayments()).thenReturn(unmatchedPayments);

        List<PaymentViewBean> foundPayments = paymentService.findAllUnmatchedPayments();

        assertEquals(3, foundPayments.size());
        assertEquals("FPS CREDIT 0101 THOMAS", foundPayments.get(0).getCreditReference());
    }

    @Test
    public void findAllMatchedLotteryPayments() throws Exception {
        List<PaymentViewBean> matchedPayments = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        PaymentViewBean payment1 = new PaymentViewBean(75L, paymentDate, 20.00F, "FPS CREDIT 0101 THOMAS", "83776900435093BZ", "BOB SMITH", true, 999L, "0101: Bob Smith");
        PaymentViewBean payment2 = new PaymentViewBean(76L, paymentDate, 240.00F, "FPS CREDIT 0155 HARRIS", "83776900435093BZ", "BOB SMITH", true, 999L, "0101: Bob Harris");
        matchedPayments.add(payment1);
        matchedPayments.add(payment2);
        when(paymentRepository.findMatchedLotteryPayments()).thenReturn(matchedPayments);

        List<PaymentViewBean> foundPayments = paymentService.findAllMatchedLotteryPayments();

        verify(paymentRepository, times(1)).findMatchedLotteryPayments();
        assertEquals(2, foundPayments.size());
        assertEquals(75L, foundPayments.get(0).getId().longValue());
        assertEquals("FPS CREDIT 0101 THOMAS", foundPayments.get(0).getCreditReference());
        assertEquals(999L, foundPayments.get(0).getMemberId().longValue());
        assertEquals("0101: Bob Smith", foundPayments.get(0).getMemberDisplayText());
        assertEquals(76L, foundPayments.get(1).getId().longValue());
    }

    @Test
    public void findAllNonLotteryPayments() throws Exception {
        List<PaymentViewBean> nonLotteryPayments = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        PaymentViewBean payment1 = new PaymentViewBean(paymentDate, 100.00F, "POTY SPONSOR", "83776900435093BZ", "BOB SMITH", true);
        PaymentViewBean payment2 = new PaymentViewBean(paymentDate, 500.00F, "POTY TABLE BOOKING", "83776900435093BZ", "ALEX REID", true);
        nonLotteryPayments.add(payment1);
        nonLotteryPayments.add(payment2);
        when(paymentRepository.findAllNonLotteryPayments()).thenReturn(nonLotteryPayments);

        List<PaymentViewBean> foundPayments = paymentService.findAllNonLotteryPayments();

        assertEquals(2, foundPayments.size());
        assertEquals("POTY SPONSOR", foundPayments.get(0).getCreditReference());
        assertEquals("POTY TABLE BOOKING", foundPayments.get(1).getCreditReference());
    }

    @Test
    public void savePayment_unallocated() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        Payment payment = new Payment(paymentDate, 20.09F, "FPS CREDIT 0101 THOMAS", "83776900435093BZ", "BOB SMITH", true);
        when(paymentRepository.save(payment)).thenReturn(payment);
        Configuration refreshRequired = new Configuration();
        refreshRequired.setBooleanValue(false);
        when(configurationRepository.findByName(ELIGIBILITY_REFRESH_REQUIRED)).thenReturn(refreshRequired);

        Payment savedPayment = paymentService.savePayment(payment);

        verify(paymentRepository, times(1)).save(payment);
        verify(memberRepository, never()).save(any(Member.class));


        assertSame(payment, savedPayment);
    }

    @Test
    public void savePayment_currentMember() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        Payment payment = new Payment(paymentDate, 20.09F, "FPS CREDIT 0101 THOMAS", "83776900435093BZ", "BOB SMITH", true);
        Member member = new Member();
        payment.setMember(member);
        member.setStatus("Open");
        when(paymentRepository.save(payment)).thenReturn(payment);
        Configuration refreshRequired = new Configuration();
        refreshRequired.setBooleanValue(false);
        when(configurationRepository.findByName(ELIGIBILITY_REFRESH_REQUIRED)).thenReturn(refreshRequired);

        Payment savedPayment = paymentService.savePayment(payment);

        verify(paymentRepository, times(1)).save(payment);
        verify(memberRepository, never()).save(member);
        assertEquals("Open", member.getStatus());

        assertSame(payment, savedPayment);
    }

    @Test
    public void savePayment_tbcMember() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        Payment payment = new Payment(paymentDate, 20.09F, "FPS CREDIT 0101 THOMAS", "83776900435093BZ", "BOB SMITH", true);
        Member member = new Member();
        payment.setMember(member);
        member.setStatus("TBC");
        when(paymentRepository.save(payment)).thenReturn(payment);
        Configuration refreshRequired = new Configuration();
        refreshRequired.setBooleanValue(false);
        when(configurationRepository.findByName(ELIGIBILITY_REFRESH_REQUIRED)).thenReturn(refreshRequired);

        Payment savedPayment = paymentService.savePayment(payment);

        verify(paymentRepository, times(1)).save(payment);
        verify(memberRepository, times(1)).save(member);
        assertEquals("Open", member.getStatus());

        assertSame(payment, savedPayment);
    }

    @Test
    public void updatePayment_currentMember() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        Payment payment = new Payment(paymentDate, 20.09F, "FPS CREDIT 0101 THOMAS", "83776900435093BZ", "BOB SMITH", true);
        Member member = new Member();
        payment.setMember(member);
        member.setStatus("Open");
        when(paymentRepository.save(payment)).thenReturn(payment);
        Configuration refreshRequired = new Configuration();
        refreshRequired.setBooleanValue(false);
        when(configurationRepository.findByName(ELIGIBILITY_REFRESH_REQUIRED)).thenReturn(refreshRequired);

        Payment savedPayment = paymentService.updatePayment(payment);

        verify(paymentRepository, times(1)).save(payment);
        verify(memberRepository, never()).save(member);
        ArgumentCaptor<Configuration> configurationArgumentCaptor = ArgumentCaptor.forClass(Configuration.class);
        verify(configurationRepository, times(1)).findByName(ELIGIBILITY_REFRESH_REQUIRED);
        verify(configurationRepository, times(1)).save(configurationArgumentCaptor.capture());
        assertEquals("Open", member.getStatus());

        assertSame(payment, savedPayment);
    }

    @Test
    public void updatePayment_tbcMember() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        Payment payment = new Payment(paymentDate, 20.09F, "FPS CREDIT 0101 THOMAS", "83776900435093BZ", "BOB SMITH", true);
        Member member = new Member();
        payment.setMember(member);
        member.setStatus("TBC");
        when(paymentRepository.save(payment)).thenReturn(payment);
        Configuration refreshRequired = new Configuration();
        refreshRequired.setBooleanValue(false);
        when(configurationRepository.findByName(ELIGIBILITY_REFRESH_REQUIRED)).thenReturn(refreshRequired);

        Payment savedPayment = paymentService.updatePayment(payment);

        verify(paymentRepository, times(1)).save(payment);
        verify(memberRepository, times(1)).save(member);
        assertEquals("Open", member.getStatus());

        assertSame(payment, savedPayment);
    }

    @Test
    public void savePayments() {
        List<Payment> payments = new ArrayList<>();
        Payment paymentWithCurrentMember = new Payment();
        Member currentMember = new Member();
        currentMember.setId(999L);
        currentMember.setStatus("Open");
        paymentWithCurrentMember.setMember(currentMember);
        payments.add(paymentWithCurrentMember);

        Payment paymentWithLapsedMember = new Payment();
        Member lapsedMember = new Member();
        lapsedMember.setId(111L);
        lapsedMember.setStatus("Closed");
        paymentWithLapsedMember.setMember(lapsedMember);
        payments.add(paymentWithLapsedMember);

        when(paymentRepository.save(payments)).thenReturn(payments);
        Configuration refreshRequired = new Configuration();
        refreshRequired.setBooleanValue(false);
        when(configurationRepository.findByName(ELIGIBILITY_REFRESH_REQUIRED)).thenReturn(refreshRequired);

        List<Payment> savedPayments = paymentService.savePayments(payments);

        verify(paymentRepository, times(1)).save(payments);
        verify(memberRepository, times(1)).save(lapsedMember);
        ArgumentCaptor<Configuration> configurationArgumentCaptor = ArgumentCaptor.forClass(Configuration.class);
        verify(configurationRepository, times(1)).findByName(ELIGIBILITY_REFRESH_REQUIRED);
        verify(configurationRepository, times(1)).save(configurationArgumentCaptor.capture());
        assertSame(payments, savedPayments);
        assertEquals("Open", lapsedMember.getStatus());
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
        Member member2 = TestHelper.newMember(550L, "Margaret", "Teed", "ma@email.com", "0131999877", null, "Monthly", "Lifeline", null, "TBC");
        members.add(member1);
        members.add(member2);
        when(memberRepository.findAll()).thenReturn(members);

        List<Payment> payments = paymentService.parsePayments(EXAMPLE_STATEMENT, "test.csv");

        assertEquals(24, payments.size());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        assertEquals("20191101", sdf.format(payments.get(0).getPaymentDate()));
        assertEquals("82621900174982CA", payments.get(0).getCreditedAccount());
        assertEquals(20.00F, payments.get(0).getPaymentAmount(), 0.002);
        assertEquals("Transfer FPS, Teed G T4E F, 4550", payments.get(0).getCreditReference());
        assertEquals("Teed G T4E F", payments.get(0).getName());
        assertEquals(member2.getMembershipNumber(), payments.get(0).getMember().getMembershipNumber());
    }

    @Test
    public void parsePayments_successWith3DigitMembershipNumber() throws Exception {
        List<Member> members = new ArrayList<>();
        Member member1 = TestHelper.newMember(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        Member member2 = TestHelper.newMember(379L, "Ross", "McKinlay", "ma@email.com", "0131999877", null, "Monthly", "Lifeline", null, "Open");
        members.add(member1);
        members.add(member2);
        when(memberRepository.findAll()).thenReturn(members);

        List<Payment> payments = paymentService.parsePayments(MATCH__3_DIGIT_MEMBERSHIP_NUMBER, "test.csv");

        assertEquals(1, payments.size());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        assertEquals("20191101", sdf.format(payments.get(0).getPaymentDate()));
        assertEquals("82621900174982CA", payments.get(0).getCreditedAccount());
        assertEquals(8.66F, payments.get(0).getPaymentAmount(), 0.002);
        assertEquals("Transfer FPS, R F MCKINLAY, 379", payments.get(0).getCreditReference());
        assertEquals("R F MCKINLAY", payments.get(0).getName());
        assertEquals(member2.getMembershipNumber(), payments.get(0).getMember().getMembershipNumber());
    }

    @Test
    public void parsePayments_successWithMatchingReference() throws Exception {
        List<Member> members = new ArrayList<>();
        Member member1 = TestHelper.newMember(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        Member member2 = TestHelper.newMember(9999L, "Ross", "McKinlay", "ma@email.com", "0131999877", null, "Monthly", "Lifeline", null, "Open");
        members.add(member1);
        PaymentReference paymentReference = new PaymentReference("Transfer FPS, R F MCKINLAY, 379", "R F MCKINLAY", true, null);
        List<PaymentReference> references = new ArrayList<>();
        references.add(paymentReference);
        paymentReference.setMember(member2);
        member2.setPaymentReferences(references);
        members.add(member2);
        when(memberRepository.findAll()).thenReturn(members);
        when(paymentReferenceRepository.findAll()).thenReturn(references);

        List<Payment> payments = paymentService.parsePayments(MATCH__3_DIGIT_MEMBERSHIP_NUMBER, "test.csv");

        assertEquals(1, payments.size());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        assertEquals("20191101", sdf.format(payments.get(0).getPaymentDate()));
        assertEquals("82621900174982CA", payments.get(0).getCreditedAccount());
        assertEquals(8.66F, payments.get(0).getPaymentAmount(), 0.002);
        assertEquals("Transfer FPS, R F MCKINLAY, 379", payments.get(0).getCreditReference());
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

        assertEquals("20191101", sdf.format(payments.get(0).getPaymentDate()));
        assertEquals("82621900174982CA", payments.get(0).getCreditedAccount());
        assertEquals(20.00F, payments.get(0).getPaymentAmount(), 0.002);
        assertEquals("Transfer FPS, Teed G T4E F, 4550", payments.get(0).getCreditReference());
        assertEquals("Teed G T4E F", payments.get(0).getName());
        assertNull(payments.get(0).getMember());
    }

    @Test
    public void parsePayments_nonCSVfile() throws Exception {
        List<Payment> payments = paymentService.parsePayments(EXAMPLE_STATEMENT, "test.XYZ");
        verify(memberRepository, never()).findAll();
        verify(memberRepository, never()).findAllByStatusOrderBySurnameAscForenameAsc("Open");
        assertEquals(0, payments.size());
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
    public void findLatestLotteryPaymentForMember() {
        Payment payment = new Payment();
        payment.setId(2865L);
        Member member = new Member();
        member.setId(34L);
        when(paymentRepository.findTopByMemberAndIsLotteryPaymentOrderByPaymentDateDesc(member, true)).thenReturn(payment);

        Payment foundPayment = paymentService.findLatestLotteryPayment(member);

        verify(paymentRepository, times(1)).findTopByMemberAndIsLotteryPaymentOrderByPaymentDateDesc(member, true);
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

    @Test
    public void deletePayment() {
        paymentService.deletePayment(69L);
        verify(paymentRepository, times(1)).delete(69L);
    }

    @Test
    public void markPaymentAsNonLottery_success_withoutUpdatingEligibilityRefreshRequired() {
        Payment payment = new Payment();
        payment.setId(68L);
        payment.setLotteryPayment(true);
        when(paymentRepository.getOne(68L)).thenReturn(payment);
        Configuration refreshRequired = new Configuration();
        refreshRequired.setBooleanValue(true);
        when(configurationRepository.findByName(ELIGIBILITY_REFRESH_REQUIRED)).thenReturn(refreshRequired);
        assertTrue(payment.isLotteryPayment());

        paymentService.markPaymentAsNonLottery(68L);

        verify(paymentRepository, times(1)).getOne(68L);
        verify(configurationRepository, times(1)).findByName(ELIGIBILITY_REFRESH_REQUIRED);
        verify(configurationRepository, never()).save(any(Configuration.class));
        verify(paymentRepository, times(1)).save(payment);
        assertFalse(payment.isLotteryPayment());
    }

    @Test
    public void markPaymentAsNonLottery_success_updatingEligibilityRefreshRequired() {
        Payment payment = new Payment();
        payment.setId(68L);
        payment.setLotteryPayment(true);
        when(paymentRepository.getOne(68L)).thenReturn(payment);
        Configuration refreshRequired = new Configuration();
        refreshRequired.setName(ELIGIBILITY_REFRESH_REQUIRED);
        refreshRequired.setBooleanValue(false);
        when(configurationRepository.findByName(ELIGIBILITY_REFRESH_REQUIRED)).thenReturn(refreshRequired);
        assertTrue(payment.isLotteryPayment());

        paymentService.markPaymentAsNonLottery(68L);

        verify(paymentRepository, times(1)).getOne(68L);
        verify(paymentRepository, times(1)).save(payment);
        ArgumentCaptor<Configuration> configurationArgumentCaptor = ArgumentCaptor.forClass(Configuration.class);
        verify(configurationRepository, times(1)).findByName(ELIGIBILITY_REFRESH_REQUIRED);
        verify(configurationRepository, times(1)).save(configurationArgumentCaptor.capture());
        assertFalse(payment.isLotteryPayment());
        assertEquals(ELIGIBILITY_REFRESH_REQUIRED, configurationArgumentCaptor.getValue().getName());
        assertTrue(configurationArgumentCaptor.getValue().getBooleanValue());
    }

    @Test
    public void markPaymentForLottery() {
        Payment payment = new Payment();
        payment.setId(68L);
        payment.setLotteryPayment(false);
        when(paymentRepository.getOne(68L)).thenReturn(payment);
        Configuration refreshRequired = new Configuration();
        refreshRequired.setBooleanValue(false);
        when(configurationRepository.findByName(ELIGIBILITY_REFRESH_REQUIRED)).thenReturn(refreshRequired);
        assertFalse(payment.isLotteryPayment());

        paymentService.markPaymentForLottery(68L);

        verify(paymentRepository, times(1)).getOne(68L);
        verify(paymentRepository, times(1)).save(payment);
        assertTrue(payment.isLotteryPayment());
    }

}