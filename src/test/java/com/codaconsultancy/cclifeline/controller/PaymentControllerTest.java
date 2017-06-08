package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.common.TestHelper;
import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.domain.Notification;
import com.codaconsultancy.cclifeline.domain.Payment;
import com.codaconsultancy.cclifeline.repositories.BaseTest;
import com.codaconsultancy.cclifeline.service.MemberService;
import com.codaconsultancy.cclifeline.service.NotificationService;
import com.codaconsultancy.cclifeline.service.PaymentService;
import com.codaconsultancy.cclifeline.view.PaymentViewBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@EnableJpaRepositories(basePackages = {"com.codaconsultancy.cclifeline.repositories"})
@SpringBootTest(classes = PaymentController.class)
public class PaymentControllerTest extends BaseTest {

    @Autowired
    PaymentController paymentController;

    @MockBean
    PaymentService paymentService;

    @MockBean
    MemberService memberService;

    @MockBean
    NotificationService notificationService;

    @Test
    public void navigateToPayments() throws Exception {
        List<Payment> payments = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        Payment payment1 = new Payment(paymentDate, 20.00F, "FPS CREDIT 3344 LINDSAY", "83776900435093BZ");
        Payment payment2 = new Payment(paymentDate, 240.00F, "FPS CREDIT 0998 JONES", "83776900435093BZ");
        Payment payment3 = new Payment(paymentDate, 20.00F, "FPS CREDIT 0101 THOMAS", "83776900435093BZ");
        payments.add(payment1);
        payments.add(payment2);
        payments.add(payment3);
        when(paymentService.findAllPayments()).thenReturn(payments);
        List<Notification> notifications = new ArrayList<>();
        when(notificationService.fetchLatestNotifications()).thenReturn(notifications);


        ModelAndView modelAndView = paymentController.navigateToPayments();

        assertEquals("payments", modelAndView.getViewName());
        List<Payment> foundPayments = (List<Payment>) modelAndView.getModel().get("payments");
        assertEquals(3, foundPayments.size());
        assertEquals(20.00F, payments.get(0).getPaymentAmount(), 0.002F);
        assertEquals("FPS CREDIT 3344 LINDSAY", payments.get(0).getCreditReference());
        assertEquals(240.00F, payments.get(1).getPaymentAmount(), 0.002F);
        assertEquals("FPS CREDIT 0998 JONES", payments.get(1).getCreditReference());
        assertEquals(20.00F, payments.get(2).getPaymentAmount(), 0.002F);
        assertEquals("FPS CREDIT 0101 THOMAS", payments.get(2).getCreditReference());
        assertSame(notifications, modelAndView.getModel().get("notifications"));

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
        when(memberService.findAllMembers()).thenReturn(members);

        ModelAndView modelAndView = paymentController.navigateToAddPayment();

        verify(memberService, times(1)).findAllMembers();
        assertEquals("add-payment", modelAndView.getViewName());
        assertEquals(3, ((List) modelAndView.getModel().get("members")).size());
        Object payment = modelAndView.getModel().get("payment");
        assertTrue(payment instanceof PaymentViewBean);
        String defaultAccount = "82621900174982CA";
        assertEquals(defaultAccount, ((PaymentViewBean) payment).getCreditedAccount());
    }

    @Test
    public void addPayment_success() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        PaymentViewBean paymentViewBean = new PaymentViewBean(paymentDate, 12.23F, "FPS CREDIT 1986 JACK", "83776900435093BZ");
        paymentViewBean.setId(367L);
        paymentViewBean.setMemberId(555L);
        BindingResult bindingResult = getBindingResult("payment");
        ArgumentCaptor<Payment> paymentArgumentCaptor = ArgumentCaptor.forClass(Payment.class);
        Member member = TestHelper.newMember(3678L, "Fred", "Reid", "f@mail.com", "01323234", null, "Monthly", "Lifeline", null, "Open");
        member.setId(555L);
        when(memberService.findMemberById(member.getId())).thenReturn(member);

        ModelAndView modelAndView = paymentController.addPayment(paymentViewBean, bindingResult);

        verify(memberService, times(1)).findMemberById(555L);
        verify(paymentService, times(1)).savePayment(paymentArgumentCaptor.capture());
        Payment savedPayment = paymentArgumentCaptor.getValue();
        assertEquals(367L, savedPayment.getId().longValue());
        assertEquals(12.23F, savedPayment.getPaymentAmount(), 0.002F);
        assertEquals("f@mail.com", savedPayment.getMember().getEmail());
    }

    @Test
    public void addPayment_validationErrors() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date paymentDate = sdf.parse("20170103 ");
        PaymentViewBean paymentViewBean = new PaymentViewBean(paymentDate, 12.23F, "FPS CREDIT 1986 JACK", "83776900435093BZ");
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
    public void navigateToUploadPayments() {
        ModelAndView modelAndView = paymentController.navigateToUploadPayments();
        assertEquals("upload-payments", modelAndView.getViewName());
    }

}