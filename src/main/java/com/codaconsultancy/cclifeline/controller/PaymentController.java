package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.Payment;
import com.codaconsultancy.cclifeline.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PaymentController {

    @Autowired
    private PaymentService paymentService;


    @RequestMapping(value = "/payments", method = RequestMethod.GET)
    public ModelAndView navigateToPayments() {
        List<Payment> payments = paymentService.findAllPayments();
        return modelAndView("payments").addObject("payments", payments);
    }

    @RequestMapping(value = "/add-payment", method = RequestMethod.GET)
    public ModelAndView navigateToAddPayment() {
        return modelAndView("add-payment");
    }

    private ModelAndView modelAndView(String page) {
        return new ModelAndView(page);
    }
}
