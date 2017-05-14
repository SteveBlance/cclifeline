package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.Address;
import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.service.AddressService;
import com.codaconsultancy.cclifeline.service.MemberService;
import com.codaconsultancy.cclifeline.view.AddressViewBean;
import com.codaconsultancy.cclifeline.view.MemberViewBean;
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
public class LifelineController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private AddressService addressService;

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

    @RequestMapping(value = "/add-member", method = RequestMethod.GET)
    public ModelAndView navigateToAddMember() {
        MemberViewBean member = new MemberViewBean();
        return modelAndView("add-member").addObject("member", member);
    }

    @RequestMapping(value = "/member", method = RequestMethod.POST)
    public ModelAndView addMember(@Valid @ModelAttribute("member") MemberViewBean memberViewBean, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            logger.debug("Validation errors for member: ",memberViewBean);
            return navigateToAddMember() ;
        }

        Member newMember = memberService.saveMember(memberViewBean.toEntity());

        return navigateToAddAddress(newMember.getId());

    }

    @RequestMapping(value = "/edit-member/{number}", method = RequestMethod.GET)
    public ModelAndView navigateToEditMember(@PathVariable Long number) {
        Member member = memberService.findMemberByMembershipNumber(number);
        return modelAndView("edit-member").addObject("member", member);
    }

    @RequestMapping(value = "/edit-member", method = RequestMethod.POST)
    public ModelAndView editMember(@Valid @ModelAttribute("member") Member member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.debug("Validation errors for member: ", member);
            return navigateToEditMember(member.getMembershipNumber());
        }

        Member updatedMember = memberService.updateMember(member);

        return memberDetails(updatedMember.getMembershipNumber());
    }

    @RequestMapping(value = "/add-address", method = RequestMethod.GET)
    public ModelAndView navigateToAddAddress(Long memberId) {
        AddressViewBean addressViewBean = new AddressViewBean();
        addressViewBean.setIsActive(true);
        addressViewBean.setMemberId(memberId);
        return modelAndView("add-address").addObject("address", addressViewBean);
    }

    @RequestMapping(value = "/member/{memberId}/edit-address", method = RequestMethod.GET)
    public ModelAndView navigateToEditAddress(@PathVariable Long memberId) {
        Member member = memberService.findMemberById(memberId);
        AddressViewBean addressViewBean;
        if (!member.getAddresses().isEmpty()) {
            addressViewBean = member.getAddresses().get(0).toViewBean();
        } else {
            addressViewBean = new AddressViewBean();
        }
        addressViewBean.setIsActive(true);
        addressViewBean.setMemberId(memberId);
        return modelAndView("edit-address").addObject("address", addressViewBean).addObject("member", member);
    }

    @RequestMapping(value = "/address", method = RequestMethod.POST)
    public ModelAndView addAddress(@Valid @ModelAttribute("address") AddressViewBean addressViewBean, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.debug("Validation errors for address: ", addressViewBean);
            return navigateToAddAddress(addressViewBean.getMemberId());
        }
        Address address = addressViewBean.toEntity();
        Member member = memberService.findMemberById(addressViewBean.getMemberId());
        address.setMember(member);
        addressService.saveAddress(address);

        return members();
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
