package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.common.TestHelper;
import com.codaconsultancy.cclifeline.domain.*;
import com.codaconsultancy.cclifeline.repositories.BaseTest;
import com.codaconsultancy.cclifeline.service.*;
import com.codaconsultancy.cclifeline.view.AddressViewBean;
import com.codaconsultancy.cclifeline.view.MemberViewBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@EnableJpaRepositories(basePackages = {"com.codaconsultancy.cclifeline.repositories"})
@SpringBootTest(classes = MemberController.class)
public class MemberControllerTest extends BaseTest {

    @Autowired
    MemberController memberController;

    @MockBean
    PaymentService paymentService;

    @MockBean
    AddressService addressService;

    @MockBean
    NotificationService notificationService;

    @Before
    public void setup() {
        when(securitySubjectService.findByUsername(any(String.class))).thenReturn(new SecuritySubject());
    }

    @Test
    public void testUserNotYetLoggedIn() {
        Authentication auth = new PreAuthenticatedAuthenticationToken("Bob", "password");
        auth.setAuthenticated(false);
        SecurityContextHolder.getContext().setAuthentication(auth);
        assertEquals("Bob", memberController.loggedInUser());
    }

    @Test
    public void navigateToHomePage() throws Exception {
        List<Notification> notifications = new ArrayList<>();
        LotteryDraw lastDraw = new LotteryDraw();
        when(memberService.countAllCurrentMembers()).thenReturn(22L);
        when(lotteryDrawService.countAllWinners()).thenReturn(18L);
        when(notificationService.fetchLatestNotifications()).thenReturn(notifications);
        when(lotteryDrawService.fetchLastDraw()).thenReturn(lastDraw);

        ModelAndView response = memberController.home();

        verify(memberService, times(1)).countAllCurrentMembers();
        verify(lotteryDrawService, times(1)).countAllWinners();
        verify(lotteryDrawService, times(1)).fetchLastDraw();
        assertEquals(22L, response.getModel().get("memberCount"));
        assertEquals(18L, response.getModel().get("totalNumberOfWinners"));
        assertEquals("Bob", response.getModel().get("loggedInUser"));
        assertSame(notifications, response.getModel().get("notifications"));
        assertSame(lastDraw, response.getModel().get("lastDraw"));
        assertEquals("index", response.getViewName());
    }

    @Test
    public void navigateToHomePage_redirectToForcedPasswordChange() throws Exception {
        SecuritySubject securitySubjectNeedingPasswordChanged = new SecuritySubject();
        securitySubjectNeedingPasswordChanged.setPasswordToBeChanged(true);

        when(securitySubjectService.findByUsername(any(String.class))).thenReturn(securitySubjectNeedingPasswordChanged);
        List<Notification> notifications = new ArrayList<>();
        when(memberService.countAllCurrentMembers()).thenReturn(22L);
        when(lotteryDrawService.countAllWinners()).thenReturn(18L);
        when(notificationService.fetchLatestNotifications()).thenReturn(notifications);

        ModelAndView response = memberController.home();

        verify(memberService, times(1)).countAllCurrentMembers();
        verify(lotteryDrawService, times(1)).countAllWinners();
        assertEquals(22L, response.getModel().get("memberCount"));
        assertEquals(18L, response.getModel().get("totalNumberOfWinners"));
        assertEquals("alert alert-info", response.getModel().get("alertClass"));
        assertEquals("Password must be between 8 and 100 characters and must contain uppercase characters, lowercase characters and numbers", response.getModel().get("alertMessage"));
        assertEquals("change-password", response.getViewName());
    }

    @Test
    public void navigateToMembers_all() throws Exception {
        List<MemberViewBean> members = getMockMembers();
        when(memberService.countAllCurrentMembers()).thenReturn(22L);
        when(memberService.findAllMembers()).thenReturn(members);

        ModelAndView response = memberController.members("all");

        verify(memberService, times(1)).findAllMembers();
        verify(memberService, times(1)).updateEligibilityStatuses();

        assertEquals(2L, response.getModel().get("memberCount"));
        assertSame(members, response.getModel().get("members"));
        assertEquals("All members", response.getModel().get("title"));
        assertEquals("disabled", response.getModel().get("allTabStatus"));
        assertEquals("enabled", response.getModel().get("currentTabStatus"));
        assertEquals("enabled", response.getModel().get("formerTabStatus"));
        assertEquals("enabled", response.getModel().get("eligibleTabStatus"));
        assertEquals("enabled", response.getModel().get("pendingTabStatus"));
        assertEquals("enabled", response.getModel().get("recentlyLapsedTabStatus"));
        assertEquals("members", response.getViewName());
    }

    @Test
    public void navigateToMembers_current() throws Exception {
        List<MemberViewBean> members = getMockMembers();
        when(memberService.countAllCurrentMembers()).thenReturn(2L);
        when(memberService.findCurrentMembers()).thenReturn(members);

        ModelAndView response = memberController.members("current");

        verify(memberService, times(1)).findCurrentMembers();
        verify(memberService, times(1)).updateEligibilityStatuses();

        assertEquals(2L, response.getModel().get("memberCount"));
        assertSame(members, response.getModel().get("members"));
        assertEquals("Current members", response.getModel().get("title"));
        assertEquals("disabled", response.getModel().get("currentTabStatus"));
        assertEquals("enabled", response.getModel().get("allTabStatus"));
        assertEquals("enabled", response.getModel().get("formerTabStatus"));
        assertEquals("enabled", response.getModel().get("eligibleTabStatus"));
        assertEquals("enabled", response.getModel().get("pendingTabStatus"));
        assertEquals("enabled", response.getModel().get("recentlyLapsedTabStatus"));
        assertEquals("members", response.getViewName());
    }

    @Test
    public void navigateToMembers_former() throws Exception {
        List<MemberViewBean> members = getMockMembers();
        when(memberService.countAllCurrentMembers()).thenReturn(2L);
        when(memberService.findFormerMembers()).thenReturn(members);

        ModelAndView response = memberController.members("former");

        verify(memberService, times(1)).findFormerMembers();
        verify(memberService, times(1)).updateEligibilityStatuses();

        assertEquals(2L, response.getModel().get("memberCount"));
        assertSame(members, response.getModel().get("members"));
        assertEquals("Former members", response.getModel().get("title"));
        assertEquals("disabled", response.getModel().get("formerTabStatus"));
        assertEquals("enabled", response.getModel().get("allTabStatus"));
        assertEquals("enabled", response.getModel().get("currentTabStatus"));
        assertEquals("enabled", response.getModel().get("eligibleTabStatus"));
        assertEquals("enabled", response.getModel().get("pendingTabStatus"));
        assertEquals("enabled", response.getModel().get("recentlyLapsedTabStatus"));
        assertEquals("members", response.getViewName());
    }

    @Test
    public void navigateToMembers_eligible() throws Exception {
        List<MemberViewBean> members = getMockMembers();
        when(memberService.countAllCurrentMembers()).thenReturn(2L);
        when(memberService.findEligibleMembers()).thenReturn(members);

        ModelAndView response = memberController.members("eligible");

        verify(memberService, times(1)).findEligibleMembers();
        verify(memberService, times(1)).updateEligibilityStatuses();

        assertEquals(2L, response.getModel().get("memberCount"));
        assertSame(members, response.getModel().get("members"));
        assertEquals("Eligible for draw", response.getModel().get("title"));
        assertEquals("disabled", response.getModel().get("eligibleTabStatus"));
        assertEquals("enabled", response.getModel().get("formerTabStatus"));
        assertEquals("enabled", response.getModel().get("allTabStatus"));
        assertEquals("enabled", response.getModel().get("currentTabStatus"));
        assertEquals("enabled", response.getModel().get("pendingTabStatus"));
        assertEquals("enabled", response.getModel().get("recentlyLapsedTabStatus"));
        assertEquals("members", response.getViewName());
    }

    @Test
    public void navigateToMembers_pending() throws Exception {
        List<MemberViewBean> members = getMockMembers();
        when(memberService.countAllCurrentMembers()).thenReturn(2L);
        when(memberService.findPendingMembers()).thenReturn(members);

        ModelAndView response = memberController.members("pending");

        verify(memberService, times(1)).findPendingMembers();
        verify(memberService, times(1)).updateEligibilityStatuses();

        assertEquals(2L, response.getModel().get("memberCount"));
        assertSame(members, response.getModel().get("members"));
        assertEquals("Membership not confirmed", response.getModel().get("title"));
        assertEquals("disabled", response.getModel().get("pendingTabStatus"));
        assertEquals("enabled", response.getModel().get("eligibleTabStatus"));
        assertEquals("enabled", response.getModel().get("formerTabStatus"));
        assertEquals("enabled", response.getModel().get("allTabStatus"));
        assertEquals("enabled", response.getModel().get("currentTabStatus"));
        assertEquals("enabled", response.getModel().get("recentlyLapsedTabStatus"));
        assertEquals("members", response.getViewName());
    }

    @Test
    public void navigateToMembers_lapsed() throws Exception {
        List<MemberViewBean> members = getMockMembers();
        when(memberService.countAllCurrentMembers()).thenReturn(2L);
        when(memberService.findRecentlyLapsedMembers()).thenReturn(members);

        ModelAndView response = memberController.members("recently-lapsed");

        verify(memberService, times(1)).findRecentlyLapsedMembers();
        verify(memberService, times(1)).updateEligibilityStatuses();

        assertEquals(2L, response.getModel().get("memberCount"));
        assertSame(members, response.getModel().get("members"));
        assertEquals("Recently lapsed members", response.getModel().get("title"));
        assertEquals("enabled", response.getModel().get("pendingTabStatus"));
        assertEquals("enabled", response.getModel().get("eligibleTabStatus"));
        assertEquals("enabled", response.getModel().get("formerTabStatus"));
        assertEquals("enabled", response.getModel().get("allTabStatus"));
        assertEquals("enabled", response.getModel().get("currentTabStatus"));
        assertEquals("disabled", response.getModel().get("recentlyLapsedTabStatus"));
        assertEquals("members", response.getViewName());
    }

    private List<MemberViewBean> getMockMembers() {
        List<MemberViewBean> members = new ArrayList<>();
        MemberViewBean member1 = TestHelper.newMemberViewBean(123L, "Bobby", "Smith", "bs@email.com", "01383 776655", "077665544", "Monthly", "Lifeline", "", "Open");
        MemberViewBean member2 = TestHelper.newMemberViewBean(124L, "Jane", "Wilkinson", "jw@email.com", "01383 414141", "077889900", "Monthly", "Lifeline", "", "Open");
        members.add(member1);
        members.add(member2);
        return members;
    }

    @Test
    public void getMemberDetails() throws Exception {
        long memberNumber = 1234L;
        Member member1234 = TestHelper.newMember(memberNumber, "Bobby", "Smith", "bs@email.com", "01383 776655", "077665544", "Monthly", "Lifeline", "", "Open");
        when(memberService.findMemberByMembershipNumber(memberNumber)).thenReturn(member1234);

        ModelAndView response = memberController.memberDetails(memberNumber);

        verify(memberService, times(1)).findMemberByMembershipNumber(1234L);
        assertEquals("Bobby", ((MemberViewBean) response.getModel().get("member")).getForename());
        assertEquals("member", response.getViewName());

    }

    @Test
    public void addMember_success() {

        MemberViewBean memberViewBean = TestHelper.newMemberViewBean(2L, "Bobby", "Smith", "bs@email.com", "01383 776655", "077665544", "Monthly", "Lifeline", "", "Open");
        Member member = memberViewBean.toEntity();
        when(memberService.saveMember(any(Member.class))).thenReturn(member);
        BindingResult bindingResult = getBindingResult("member");
        ModelAndView modelAndView = memberController.addMember(memberViewBean, bindingResult);

        verify(memberService, times(1)).saveMember(any(Member.class));

        assertEquals("add-address", modelAndView.getViewName());
    }

    @Test
    public void addMember_validationErrors() {

        MemberViewBean memberViewBean = TestHelper.newMemberViewBean(2L, "Bobby", "Smith", "bs@email.com", "01383 776655", "077665544", "Monthly", "Lifeline", "", "Open");
        Member member = memberViewBean.toEntity();
        BindingResult bindingResult = getBindingResult("member");
        bindingResult.addError(new ObjectError("surname", "Surname cannot be blank"));
        ModelAndView modelAndView = memberController.addMember(memberViewBean, bindingResult);

        verify(memberService, never()).saveMember(member);

        assertEquals("add-member", modelAndView.getViewName());
    }

    @Test
    public void navigateToAddMember() {
        ModelAndView response = memberController.navigateToAddMember();
        assertEquals("add-member", response.getViewName());
        assertTrue(response.getModel().get("member") instanceof MemberViewBean);
    }

    @Test
    public void navigateToAddAddress() {
        Member member = new Member();
        member.setId(964L);
        ModelAndView response = memberController.navigateToAddAddress(member.getId());

        assertEquals("add-address", response.getViewName());
        AddressViewBean address = (AddressViewBean) response.getModel().get("address");
        assertEquals(member.getId(), address.getMemberId());
        assertTrue(address.getIsActive());
    }

    @Test
    public void addAddress_success() {

        Member member = new Member();
        member.setId(888L);
        member.setMembershipNumber(3399L);
        AddressViewBean address = new AddressViewBean();
        address.setMemberId(member.getId());

        BindingResult bindingResult = getBindingResult("address");

        when(memberService.findMemberById(888L)).thenReturn(member);
        when(memberService.isEligibleForDraw(member.toViewBean())).thenReturn(true);
        when(memberService.findMemberByMembershipNumber(3399L)).thenReturn(member);
        ArgumentCaptor<Address> addressArgumentCaptor = ArgumentCaptor.forClass(Address.class);

        ModelAndView modelAndView = memberController.addAddress(address, bindingResult);

        verify(addressService, times(1)).saveAddress(addressArgumentCaptor.capture());
        verify(memberService, times(1)).findMemberById(888L);

        assertEquals("member", modelAndView.getViewName());
        assertEquals(888L, addressArgumentCaptor.getValue().getMember().getId().longValue());
    }

    @Test
    public void addAddress_validationErrors() {

        AddressViewBean address = new AddressViewBean();
        BindingResult bindingResult = getBindingResult("address");
        bindingResult.addError(new ObjectError("line 1", "line 1 cannot be blank"));
        ModelAndView modelAndView = memberController.addAddress(address, bindingResult);

        verify(addressService, never()).saveAddress(any(Address.class));

        assertEquals("add-address", modelAndView.getViewName());
    }

    @Test
    public void navigateToEditMember() {
        Member member1234 = TestHelper.newMember(1234L, "Bobby", "Smith", "bs@email.com", "01383 776655", "077665544", "Monthly", "Lifeline", "", "Open");
        when(memberService.findMemberByMembershipNumber(1234L)).thenReturn(member1234);

        ModelAndView response = memberController.navigateToEditMember(1234L);

        assertEquals("edit-member", response.getViewName());
        assertTrue(response.getModel().get("member") instanceof Member);
    }

    @Test
    public void navigateToEditAddress_success_memberWithAddress() {
        long memberId = 98L;
        Member member = TestHelper.newMember(1234L, "Bobby", "Smith", "bs@email.com", "01383 776655", "077665544", "Monthly", "Lifeline", "", "Open");
        member.setId(memberId);
        List<Address> addresses = new ArrayList<>();
        Address address = new Address();
        address.setAddressLine1("1 New Row");
        addresses.add(address);
        member.setAddresses(addresses);
        when(memberService.findMemberByMembershipNumber(1234L)).thenReturn(member);

        ModelAndView response = memberController.navigateToEditAddress(1234L);

        assertEquals("edit-address", response.getViewName());
        assertTrue(response.getModel().get("address") instanceof AddressViewBean);
        AddressViewBean foundAddress = (AddressViewBean) response.getModel().get("address");
        assertEquals("1 New Row", foundAddress.getAddressLine1());
        assertEquals(memberId, foundAddress.getMemberId().longValue());
        assertTrue(foundAddress.getIsActive());
    }

    @Test
    public void navigateToEditAddress_success_memberWithNoAddress() {
        long memberId = 98L;
        Member member = TestHelper.newMember(1234L, "Bobby", "Smith", "bs@email.com", "01383 776655", "077665544", "Monthly", "Lifeline", "", "Open");
        member.setId(memberId);
        when(memberService.findMemberByMembershipNumber(1234L)).thenReturn(member);

        ModelAndView response = memberController.navigateToEditAddress(1234L);

        assertEquals("edit-address", response.getViewName());
        assertTrue(response.getModel().get("address") instanceof AddressViewBean);
        AddressViewBean foundAddress = (AddressViewBean) response.getModel().get("address");
        assertNull(foundAddress.getAddressLine1());
        assertEquals(memberId, foundAddress.getMemberId().longValue());
        assertTrue(foundAddress.getIsActive());
    }

    @Test
    public void editMember_success() {

        Member member = TestHelper.newMember(2L, "Bobby", "Smith", "bs@email.com", "01383 776655", "077665544", "Monthly", "Lifeline", "", "Open");
        when(memberService.updateMember(any(Member.class))).thenReturn(member);
        when(memberService.findMemberByMembershipNumber(2L)).thenReturn(member);
        BindingResult bindingResult = getBindingResult("member");
        ModelAndView modelAndView = memberController.editMember(member, bindingResult);

        verify(memberService, times(1)).updateMember(any(Member.class));

        assertEquals("edit-address", modelAndView.getViewName());
    }

    @Test
    public void editMember_validationErrors() {

        Member member = TestHelper.newMember(2L, "Bobby", "Smith", "bs@email.com", "01383 776655", "077665544", "Monthly", "Lifeline", "", "Open");
        BindingResult bindingResult = getBindingResult("member");
        bindingResult.addError(new ObjectError("surname", "Surname cannot be blank"));
        ModelAndView modelAndView = memberController.editMember(member, bindingResult);

        verify(memberService, never()).updateMember(member);

        assertEquals("edit-member", modelAndView.getViewName());
    }

    @Test
    public void closeLapsedMemberships() {
        when(memberService.closeLapsedMemberships()).thenReturn(564);

        ModelAndView modelAndView = memberController.closeLapsedMemberships();

        verify(memberService, times(1)).closeLapsedMemberships();
        verify(notificationService, times(1)).logMembershipsClosed(564);
        assertEquals("members", modelAndView.getViewName());
    }

    @Test
    public void navigateExportData() {
        assertEquals("export-data", memberController.navigateExportData().getViewName());
    }

    @Test
    public void navigateToLogin() {
        assertEquals("login", memberController.navigateToLogin().getViewName());
    }

    @Test
    public void navigateToLogout() {
        assertEquals("logout", memberController.navigateToLogout().getViewName());
    }

}