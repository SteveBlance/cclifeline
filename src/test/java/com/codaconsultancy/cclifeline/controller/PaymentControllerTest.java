package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.common.TestHelper;
import com.codaconsultancy.cclifeline.domain.*;
import com.codaconsultancy.cclifeline.repositories.BaseTest;
import com.codaconsultancy.cclifeline.service.NotificationService;
import com.codaconsultancy.cclifeline.service.PaymentService;
import com.codaconsultancy.cclifeline.view.PaymentReferenceViewBean;
import com.codaconsultancy.cclifeline.view.PaymentViewBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaymentController.class)
public class PaymentControllerTest extends BaseTest {

    @Autowired
    PaymentController paymentController;

    @MockBean
    PaymentService paymentService;

    @MockBean
    NotificationService notificationService;

    @Before
    public void setup() {
        when(securitySubjectService.findByUsername(any(String.class))).thenReturn(new SecuritySubject());
    }

    @Test
    public void navigateToRecentPayments() throws Exception {
        List<PaymentViewBean> payments = getPaymentsAsViewBeans();
        when(paymentService.findRecentPayments()).thenReturn(payments);
        List<Notification> notifications = new ArrayList<>();
        when(notificationService.fetchLatestNotifications()).thenReturn(notifications);

        ModelAndView modelAndView = paymentController.navigateToPayments("recent");

        assertEquals(PaymentController.PAYMENTS_PAGE, modelAndView.getViewName());
        List<Payment> foundPayments = (List<Payment>) modelAndView.getModel().get("payments");
        assertEquals(3, foundPayments.size());
        assertEquals(20.00F, payments.get(0).getPaymentAmount(), 0.002F);
        assertEquals("FPS CREDIT 3344 LINDSAY", payments.get(0).getCreditReference());
        assertEquals(240.00F, payments.get(1).getPaymentAmount(), 0.002F);
        assertEquals("FPS CREDIT 0998 JONES", payments.get(1).getCreditReference());
        assertEquals(20.00F, payments.get(2).getPaymentAmount(), 0.002F);
        assertEquals("FPS CREDIT 0101 THOMAS", payments.get(2).getCreditReference());
        assertSame(notifications, modelAndView.getModel().get("notifications"));
        assertEquals("Recent payments", modelAndView.getModel().get("title"));
        assertEquals("disabled", modelAndView.getModel().get("recentTabStatus"));
        assertEquals("enabled", modelAndView.getModel().get("matchedTabStatus"));
        assertEquals("enabled", modelAndView.getModel().get("unmatchedTabStatus"));
        assertEquals("enabled", modelAndView.getModel().get("nonLotteryTabStatus"));
        assertEquals("enabled", modelAndView.getModel().get("allTabStatus"));
    }

    @Test
    public void navigateToAllPayments() throws Exception {
        List<PaymentViewBean> payments = getPaymentsAsViewBeans();
        when(paymentService.findAllPayments()).thenReturn(payments);
        List<Notification> notifications = new ArrayList<>();
        when(notificationService.fetchLatestNotifications()).thenReturn(notifications);

        ModelAndView modelAndView = paymentController.navigateToPayments("all");

        assertEquals(PaymentController.PAYMENTS_PAGE, modelAndView.getViewName());
        List<Payment> foundPayments = (List<Payment>) modelAndView.getModel().get("payments");
        assertEquals(3, foundPayments.size());
        assertEquals(20.00F, payments.get(0).getPaymentAmount(), 0.002F);
        assertEquals("FPS CREDIT 3344 LINDSAY", payments.get(0).getCreditReference());
        assertEquals(240.00F, payments.get(1).getPaymentAmount(), 0.002F);
        assertEquals("FPS CREDIT 0998 JONES", payments.get(1).getCreditReference());
        assertEquals(20.00F, payments.get(2).getPaymentAmount(), 0.002F);
        assertEquals("FPS CREDIT 0101 THOMAS", payments.get(2).getCreditReference());
        assertSame(notifications, modelAndView.getModel().get("notifications"));
        assertEquals("All payments", modelAndView.getModel().get("title"));
        assertEquals("disabled", modelAndView.getModel().get("allTabStatus"));
        assertEquals("enabled", modelAndView.getModel().get("recentTabStatus"));
        assertEquals("enabled", modelAndView.getModel().get("matchedTabStatus"));
        assertEquals("enabled", modelAndView.getModel().get("unmatchedTabStatus"));
        assertEquals("enabled", modelAndView.getModel().get("nonLotteryTabStatus"));
    }

    @Test
    public void navigateToPaymentsForMember() throws Exception {
        Member member = new Member();
        member.setMembershipNumber(1212L);
        when(memberService.findMemberByMembershipNumber(member.getMembershipNumber())).thenReturn(member);
        List<Payment> payments = getPayments();
        when(paymentService.findPaymentsForMember(member)).thenReturn(payments);

        ModelAndView modelAndView = paymentController.navigateToPaymentsForMember(member.getMembershipNumber());

        assertEquals(PaymentController.MEMBER_PAYMENTS_PAGE, modelAndView.getViewName());
        assertEquals(1212L, ((Member) modelAndView.getModel().get("member")).getMembershipNumber().longValue());
        assertEquals(3, ((List) modelAndView.getModel().get("payments")).size());
    }

    @Test
    public void navigateToPaymentDetails() {
        Payment payment = new Payment();
        long paymentId = 73L;
        payment.setId(paymentId);
        when(paymentService.findById(paymentId)).thenReturn(payment);

        ModelAndView modelAndView = paymentController.navigateToPaymentDetails(paymentId);

        verify(paymentService, times(1)).findById(paymentId);
        assertEquals(PaymentController.PAYMENT_DETAILS_PAGE, modelAndView.getViewName());
        assertSame(payment, modelAndView.getModel().get("payment"));
    }

    @Test
    public void navigateToPaymentsWithMatchedFilter() throws Exception {
        List<PaymentViewBean> payments = getPaymentsAsViewBeans();
        when(paymentService.findAllMatchedLotteryPayments()).thenReturn(payments);
        List<Notification> notifications = new ArrayList<>();
        when(notificationService.fetchLatestNotifications()).thenReturn(notifications);

        ModelAndView modelAndView = paymentController.navigateToPayments("matched");

        assertEquals(PaymentController.PAYMENTS_PAGE, modelAndView.getViewName());
        List<PaymentViewBean> foundPayments = (List<PaymentViewBean>) modelAndView.getModel().get("payments");
        assertSame(notifications, modelAndView.getModel().get("notifications"));
        assertEquals(3, foundPayments.size());
        assertEquals("Matched payments", modelAndView.getModel().get("title"));
        assertEquals("enabled", modelAndView.getModel().get("allTabStatus"));
        assertEquals("enabled", modelAndView.getModel().get("recentTabStatus"));
        assertEquals("disabled", modelAndView.getModel().get("matchedTabStatus"));
        assertEquals("enabled", modelAndView.getModel().get("unmatchedTabStatus"));
        assertEquals("enabled", modelAndView.getModel().get("nonLotteryTabStatus"));

    }

    @Test
    public void navigateToPaymentsWithUnmatchedFilter() throws Exception {
        List<PaymentViewBean> payments = getPaymentsAsViewBeans();
        when(paymentService.findAllUnmatchedPayments()).thenReturn(payments);
        List<Notification> notifications = new ArrayList<>();
        when(notificationService.fetchLatestNotifications()).thenReturn(notifications);

        ModelAndView modelAndView = paymentController.navigateToPayments("unmatched");

        assertEquals(PaymentController.PAYMENTS_PAGE, modelAndView.getViewName());
        List<PaymentViewBean> foundPayments = (List<PaymentViewBean>) modelAndView.getModel().get("payments");
        assertEquals(3, foundPayments.size());
        assertSame(notifications, modelAndView.getModel().get("notifications"));
        assertEquals("enabled", modelAndView.getModel().get("allTabStatus"));
        assertEquals("enabled", modelAndView.getModel().get("recentTabStatus"));
        assertEquals("enabled", modelAndView.getModel().get("matchedTabStatus"));
        assertEquals("disabled", modelAndView.getModel().get("unmatchedTabStatus"));
        assertEquals("enabled", modelAndView.getModel().get("nonLotteryTabStatus"));
        assertEquals("Unmatched payments", modelAndView.getModel().get("title"));
    }

    @Test
    public void navigateToPaymentsWithNonLotteryFilter() throws Exception {
        List<PaymentViewBean> payments = getPaymentsAsViewBeans();
        when(paymentService.findAllNonLotteryPayments()).thenReturn(payments);
        List<Notification> notifications = new ArrayList<>();
        when(notificationService.fetchLatestNotifications()).thenReturn(notifications);

        ModelAndView modelAndView = paymentController.navigateToPayments("non-lottery");

        assertEquals(PaymentController.PAYMENTS_PAGE, modelAndView.getViewName());
        List<PaymentViewBean> foundPayments = (List<PaymentViewBean>) modelAndView.getModel().get("payments");
        assertEquals(3, foundPayments.size());
        assertSame(notifications, modelAndView.getModel().get("notifications"));
        assertEquals("enabled", modelAndView.getModel().get("allTabStatus"));
        assertEquals("enabled", modelAndView.getModel().get("recentTabStatus"));
        assertEquals("enabled", modelAndView.getModel().get("matchedTabStatus"));
        assertEquals("enabled", modelAndView.getModel().get("unmatchedTabStatus"));
        assertEquals("disabled", modelAndView.getModel().get("nonLotteryTabStatus"));
        assertEquals("Non-lottery payments", modelAndView.getModel().get("title"));
    }

    @Test
    public void navigateToAddPayment() {
        List<Member> members = new ArrayList<>();
        Member member1 = TestHelper.newMember(5678L, "Bob", "Beth", "e@mail.com", "01323232", null, "Monthly", "Lifeline", null, "Open");
        Member member2 = TestHelper.newMember(4678L, "Jane", "Kent", "j@mail.com", "01323233", null, "Monthly", "Lifeline", null, "Open");
        Member member3 = TestHelper.newMember(3678L, "Fred", "Reid", "f@mail.com", "01323234", null, "Monthly", "Lifeline", null, "Open");
        members.add(member1);
        members.add(member2);
        members.add(member3);
        when(memberService.findAllMembersOrderedBySurname()).thenReturn(members);

        ModelAndView modelAndView = paymentController.navigateToAddPayment();

        verify(memberService, times(1)).findAllMembersOrderedBySurname();
        assertEquals(PaymentController.ADD_PAYMENT_PAGE, modelAndView.getViewName());
        assertEquals(3, ((List) modelAndView.getModel().get("members")).size());
        Object payment = modelAndView.getModel().get("payment");
        assertTrue(payment instanceof PaymentViewBean);
        String defaultAccount = "82621900174982CA";
        assertEquals(defaultAccount, ((PaymentViewBean) payment).getCreditedAccount());
    }

    @Test
    public void navigateToEditPayment() {
        List<Member> members = new ArrayList<>();
        when(memberService.findAllMembersOrderedBySurname()).thenReturn(members);
        Payment payment = new Payment();
        payment.setCreditReference("GH 1234");
        payment.setLotteryPayment(true);
        when(paymentService.findById(1234L)).thenReturn(payment);
        List<Member> possiblePayers = new ArrayList<>();
        Member possiblePayer1 = new Member();
        Member possiblePayer2 = new Member();
        possiblePayers.add(possiblePayer1);
        possiblePayers.add(possiblePayer2);
        when(paymentService.findPossiblePayers(payment)).thenReturn(possiblePayers);

        ModelAndView response = paymentController.navigateToEditPayment(1234L);

        verify(memberService, times(1)).findAllMembersOrderedBySurname();
        assertEquals(PaymentController.EDIT_PAYMENT_PAGE, response.getViewName());
        Object paymentViewBean = response.getModel().get("payment");
        assertTrue(paymentViewBean instanceof PaymentViewBean);
        assertEquals("GH 1234", ((PaymentViewBean) paymentViewBean).getCreditReference());
        assertSame(members, response.getModel().get("members"));
        assertSame(possiblePayers, response.getModel().get("possiblePayers"));
    }

    @Test
    public void addPayment_success() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        PaymentViewBean paymentViewBean = new PaymentViewBean(paymentDate, 12.23F, "FPS CREDIT 1986 JACK", "83776900435093BZ", "BOB SMITH", true);
        paymentViewBean.setId(367L);
        paymentViewBean.setMemberId(555L);
        BindingResult bindingResult = getBindingResult("payment");
        ArgumentCaptor<Payment> paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);
        Member member = TestHelper.newMember(3678L, "Fred", "Reid", "f@mail.com", "01323234", null, "Monthly", "Lifeline", null, "Open");
        member.setId(555L);
        when(memberService.findMemberById(member.getId())).thenReturn(member);
        Payment payment = paymentViewBean.toEntity();
        when(paymentService.savePayment(paymentArgumentCaptor.capture())).thenReturn(payment);

        ModelAndView modelAndView = paymentController.addPayment(paymentViewBean, bindingResult);

        verify(memberService, times(1)).findMemberById(555L);
        verify(paymentService, never()).savePaymentReference(any(PaymentReference.class));
        verify(paymentService, times(1)).savePayment(paymentArgumentCaptor.capture());
        Payment savedPayment = paymentArgumentCaptor.getValue();
        assertEquals(367L, savedPayment.getId().longValue());
        assertEquals(12.23F, savedPayment.getPaymentAmount(), 0.002F);
        assertEquals("f@mail.com", savedPayment.getMember().getEmail());
        verify(notificationService, times(1)).logPayment(1);
        assertEquals(PaymentController.PAYMENT_DETAILS_PAGE, modelAndView.getViewName());
    }

    public void addPaymentAndStoreReference_success() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        PaymentViewBean paymentViewBean = new PaymentViewBean(paymentDate, 12.23F, "FPS CREDIT JACK", "83776900435093BZ", "BOB SMITH", true);
        paymentViewBean.setId(367L);
        paymentViewBean.setMemberId(555L);
        paymentViewBean.setStoreReferenceForMatching(true);
        BindingResult bindingResult = getBindingResult("payment");
        ArgumentCaptor<Payment> paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);
        Member member = TestHelper.newMember(3678L, "Fred", "Reid", "f@mail.com", "01323234", null, "Monthly", "Lifeline", null, "Open");
        member.setId(555L);
        when(memberService.findMemberById(member.getId())).thenReturn(member);

        ModelAndView modelAndView = paymentController.addPayment(paymentViewBean, bindingResult);

        verify(memberService, times(1)).findMemberById(555L);
        verify(paymentService, times(1)).savePayment(paymentArgumentCaptor.capture());
        ArgumentCaptor<PaymentReference> paymentReferenceArgumentCaptor = ArgumentCaptor.forClass(PaymentReference.class);
        verify(paymentService, times(1)).savePaymentReference(paymentReferenceArgumentCaptor.capture());
        PaymentReference savePaymentReference = paymentReferenceArgumentCaptor.getValue();
        assertEquals("FPS CREDIT JACK", savePaymentReference.getReference());
        assertEquals("BOB SMITH", savePaymentReference.getName());
        Payment savedPayment = paymentArgumentCaptor.getValue();
        assertEquals(367L, savedPayment.getId().longValue());
        assertEquals(12.23F, savedPayment.getPaymentAmount(), 0.002F);
        assertEquals("f@mail.com", savedPayment.getMember().getEmail());
        verify(notificationService, times(1)).logPayment(1);
        assertEquals("payments", modelAndView.getViewName());
    }

    @Test
    public void addPayment_validationErrors() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        PaymentViewBean paymentViewBean = new PaymentViewBean(paymentDate, 12.23F, "FPS CREDIT 1986 JACK", "83776900435093BZ", "BOB SMITH", true);
        paymentViewBean.setId(367L);
        paymentViewBean.setMemberId(555L);
        BindingResult bindingResult = getBindingResult("payment");
        bindingResult.addError(new ObjectError("paymentAmount", "Payment amount cannot be blank"));
        Member member = TestHelper.newMember(3678L, "Fred", "Reid", "f@mail.com", "01323234", null, "Monthly", "Lifeline", null, "Open");
        member.setId(555L);
        when(memberService.findMemberById(member.getId())).thenReturn(member);

        paymentController.addPayment(paymentViewBean, bindingResult);

        verify(memberService, never()).findMemberById(555L);
        verify(paymentService, never()).savePayment(any(Payment.class));
        verify(notificationService, never()).logPayment(1);
    }

    @Test
    public void editPayment_success() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        PaymentViewBean paymentViewBean = new PaymentViewBean(paymentDate, 12.23F, "FPS CREDIT 1986 JACK", "83776900435093BZ", "BOB SMITH", true);
        paymentViewBean.setId(367L);
        paymentViewBean.setMemberId(555L);
        BindingResult bindingResult = getBindingResult("payment");
        ArgumentCaptor<Payment> paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);
        Member member = TestHelper.newMember(3678L, "Fred", "Reid", "f@mail.com", "01323234", null, "Monthly", "Lifeline", null, "Open");
        member.setId(555L);
        when(memberService.findMemberById(member.getId())).thenReturn(member);
        Payment payment = paymentViewBean.toEntity();
        when(paymentService.updatePayment(paymentArgumentCaptor.capture())).thenReturn(payment);

        ModelAndView modelAndView = paymentController.editPayment(paymentViewBean, bindingResult);

        verify(memberService, times(1)).findMemberById(555L);
        verify(paymentService, never()).savePaymentReference(any(PaymentReference.class));
        verify(paymentService, times(1)).updatePayment(paymentArgumentCaptor.capture());
        Payment updatedPayment = paymentArgumentCaptor.getValue();
        assertEquals(367L, updatedPayment.getId().longValue());
        assertEquals(12.23F, updatedPayment.getPaymentAmount(), 0.002F);
        assertEquals("f@mail.com", updatedPayment.getMember().getEmail());
        assertEquals("payment", modelAndView.getViewName());
    }

    @Test
    public void editPaymentAndStoreReference_success() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        PaymentViewBean paymentViewBean = new PaymentViewBean(paymentDate, 12.23F, "FPS CREDIT 1986 JACK", "83776900435093BZ", "BOB SMITH", true);
        paymentViewBean.setId(367L);
        paymentViewBean.setMemberId(555L);
        paymentViewBean.setStoreReferenceForMatching(true);
        BindingResult bindingResult = getBindingResult("payment");
        ArgumentCaptor<Payment> paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);
        Member member = TestHelper.newMember(3678L, "Fred", "Reid", "f@mail.com", "01323234", null, "Monthly", "Lifeline", null, "Open");
        member.setId(555L);
        when(memberService.findMemberById(member.getId())).thenReturn(member);
        Payment payment = paymentViewBean.toEntity();
        when(paymentService.updatePayment(paymentArgumentCaptor.capture())).thenReturn(payment);

        ModelAndView modelAndView = paymentController.editPayment(paymentViewBean, bindingResult);

        ArgumentCaptor<PaymentReference> paymentReferenceArgumentCaptor = ArgumentCaptor.forClass(PaymentReference.class);
        verify(paymentService, times(1)).savePaymentReference(paymentReferenceArgumentCaptor.capture());
        PaymentReference savePaymentReference = paymentReferenceArgumentCaptor.getValue();
        assertEquals("FPS CREDIT 1986 JACK", savePaymentReference.getReference());
        assertEquals("BOB SMITH", savePaymentReference.getName());
        verify(memberService, times(1)).findMemberById(555L);
        verify(paymentService, times(1)).savePaymentReference(any(PaymentReference.class));
        verify(paymentService, times(1)).updatePayment(paymentArgumentCaptor.capture());
        Payment updatedPayment = paymentArgumentCaptor.getValue();
        assertEquals(367L, updatedPayment.getId().longValue());
        assertEquals(12.23F, updatedPayment.getPaymentAmount(), 0.002F);
        assertEquals("f@mail.com", updatedPayment.getMember().getEmail());
        assertEquals("payment", modelAndView.getViewName());
    }

    @Test
    public void editPayment_validationErrors() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        PaymentViewBean paymentViewBean = new PaymentViewBean(paymentDate, 12.23F, "FPS CREDIT 1986 JACK", "83776900435093BZ", "BOB SMITH", true);
        paymentViewBean.setId(367L);
        paymentViewBean.setMemberId(555L);
        BindingResult bindingResult = getBindingResult("payment");
        bindingResult.addError(new ObjectError("paymentAmount", "Payment amount cannot be blank"));
        Member member = TestHelper.newMember(3678L, "Fred", "Reid", "f@mail.com", "01323234", null, "Monthly", "Lifeline", null, "Open");
        member.setId(555L);
        when(memberService.findMemberById(member.getId())).thenReturn(member);
        when(memberService.findAllMembersOrderedBySurname()).thenReturn(new ArrayList<>());
        when(paymentService.findById(367L)).thenReturn(paymentViewBean.toEntity());
        when(paymentService.findPossiblePayers(any(Payment.class))).thenReturn(new ArrayList<>());

        paymentController.editPayment(paymentViewBean, bindingResult);

        verify(memberService, never()).findMemberById(555L);
        verify(paymentService, never()).updatePayment(any(Payment.class));
    }


    @Test
    public void navigateToPaymentReferencesForMember() {
        Member member = new Member();
        member.setMembershipNumber(7676L);
        member.setForename("Bob");
        when(memberService.findMemberByMembershipNumber(7676L)).thenReturn(member);

        ModelAndView modelAndView = paymentController.navigateToPaymentReferencesForMember(7676L);

        verify(memberService, times(1)).findMemberByMembershipNumber(7676L);
        assertEquals("payment-references", modelAndView.getViewName());
        assertEquals("Bob", ((Member) modelAndView.getModel().get("member")).getForename());
    }

    @Test
    public void navigateToAddPaymentReference() {
        Member member = new Member();
        member.setMembershipNumber(4680L);
        when(memberService.findMemberByMembershipNumber(member.getMembershipNumber())).thenReturn(member);

        ModelAndView modelAndView = paymentController.navigateToAddPaymentReference(member.getMembershipNumber());

        verify(memberService, times(1)).findMemberByMembershipNumber(member.getMembershipNumber());
        Object paymentReference = modelAndView.getModel().get("paymentReference");
        assertTrue(paymentReference instanceof PaymentReferenceViewBean);
        assertSame(member, ((PaymentReferenceViewBean) paymentReference).getMember());
        assertEquals("add-payment-reference", modelAndView.getViewName());
    }

    @Test
    public void addPaymentReference_success() {
        PaymentReferenceViewBean paymentReferenceViewBean = new PaymentReferenceViewBean();
        paymentReferenceViewBean.setReference("BGC 9182 SMITH");
        Member member = new Member();
        member.setMembershipNumber(9182L);
        paymentReferenceViewBean.setMember(member);
        when(memberService.findMemberByMembershipNumber(9182L)).thenReturn(member);
        assertNull(paymentReferenceViewBean.getIsActive());

        ModelAndView modelAndView = paymentController.addPaymentReference(paymentReferenceViewBean, getBindingResult("paymentReference"));

        assertTrue(paymentReferenceViewBean.getIsActive());
        assertEquals(9182L, paymentReferenceViewBean.getMember().getMembershipNumber().longValue());
        assertEquals(1, member.getPaymentReferences().size());
        assertEquals("BGC 9182 SMITH", member.getPaymentReferences().get(0).getReference());
        verify((memberService), times(2)).findMemberByMembershipNumber(9182L);
        ArgumentCaptor<PaymentReference> paymentReferenceArgumentCaptor = ArgumentCaptor.forClass(PaymentReference.class);
        verify((paymentService), times(1)).savePaymentReference(paymentReferenceArgumentCaptor.capture());
        assertEquals("BGC 9182 SMITH", paymentReferenceArgumentCaptor.getValue().getReference());
        assertEquals("payment-references", modelAndView.getViewName());
    }

    @Test
    public void addPaymentReference_validationFailure() {
        PaymentReferenceViewBean paymentReferenceViewBean = new PaymentReferenceViewBean();
        Member member = new Member();
        member.setMembershipNumber(9182L);
        paymentReferenceViewBean.setMember(member);
        when(memberService.findMemberByMembershipNumber(9182L)).thenReturn(member);

        BindingResult bindingResult = getBindingResult("paymentReference");
        bindingResult.addError(new ObjectError("paymentReference", "Reference cannot be blank"));

        ModelAndView modelAndView = paymentController.addPaymentReference(paymentReferenceViewBean, bindingResult);

        assertNull(paymentReferenceViewBean.getIsActive());

        verify((memberService), times(2)).findMemberByMembershipNumber(9182L);
        verify((paymentService), never()).savePaymentReference(any(PaymentReference.class));
        assertEquals("add-payment-reference", modelAndView.getViewName());
    }

    @Test
    public void navigateToUploadPayments() {
        ModelAndView modelAndView = paymentController.navigateToUploadPayments();
        assertEquals("upload-payments", modelAndView.getViewName());
    }

    @Test
    public void deletePayment() {
        ModelAndView modelAndView = paymentController.deletePayment(78L);
        verify(paymentService, times(1)).deletePayment(78L);
        assertEquals(PaymentController.PAYMENTS_PAGE, modelAndView.getViewName());
        assertEquals("Unmatched payments", modelAndView.getModel().get("title"));
    }

    @Test
    public void markPaymentAsNonLottery() {
        long paymentId = 99L;

        ModelAndView modelAndView = paymentController.markPaymentAsNonLottery(paymentId);

        verify(paymentService, times(1)).markPaymentAsNonLottery(paymentId);
        assertEquals(PaymentController.PAYMENT_DETAILS_PAGE, modelAndView.getViewName());
        assertEquals("Payment marked as non-lottery payment", modelAndView.getModel().get("alertMessage"));
        assertEquals("alert alert-success", modelAndView.getModel().get("alertClass"));
    }

    @Test
    public void markPaymentForLottery() {
        long paymentId = 98L;

        ModelAndView modelAndView = paymentController.markPaymentForLottery(paymentId);

        verify(paymentService, times(1)).markPaymentForLottery(paymentId);
        assertEquals(PaymentController.PAYMENT_DETAILS_PAGE, modelAndView.getViewName());
        assertEquals("alert alert-success", modelAndView.getModel().get("alertClass"));
        assertEquals("Payment marked as a lottery payment", modelAndView.getModel().get("alertMessage"));
    }

    @Test
    public void uploadPayment() throws Exception {
        byte[] content = new byte[10];
        MultipartFile file = new MockMultipartFile("payments.csv", "payments.csv", "", content);
        List<Payment> payments = getPayments();
        when(paymentService.parsePayments(anyString(), eq("payments.csv"))).thenReturn(payments);

        ModelAndView modelAndView = paymentController.handleFileUpload(file);

        verify(paymentService, times(1)).parsePayments(anyString(), eq("payments.csv"));
        verify(paymentService, times(1)).savePayments(payments);
        verify(notificationService, times(1)).logPayment(3);
        assertEquals(PaymentController.UPLOAD_PAYMENTS_PAGE, modelAndView.getViewName());
        assertEquals("payments.csv", modelAndView.getModel().get("filename"));
        assertFalse((boolean) modelAndView.getModel().get("disabled"));
    }

    @Test
    public void uploadPayment_failure() throws Exception {
        byte[] content = new byte[10];
        MultipartFile file = new MockMultipartFile("payments.csv", "payments.csv", "", content);
        List<Payment> payments = getPayments();
        when(paymentService.parsePayments(anyString(), eq("payments.csv"))).thenThrow(new IOException("Invalid file"));

        ModelAndView modelAndView = paymentController.handleFileUpload(file);

        verify(paymentService, times(1)).parsePayments(anyString(), eq("payments.csv"));
        verify(paymentService, never()).savePayments(payments);
        verify(notificationService, never()).logPayment(3);
        assertEquals(PaymentController.UPLOAD_PAYMENTS_PAGE, modelAndView.getViewName());
        assertEquals("payments.csv", modelAndView.getModel().get("filename"));
        assertTrue((boolean) modelAndView.getModel().get("disabled"));
        assertEquals("alert alert-danger", modelAndView.getModel().get("alertClass"));
        assertEquals("Upload of payments failed. Please check bank statement file.", modelAndView.getModel().get("alertMessage"));
    }


    private List<Payment> getPayments() throws ParseException {
        List<Payment> payments = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        Payment payment1 = new Payment(paymentDate, 20.00F, "FPS CREDIT 3344 LINDSAY", "83776900435093BZ", "BOB SMITH", true);
        Payment payment2 = new Payment(paymentDate, 240.00F, "FPS CREDIT 0998 JONES", "83776900435093BZ", "BOB SMITH", true);
        Payment payment3 = new Payment(paymentDate, 20.00F, "FPS CREDIT 0101 THOMAS", "83776900435093BZ", "BOB SMITH", true);
        payments.add(payment1);
        payments.add(payment2);
        payments.add(payment3);
        return payments;
    }

    private List<PaymentViewBean> getPaymentsAsViewBeans() throws Exception {
        List<PaymentViewBean> paymentViewBeans = new ArrayList<>();
        List<Payment> payments = getPayments();
        for (Payment payment : payments) {
            paymentViewBeans.add(payment.toViewBean());
        }
        return paymentViewBeans;
    }

}