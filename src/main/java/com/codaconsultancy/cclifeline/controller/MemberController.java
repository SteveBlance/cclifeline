package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.Address;
import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.service.AddressService;
import com.codaconsultancy.cclifeline.service.LotteryDrawService;
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
public class MemberController extends LifelineController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private LotteryDrawService lotteryDrawService;

    private Logger logger = LoggerFactory.getLogger(MemberController.class);

    @RequestMapping("/")
    public ModelAndView home() {
        long count = memberService.countAllMembers();
        Long totalNumberOfWinners = lotteryDrawService.countAllWinners();
        return modelAndView("index")
                .addObject("memberCount", count)
                .addObject("totalNumberOfWinners", totalNumberOfWinners);
    }

    @RequestMapping("/members/{filter}")
    public ModelAndView members(@PathVariable String filter) {
        List<Member> members;
        String title;
        String currentTabStatus = "enabled";
        String formerTabStatus = "enabled";
        String allTabStatus = "enabled";
        if (filter.equalsIgnoreCase("current")) {
            members = memberService.findCurrentMembers();
            title = "Current members";
            currentTabStatus = "disabled";
        } else if (filter.equalsIgnoreCase("former")) {
            members = memberService.findFormerMembers();
            title = "Former members";
            formerTabStatus = "disabled";
        } else {
            members = memberService.findAllMembers();
            title = "All members";
            allTabStatus = "disabled";
        }
        for (Member member : members) {
            if (memberService.isEligibleForDraw(member)) {
                member.setIsEligibleForDraw(true);
            } else {
                member.setIsEligibleForDraw(false);
            }
        }
        long count = members.size();
        return modelAndView("members").addObject("memberCount", count)
                .addObject("members", members)
                .addObject("title", title)
                .addObject("currentTabStatus", currentTabStatus)
                .addObject("formerTabStatus", formerTabStatus)
                .addObject("allTabStatus", allTabStatus);
    }

    @RequestMapping(value = "/member/{number}", method = RequestMethod.GET)
    public ModelAndView memberDetails(@PathVariable Long number) {
        Member member = memberService.findMemberByMembershipNumber(number);
        boolean isEligible = memberService.isEligibleForDraw(member);
        member.setIsEligibleForDraw(isEligible);
        return modelAndView("member").addObject("member", member);
    }

    @RequestMapping(value = "/add-member", method = RequestMethod.GET)
    public ModelAndView navigateToAddMember() {
        MemberViewBean member = new MemberViewBean();
        return modelAndView("add-member").addObject("member", member);
    }

    @RequestMapping(value = "/member", method = RequestMethod.POST)
    public ModelAndView addMember(@Valid @ModelAttribute("member") MemberViewBean memberViewBean, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.debug("Validation errors for member: ", memberViewBean);
            return navigateToAddMember();
        }

        Member newMember = memberService.saveMember(memberViewBean.toEntity());
        notificationService.logNewMemberAdded(memberViewBean.getJoinDate());

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

        return navigateToEditAddress(updatedMember.getMembershipNumber());
    }

    @RequestMapping(value = "/add-address", method = RequestMethod.GET)
    public ModelAndView navigateToAddAddress(Long memberId) {
        AddressViewBean addressViewBean = new AddressViewBean();
        addressViewBean.setIsActive(true);
        addressViewBean.setMemberId(memberId);
        return modelAndView("add-address").addObject("address", addressViewBean);
    }

    @RequestMapping(value = "/member/{membershipNumber}/edit-address", method = RequestMethod.GET)
    public ModelAndView navigateToEditAddress(@PathVariable Long membershipNumber) {
        Member member = memberService.findMemberByMembershipNumber(membershipNumber);
        AddressViewBean addressViewBean;
        if (!member.getAddresses().isEmpty()) {
            addressViewBean = member.getAddresses().get(0).toViewBean();
        } else {
            addressViewBean = new AddressViewBean();
        }
        addressViewBean.setIsActive(true);
        addressViewBean.setMemberId(member.getId());
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

        return memberDetails(member.getMembershipNumber());
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView navigateToLogin() {
        return modelAndView("login");
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView navigateToLogout() {
        return modelAndView("logout");
    }

    @RequestMapping(value = "/reports", method = RequestMethod.GET)
    public ModelAndView navigateToReports() {
        return modelAndView("reports");
    }

    @RequestMapping(value = "/export-data", method = RequestMethod.GET)
    public ModelAndView navigateExportData() {
        return modelAndView("export-data");
    }
}
