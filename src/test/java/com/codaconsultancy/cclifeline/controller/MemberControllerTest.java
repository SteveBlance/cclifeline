package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.common.TestHelper;
import com.codaconsultancy.cclifeline.domain.Address;
import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.domain.Notification;
import com.codaconsultancy.cclifeline.domain.SecuritySubject;
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

    @MockBean
    private LotteryDrawService lotteryDrawService;

    @Autowired
    MemberController memberController;

    @MockBean
    MemberService memberService;

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
    public void home() throws Exception {
        List<Notification> notifications = new ArrayList<>();
        when(memberService.countAllCurrentMembers()).thenReturn(22L);
        when(lotteryDrawService.countAllWinners()).thenReturn(18L);
        when(notificationService.fetchLatestNotifications()).thenReturn(notifications);

        ModelAndView response = memberController.home();

        verify(memberService, times(1)).countAllCurrentMembers();
        verify(lotteryDrawService, times(1)).countAllWinners();
        assertEquals(22L, response.getModel().get("memberCount"));
        assertEquals(18L, response.getModel().get("totalNumberOfWinners"));
        assertEquals("Bob", response.getModel().get("loggedInUser"));
        assertSame(notifications, response.getModel().get("notifications"));
        assertEquals("index", response.getViewName());
    }

    @Test
    public void members() throws Exception {
        List<MemberViewBean> members = new ArrayList<>();
        MemberViewBean member1 = TestHelper.newMemberViewBean(123L, "Bobby", "Smith", "bs@email.com", "01383 776655", "077665544", "Monthly", "Lifeline", "", "Open");
        MemberViewBean member2 = TestHelper.newMemberViewBean(124L, "Jane", "Wilkinson", "jw@email.com", "01383 414141", "077889900", "Monthly", "Lifeline", "", "Open");
        members.add(member1);
        members.add(member2);
        when(memberService.countAllCurrentMembers()).thenReturn(22L);
        when(memberService.findAllMembers()).thenReturn(members);

        ModelAndView response = memberController.members("all");

        verify(memberService, times(1)).findAllMembers();

        assertEquals(2L, response.getModel().get("memberCount"));
        assertSame(members, response.getModel().get("members"));
        assertEquals("All members", response.getModel().get("title"));
        assertEquals("disabled", response.getModel().get("allTabStatus"));
        assertEquals("members", response.getViewName());
    }

    @Test
    public void getMemberDetails() throws Exception {
        long memberNumber = 1234L;
        Member member1234 = TestHelper.newMember(memberNumber, "Bobby", "Smith", "bs@email.com", "01383 776655", "077665544", "Monthly", "Lifeline", "", "Open");
        when(memberService.findMemberByMembershipNumber(memberNumber)).thenReturn(member1234);
        ArgumentCaptor<MemberViewBean> memberViewBeanArgumentCaptor = ArgumentCaptor.forClass(MemberViewBean.class);
        when(memberService.isEligibleForDraw(memberViewBeanArgumentCaptor.capture())).thenReturn(true);

        ModelAndView response = memberController.memberDetails(memberNumber);

        verify(memberService, times(1)).isEligibleForDraw(memberViewBeanArgumentCaptor.capture());
        verify(memberService, times(1)).findMemberByMembershipNumber(1234L);
        assertEquals(1234L, memberViewBeanArgumentCaptor.getValue().getMembershipNumber().longValue());
        assertEquals("Bobby", ((MemberViewBean) response.getModel().get("member")).getForename());
        assertTrue(((MemberViewBean) response.getModel().get("member")).isEligibleForDraw());
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
    public void navigateToReports() {
        assertEquals("reports", memberController.navigateToReports().getViewName());
    }

    @Test
    public void navigateExportData() {
        assertEquals("export-data", memberController.navigateExportData().getViewName());
    }

}