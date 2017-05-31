package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.domain.Payment;
import com.codaconsultancy.cclifeline.domain.PaymentReference;
import com.codaconsultancy.cclifeline.service.MemberService;
import com.codaconsultancy.cclifeline.service.PaymentService;
import com.codaconsultancy.cclifeline.view.PaymentViewBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class PaymentController {

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
        PaymentReference paymentReference = new PaymentReference();
        paymentReference.setMember(member);
        return modelAndView("add-payment-reference").addObject("member", member).addObject("paymentReference", paymentReference);
    }

    @RequestMapping(value = "/payment-reference", method = RequestMethod.POST)
    public ModelAndView addPayment(@Valid @ModelAttribute("paymentReference") PaymentReference paymentReference, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.debug("Validation errors for reference: ", paymentReference);
            return navigateToAddPayment();
        }
//        Payment payment = paymentViewBean.toEntity();
//        Member member = memberService.findMemberById(paymentViewBean.getMemberId());
//        payment.setMember(member);
//        paymentService.savePayment(payment);

        return navigateToPayments();
    }


    private ModelAndView modelAndView(String page) {
        return new ModelAndView(page);
    }
}
