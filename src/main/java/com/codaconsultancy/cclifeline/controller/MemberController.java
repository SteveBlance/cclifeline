package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.Address;
import com.codaconsultancy.cclifeline.domain.LotteryDraw;
import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.service.AddressService;
import com.codaconsultancy.cclifeline.service.LotteryDrawService;
import com.codaconsultancy.cclifeline.service.MemberService;
import com.codaconsultancy.cclifeline.service.PaymentService;
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

    private static final String ENABLED = "enabled";
    private static final String DISABLED = "disabled";
    public static final String MEMBER_PAGE = "member";
    public static final String ADD_MEMBER_PAGE = "add-member";
    public static final String EDIT_MEMBER_PAGE = "edit-member";
    public static final String ADD_ADDRESS_PAGE = "add-address";
    public static final String EDIT_ADDRESS_PAGE = "edit-address";
    public static final String LOGIN_PAGE = "login";
    public static final String LOGOUT_PAGE = "logout";
    public static final String EXPORT_DATA_PAGE = "export-data";

    @Autowired
    private MemberService memberService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private LotteryDrawService lotteryDrawService;

    private Logger logger = LoggerFactory.getLogger(MemberController.class);

    @RequestMapping("/")
    public ModelAndView home() {
        long count = memberService.countAllCurrentMembers();
        Long totalNumberOfWinners = lotteryDrawService.countAllWinners();
        LotteryDraw lastDraw = lotteryDrawService.fetchLastDraw();
        return modelAndView("index")
                .addObject("memberCount", count)
                .addObject("totalNumberOfWinners", totalNumberOfWinners)
                .addObject("lastDraw", lastDraw);
    }

    @RequestMapping("/members/{filter}")
    public ModelAndView members(@PathVariable String filter) {
        memberService.updateEligibilityStatuses();
        List<MemberViewBean> members;
        String title;
        String currentTabStatus = ENABLED;
        String formerTabStatus = ENABLED;
        String allTabStatus = ENABLED;
        String eligibleTabStatus = ENABLED;
        String pendingTabStatus = ENABLED;
        String recentlyLapsedTabStatus = ENABLED;
        switch (filter) {
            case "current":
                members = memberService.findCurrentMembers();
                title = "Current members";
                currentTabStatus = DISABLED;
                break;
            case "former":
                members = memberService.findFormerMembers();
                title = "Former members";
                formerTabStatus = DISABLED;
                break;
            case "eligible":
                members = memberService.findEligibleMembers();
                title = "Eligible for draw";
                eligibleTabStatus = DISABLED;
                break;
            case "pending":
                members = memberService.findPendingMembers();
                title = "Membership not confirmed";
                pendingTabStatus = DISABLED;
                break;
            case "recently-lapsed":
                members = memberService.findRecentlyLapsedMembers();
                title = "Recently lapsed members";
                recentlyLapsedTabStatus = DISABLED;
                break;
            default:
                members = memberService.findAllMembers();
                title = "All members";
                allTabStatus = DISABLED;
                break;
        }
        long count = members.size();
        return modelAndView("members").addObject("memberCount", count)
                .addObject("members", members)
                .addObject("title", title)
                .addObject("currentTabStatus", currentTabStatus)
                .addObject("formerTabStatus", formerTabStatus)
                .addObject("eligibleTabStatus", eligibleTabStatus)
                .addObject("pendingTabStatus", pendingTabStatus)
                .addObject("recentlyLapsedTabStatus", recentlyLapsedTabStatus)
                .addObject("allTabStatus", allTabStatus);
    }

    @RequestMapping(value = "/member/{number}", method = RequestMethod.GET)
    public ModelAndView memberDetails(@PathVariable Long number) {
        Member member = memberService.findMemberByMembershipNumber(number);
        MemberViewBean memberViewBean = member.toViewBean();
        ;
        memberViewBean.setLastPayment(paymentService.findLatestLotteryPayment(member));
        return modelAndView(MEMBER_PAGE).addObject("member", memberViewBean);
    }

    @RequestMapping(value = "/add-member", method = RequestMethod.GET)
    public ModelAndView navigateToAddMember() {
        MemberViewBean member = new MemberViewBean();
        return modelAndView(ADD_MEMBER_PAGE).addObject("member", member);
    }

    @RequestMapping(value = "/member", method = RequestMethod.POST)
    public ModelAndView addMember(@Valid @ModelAttribute("member") MemberViewBean memberViewBean, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.debug("Validation errors for member: ", memberViewBean);
            return navigateToAddMember();
        }

        Member newMember = memberService.saveMember(memberViewBean.toEntity());
        notificationService.logNewMemberAdded();

        return navigateToAddAddress(newMember.getId());
    }

    @RequestMapping(value = "/edit-member/{number}", method = RequestMethod.GET)
    public ModelAndView navigateToEditMember(@PathVariable Long number) {
        Member member = memberService.findMemberByMembershipNumber(number);
        return modelAndView(EDIT_MEMBER_PAGE).addObject("member", member);
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
        return modelAndView(ADD_ADDRESS_PAGE).addObject("address", addressViewBean);
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
        return modelAndView(EDIT_ADDRESS_PAGE).addObject("address", addressViewBean).addObject("member", member);
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

    @RequestMapping(value = "/close-lapsed-memberships", method = RequestMethod.POST)
    public ModelAndView closeLapsedMemberships() {
        int numberClosed = memberService.closeLapsedMemberships();
        notificationService.logMembershipsClosed(numberClosed);
        return members("current");
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView navigateToLogin() {
        return modelAndView(LOGIN_PAGE);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView navigateToLogout() {
        return modelAndView(LOGOUT_PAGE);
    }

    @RequestMapping(value = "/reports", method = RequestMethod.GET)
    public ModelAndView navigateToReports() {
        return modelAndView("reports");
    }

    @RequestMapping(value = "/export-data", method = RequestMethod.GET)
    public ModelAndView navigateExportData() {
        return modelAndView(EXPORT_DATA_PAGE);
    }
}
