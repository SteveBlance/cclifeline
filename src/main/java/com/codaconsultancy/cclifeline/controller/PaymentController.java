package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.domain.Payment;
import com.codaconsultancy.cclifeline.domain.PaymentReference;
import com.codaconsultancy.cclifeline.service.MemberService;
import com.codaconsultancy.cclifeline.service.PaymentService;
import com.codaconsultancy.cclifeline.view.PaymentReferenceViewBean;
import com.codaconsultancy.cclifeline.view.PaymentViewBean;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PaymentController extends LifelineController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MemberService memberService;

    private Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @RequestMapping(value = "/payments/{filter}", method = RequestMethod.GET)
    public ModelAndView navigateToPayments(@PathVariable String filter) {
        List<Payment> payments;
        String title;
        String allTabStatus = "enabled";
        String matchedTabStatus = "enabled";
        String unmatchedTabStatus = "enabled";
        if (filter.equalsIgnoreCase("matched")) {
            payments = paymentService.findAllMatchedPayments();
            title = "Matched payments";
            matchedTabStatus = "disabled";
        } else if (filter.equalsIgnoreCase("unmatched")) {
            payments = paymentService.findAllUnmatchedPayments();
            title = "Unmatched payments";
            unmatchedTabStatus = "disabled";
        } else {
            payments = paymentService.findAllPayments();
            title = "All payments";
            allTabStatus = "disabled";
        }
        return modelAndView("payments").addObject("payments", payments)
                .addObject("title", title)
                .addObject("allTabStatus", allTabStatus)
                .addObject("matchedTabStatus", matchedTabStatus)
                .addObject("unmatchedTabStatus", unmatchedTabStatus);
    }

    @RequestMapping(value = "payments/member/{membershipNumber}", method = RequestMethod.GET)
    public ModelAndView navigateToPaymentsForMember(@PathVariable Long membershipNumber) {
        Member member = memberService.findMemberByMembershipNumber(membershipNumber);
        List<Payment> payments = paymentService.findPaymentsForMember(member);
        return modelAndView("member-payments").addObject("payments", payments).addObject("member", member);
    }

    @RequestMapping(value = "/payment/{id}", method = RequestMethod.GET)
    public ModelAndView navigateToPaymentDetails(@PathVariable Long id) {
        Payment payment = paymentService.findById(id);
        return modelAndView("payment").addObject("payment", payment);
    }

    @RequestMapping(value = "/add-payment", method = RequestMethod.GET)
    public ModelAndView navigateToAddPayment() {
        List<Member> members = memberService.findCurrentMembers();
        PaymentViewBean payment = new PaymentViewBean();
        payment.setCreditedAccount("82621900174982CA");
        return modelAndView("add-payment").addObject("payment", payment).addObject("members", members);
    }

    @RequestMapping(value = "/edit-payment/{id}", method = RequestMethod.GET)
    public ModelAndView navigateToEditPayment(@PathVariable Long id) {
        Payment payment = paymentService.findById(id);
        List<Member> possiblePayers = paymentService.findPossiblePayers(payment);
        List<Member> members = memberService.findCurrentMembers();
        PaymentViewBean paymentViewBean = payment.toViewBean();
        return modelAndView("edit-payment").addObject("payment", paymentViewBean).addObject("members", members).addObject("possiblePayers", possiblePayers);
    }

    @RequestMapping(value = "/edit-payment", method = RequestMethod.POST)
    public ModelAndView editPayment(@Valid @ModelAttribute("payment") PaymentViewBean paymentViewBean, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.debug("Validation errors for payment: ", paymentViewBean);
            return navigateToEditPayment(paymentViewBean.getId());
        }
        Member member = memberService.findMemberById(paymentViewBean.getMemberId());
        if (paymentViewBean.isStoreReferenceForMatching() && null != member) {
            PaymentReference paymentReference = new PaymentReference(paymentViewBean.getCreditReference(), paymentViewBean.getName(), true, member);
            paymentService.savePaymentReference(paymentReference);
        }
        Payment payment = paymentViewBean.toEntity();
        payment.setMember(member);

        Payment updatedPayment = paymentService.updatePayment(payment);

        return navigateToPaymentDetails(updatedPayment.getId());
    }

    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public ModelAndView addPayment(@Valid @ModelAttribute("address") PaymentViewBean paymentViewBean, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.debug("Validation errors for address: ", paymentViewBean);
            return navigateToAddPayment();
        }
        Payment payment = paymentViewBean.toEntity();
        Member member = memberService.findMemberById(paymentViewBean.getMemberId());
        payment.setMember(member);
        paymentService.savePayment(payment);
        notificationService.logPayment(1);

        return navigateToPayments("all");
    }

    @RequestMapping(value = "/delete-payment/{id}", method = RequestMethod.POST)
    public ModelAndView deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return navigateToPayments("unmatched");
    }

    @RequestMapping(value = "/payment-references/member/{number}", method = RequestMethod.GET)
    public ModelAndView navigateToPaymentReferencesForMember(@PathVariable Long number) {
        Member member = memberService.findMemberByMembershipNumber(number);
        return modelAndView("payment-references").addObject("member", member);
    }

    @RequestMapping(value = "/add-payment-reference/member/{number}", method = RequestMethod.GET)
    public ModelAndView navigateToAddPaymentReference(@PathVariable Long number) {
        Member member = memberService.findMemberByMembershipNumber(number);
        PaymentReferenceViewBean paymentReferenceViewBean = new PaymentReferenceViewBean();
        paymentReferenceViewBean.setMember(member);
        return modelAndView("add-payment-reference").addObject("paymentReference", paymentReferenceViewBean);
    }

    @RequestMapping(value = "/payment-reference", method = RequestMethod.POST)
    public ModelAndView addPaymentReference(@Valid @ModelAttribute("paymentReference") PaymentReferenceViewBean paymentReferenceViewBean, BindingResult bindingResult) {
        Member member = memberService.findMemberByMembershipNumber(paymentReferenceViewBean.getMember().getMembershipNumber());
        if (bindingResult.hasErrors()) {
            logger.debug("Validation errors for reference: ", paymentReferenceViewBean);
            return navigateToAddPaymentReference(member.getMembershipNumber());
        }
        paymentReferenceViewBean.setIsActive(true);
        paymentReferenceViewBean.setMember(member);
        List<PaymentReference> paymentReferences = member.getPaymentReferences();
        PaymentReference paymentReference = paymentReferenceViewBean.toEntity();
        paymentReferences.add(paymentReference);
        member.setPaymentReferences(paymentReferences);

        paymentService.savePaymentReference(paymentReference);
        return navigateToPaymentReferencesForMember(member.getMembershipNumber());
    }

    @RequestMapping(value = "/upload-payments", method = RequestMethod.GET)
    public ModelAndView navigateToUploadPayments() {
        return modelAndView("upload-payments").addObject("disabled", true);
    }

    @RequestMapping(value = "/upload-payments", method = RequestMethod.POST)
    public ModelAndView handleFileUpload(@RequestParam("file") MultipartFile file) {
        String filename = file.getOriginalFilename();
        List<Payment> parsedPayments = new ArrayList<>();
        String contents;
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(file.getBytes());
            contents = IOUtils.toString(stream, "UTF-8");
            parsedPayments = paymentService.parsePayments(contents, filename);
            paymentService.savePayments(parsedPayments);
        } catch (IOException | NumberFormatException e) {
            //TODO: properly handle error
            e.printStackTrace();
        }
        notificationService.logPayment(parsedPayments.size());
        return modelAndView("upload-payments").addObject("payments", parsedPayments).addObject("filename", filename).addObject("disabled", false);
    }

}
