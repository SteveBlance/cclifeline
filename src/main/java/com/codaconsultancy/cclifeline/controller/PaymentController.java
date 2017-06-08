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
import java.util.List;

@Controller
public class PaymentController extends LifelineController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MemberService memberService;

    private Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @RequestMapping(value = "/payments", method = RequestMethod.GET)
    public ModelAndView navigateToPayments() {
        List<Payment> payments = paymentService.findAllPayments();
        return modelAndView("payments").addObject("payments", payments);
    }

    @RequestMapping(value = "/add-payment", method = RequestMethod.GET)
    public ModelAndView navigateToAddPayment() {
        List<Member> members = memberService.findAllMembers();
        PaymentViewBean payment = new PaymentViewBean();
        payment.setCreditedAccount("82621900174982CA");
        return modelAndView("add-payment").addObject("payment", payment).addObject("members", members);
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

        return navigateToPayments();
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
        return modelAndView("upload-payments");
    }

    @RequestMapping(value = "/upload-payments", method = RequestMethod.POST)
    public ModelAndView handleFileUpload(@RequestParam("file") MultipartFile file) {

//        storageService.store(file);
        String filename = file.getOriginalFilename();
        String contents = "";
        try {
            ByteArrayInputStream stream = new ByteArrayInputStream(file.getBytes());
            contents = IOUtils.toString(stream, "UTF-8");
        } catch (IOException e) {
            //TODO: properly handle error
            e.printStackTrace();
        }
        List<Payment> payments = paymentService.findAllPayments();
        return modelAndView("upload-payments").addObject("payments", payments).addObject("filename", filename);
    }

}
