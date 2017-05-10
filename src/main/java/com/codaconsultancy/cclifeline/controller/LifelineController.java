package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.service.MemberService;
import com.codaconsultancy.cclifeline.view.MemberViewBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mustache.MustacheTemplateAvailabilityProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
public class LifelineController {

    @Autowired
    private MemberService memberService;

    private Logger logger = LoggerFactory.getLogger(LifelineController.class);

    @RequestMapping("/")
    public ModelAndView home() {
        long count = memberService.countAllMembers();
        return modelAndView("index").addObject("memberCount", count);
    }

    @RequestMapping("/members")
    public ModelAndView members() {
        List<Member> allMembers = memberService.findAllMembers();
        long count = memberService.countAllMembers();
        return modelAndView("members").addObject("memberCount", count).addObject("members", allMembers);
    }

    @RequestMapping(value = "/member/{number}", method = RequestMethod.GET)
    public ModelAndView memberDetails(@PathVariable Long number) {
        Member member = memberService.findMemberByMembershipNumber(number);
        return modelAndView("member").addObject("member", member);
    }

    @RequestMapping(value = "/member", method = RequestMethod.POST)
    public ModelAndView addMember(@Valid @ModelAttribute("member") MemberViewBean memberViewBean, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            logger.debug("Validation errors for member: ",memberViewBean);
            return navigateToAddMember() ;
        }

        Member newMember = memberService.saveMember(memberViewBean.toEntity());

        return memberDetails(newMember.getMembershipNumber());
    }

    @RequestMapping(value = "/add-member", method = RequestMethod.GET)
    public ModelAndView navigateToAddMember() {
        MemberViewBean member = new MemberViewBean();
        return modelAndView("add-member").addObject("member", member);
    }

    @RequestMapping(value = "/payments", method = RequestMethod.GET)
    public ModelAndView navigateToPayments() {
        return modelAndView("payments");
    }

    @RequestMapping(value = "/add-payment", method = RequestMethod.GET)
    public ModelAndView navigateToAddPayment() {
        return modelAndView("add-payment");
    }

    @RequestMapping(value = "/reports", method = RequestMethod.GET)
    public ModelAndView navigateToReports() {
        return modelAndView("reports");
    }

    @RequestMapping(value = "/winners", method = RequestMethod.GET)
    public ModelAndView navigateToWinners() {
        return modelAndView("winners");
    }

    @RequestMapping(value = "/make-draw", method = RequestMethod.GET)
    public ModelAndView navigateMakeDraw() {
        return modelAndView("make-draw");
    }

    @RequestMapping(value = "/export-data", method = RequestMethod.GET)
    public ModelAndView navigateExportData() {
        return modelAndView("export-data");
    }

    private ModelAndView modelAndView(String page) {
        return new ModelAndView(page);
    }

}
