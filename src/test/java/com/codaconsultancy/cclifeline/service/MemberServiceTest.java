package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.common.TestHelper;
import com.codaconsultancy.cclifeline.domain.Configuration;
import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.repositories.MemberRepository;
import com.codaconsultancy.cclifeline.repositories.PaymentRepository;
import com.codaconsultancy.cclifeline.view.MemberViewBean;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.codaconsultancy.cclifeline.service.LifelineService.ELIGIBILITY_REFRESH_REQUIRED;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MemberService.class)
public class MemberServiceTest extends LifelineServiceTest {

    @Autowired
    private MemberService memberService;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    PaymentRepository paymentRepository;

    @Test
    public void findAllMembers() {
        List<MemberViewBean> members = new ArrayList<>();
        MemberViewBean member1 = TestHelper.newMemberViewBean(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        member1.setId(69L);
        MemberViewBean member2 = TestHelper.newMemberViewBean(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        member2.setId(88L);
        MemberViewBean member3 = TestHelper.newMemberViewBean(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        member3.setId(919L);
        members.add(member1);
        members.add(member2);
        members.add(member3);
        when(memberRepository.findAllMembers()).thenReturn(members);
        when(paymentRepository.getTotalLotteryPaymentSince(any(Date.class), eq(member1.getId()))).thenReturn(30.00D);
        when(paymentRepository.getTotalLotteryPaymentSince(any(Date.class), eq(member2.getId()))).thenReturn(20.00D);
        when(paymentRepository.getTotalLotteryPaymentSince(any(Date.class), eq(member3.getId()))).thenReturn(19.00D);

        List<MemberViewBean> allMembers = memberService.findAllMembers();

        verify(memberRepository, times(1)).findAllMembers();
        verify(paymentRepository, times(3)).getTotalLotteryPaymentSince(any(Date.class), any(Long.class));
        assertEquals(3, allMembers.size());
        assertEquals(member1.getId(), allMembers.get(0).getId());
        assertTrue(allMembers.get(0).isEligibleForDraw());
        assertEquals(member2.getId(), allMembers.get(1).getId());
        assertTrue(allMembers.get(1).isEligibleForDraw());
        assertEquals(member3.getId(), allMembers.get(2).getId());
        assertFalse(allMembers.get(2).isEligibleForDraw());
    }

    @Test
    public void findAllMembersOrderedBySurname() {
        List<Member> members = new ArrayList<>();
        Member member1 = TestHelper.newMember(1234L, "Frank", "Alpha", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        Member member2 = TestHelper.newMember(1234L, "Frank", "Beta", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        Member member3 = TestHelper.newMember(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        members.add(member1);
        members.add(member2);
        members.add(member3);
        when(memberRepository.findAllByOrderBySurnameAscForenameAsc()).thenReturn(members);

        List<Member> allMembers = memberService.findAllMembersOrderedBySurname();

        verify(memberRepository, times(1)).findAllByOrderBySurnameAscForenameAsc();
        assertEquals(3, allMembers.size());
    }

    @Test
    public void findCurrentMembers() {
        List<MemberViewBean> members = new ArrayList<>();
        MemberViewBean member1 = TestHelper.newMemberViewBean(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        member1.setId(81L);
        MemberViewBean member2 = TestHelper.newMemberViewBean(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        member2.setId(50L);
        members.add(member1);
        members.add(member2);
        when(memberRepository.findCurrentMembers()).thenReturn(members);
        when(paymentRepository.getTotalLotteryPaymentSince(any(Date.class), eq(member1.getId()))).thenReturn(20.00D);
        when(paymentRepository.getTotalLotteryPaymentSince(any(Date.class), eq(member2.getId()))).thenReturn(19.99D);

        List<MemberViewBean> currentMembers = memberService.findCurrentMembers();

        verify(memberRepository, times(1)).findCurrentMembers();
        verify(paymentRepository, times(2)).getTotalLotteryPaymentSince(any(Date.class), any(Long.class));
        assertEquals(2, currentMembers.size());
        assertEquals(member1.getId(), currentMembers.get(0).getId());
        assertTrue(currentMembers.get(0).isEligibleForDraw());
        assertEquals(member2.getId(), currentMembers.get(1).getId());
        assertFalse(currentMembers.get(1).isEligibleForDraw());
    }

    @Test
    public void findFormerMembers() {
        List<MemberViewBean> formerMembers = new ArrayList<>();
        MemberViewBean member1 = TestHelper.newMemberViewBean(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        MemberViewBean member2 = TestHelper.newMemberViewBean(1237L, "Bob", "Zippo", "bz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        MemberViewBean member3 = TestHelper.newMemberViewBean(1235L, "Dave", "Zippo", "dz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        formerMembers.add(member1);
        formerMembers.add(member2);
        formerMembers.add(member3);
        when(memberRepository.findFormerMembers()).thenReturn(formerMembers);

        List<MemberViewBean> foundFormerMembers = memberService.findFormerMembers();

        verify(memberRepository, times(1)).findFormerMembers();
        verify(paymentRepository, never()).getTotalLotteryPaymentSince(any(Date.class), any(Long.class));

        assertEquals(3, foundFormerMembers.size());
        assertFalse(foundFormerMembers.get(0).isEligibleForDraw());
        assertFalse(foundFormerMembers.get(1).isEligibleForDraw());
    }

    @Test
    public void countCurrentMembers() {
        when(memberRepository.countByStatus("Open")).thenReturn(87L);

        Long memberCount = memberService.countAllCurrentMembers();

        verify(memberRepository, times(1)).countByStatus("Open");
        assertEquals(87L, memberCount.longValue());
    }

    @Test
    public void findMemberByMembershipNumber_success() {
        Member member = TestHelper.newMember(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");

        when(memberRepository.findByMembershipNumber(1234L)).thenReturn(member);

        Member foundMember = memberService.findMemberByMembershipNumber(1234L);

        verify(memberRepository, times(1)).findByMembershipNumber(1234L);
        assertEquals("fz@email.com", foundMember.getEmail());
    }

    @Test
    public void findMemberById_success() {
        Member member = TestHelper.newMember(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        member.setId(564L);
        when(memberRepository.findOne(564L)).thenReturn(member);

        Member foundMember = memberService.findMemberById(564L);

        verify(memberRepository, times(1)).findOne(564L);
        assertEquals("fz@email.com", foundMember.getEmail());
    }

    @Test
    public void saveMember() {
        Member member = TestHelper.newMember(0L, "Billy", "Whiz", "bw@email.com", "0131991188", null, "Annual", "Lifeline", "New member", null);

        when(memberRepository.nextMembershipNumber()).thenReturn(2566L);
        when(memberRepository.save(member)).thenReturn(member);

        Member newMember = memberService.saveMember(member);

        verify(memberRepository, times(1)).nextMembershipNumber();
        verify(memberRepository, times(1)).save(member);
        assertEquals(2566L, newMember.getMembershipNumber().longValue());
        assertEquals("Billy", newMember.getForename());
        assertEquals("Whiz", newMember.getSurname());
        assertEquals("TBC", newMember.getStatus());
        assertEquals(DateTime.now().getDayOfYear(), new DateTime(newMember.getJoinDate()).getDayOfYear());

    }

    @Test
    public void updateMember() {
        Member member = TestHelper.newMember(23L, "Billy", "Whiz", "bw@email.com", "0131991188", null, "Annual", "Lifeline", "New member", "Open");

        when(memberRepository.save(member)).thenReturn(member);
        Configuration refreshRequired = new Configuration();
        refreshRequired.setBooleanValue(false);
        when(configurationRepository.findByName(ELIGIBILITY_REFRESH_REQUIRED)).thenReturn(refreshRequired);

        Member newMember = memberService.updateMember(member);

        verify(memberRepository, never()).nextMembershipNumber();
        verify(memberRepository, times(1)).save(member);
        assertEquals(23L, newMember.getMembershipNumber().longValue());
        assertEquals("Billy", newMember.getForename());
        assertEquals("Whiz", newMember.getSurname());

    }

    @Test
    public void fetchLifelineMemberDrawEntries() {
        MemberViewBean lifelineMember1 = TestHelper.newMemberViewBean(23L, "Billy", "Whiz", "bw@email.com", "0131991188", null, "Monthly", "Lifeline", "New member", "Open");
        lifelineMember1.setId(66L);
        MemberViewBean lifelineMember2 = TestHelper.newMemberViewBean(24L, "Jimmy", "Whiz", "jw@email.com", "0131991188", null, "Quarterly", "Lifeline", "New member", "Open");
        lifelineMember2.setId(404L);
        MemberViewBean underpaidMember = TestHelper.newMemberViewBean(97L, "Jen", "Underwood", "under@email.com", "0131991188", null, "Annual", "Lifeline", "New member", "Open");
        underpaidMember.setId(999L);
        MemberViewBean closedLifelineMember = TestHelper.newMemberViewBean(99L, "Boris", "Loser", "bl@email.com", "0131991188", null, "Annual", "Lifeline", "New member", "Closed");
        MemberViewBean cancelledLifelineMember = TestHelper.newMemberViewBean(98L, "Theresa", "Left", "tl@email.com", "0131991188", null, "Annual", "Lifeline", "New member", "Cancelled");

        List<MemberViewBean> allMembers = new ArrayList<>();
        allMembers.add(lifelineMember1);
        allMembers.add(lifelineMember2);
        allMembers.add(underpaidMember);
        allMembers.add(closedLifelineMember);
        allMembers.add(cancelledLifelineMember);
        when(memberRepository.findAllMembers()).thenReturn(allMembers);
        when(memberRepository.findCurrentMembers()).thenReturn(allMembers);
        ArgumentCaptor<Date> monthlyLastPaymentCutoffDate = ArgumentCaptor.forClass(Date.class);
        ArgumentCaptor<Date> quarterlyLastPaymentCutoffDate = ArgumentCaptor.forClass(Date.class);
        ArgumentCaptor<Date> annualLastPaymentCutoffDate = ArgumentCaptor.forClass(Date.class);
        when(paymentRepository.getTotalLotteryPaymentSince(monthlyLastPaymentCutoffDate.capture(), eq(66L))).thenReturn(20.00D);
        when(paymentRepository.getTotalLotteryPaymentSince(quarterlyLastPaymentCutoffDate.capture(), eq(404L))).thenReturn(60.00D);
        when(paymentRepository.getTotalLotteryPaymentSince(annualLastPaymentCutoffDate.capture(), eq(999L))).thenReturn(239.00D);

        List<MemberViewBean> memberDrawEntries = memberService.fetchMemberDrawEntries();

        DateTime now = new DateTime();
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime expectedLastPaymentForMonthly = now.minus(Period.months(1)).minus(Period.days(30));
        DateTime expectedLastPaymentForQuarterly = now.minus(Period.months(3)).minus(Period.days(30));
        DateTime expectedLastPaymentForAnnual = now.minus(Period.years(1)).minus(Period.days(30));
        assertEquals(expectedLastPaymentForMonthly.toString(dtf), new DateTime(monthlyLastPaymentCutoffDate.getValue()).toString(dtf));
        assertEquals(expectedLastPaymentForQuarterly.toString(dtf), new DateTime(quarterlyLastPaymentCutoffDate.getValue()).toString(dtf));
        assertEquals(expectedLastPaymentForAnnual.toString(dtf), new DateTime(annualLastPaymentCutoffDate.getValue()).toString(dtf));
        verify(paymentRepository, times(3)).getTotalLotteryPaymentSince(any(Date.class), any(Long.class));
        assertEquals(6, memberDrawEntries.size());
        assertTrue(memberDrawEntries.contains(lifelineMember1));
        assertTrue(memberDrawEntries.contains(lifelineMember2));
        assertFalse(memberDrawEntries.contains(closedLifelineMember));
        assertFalse(memberDrawEntries.contains(cancelledLifelineMember));

        assertEquals(2, memberService.findEligibleMembers().size());
        assertEquals(3, memberService.findIneligibleMembers().size());
    }

    @Test
    public void fetchLegacyMemberDrawEntries() {
        MemberViewBean legacyMember1 = TestHelper.newMemberViewBean(23L, "David", "Jones", "bw@email.com", "0131991188", null, "Monthly", "Legacy", "New member", "Open");
        legacyMember1.setId(87L);
        MemberViewBean premiumLegacyMember2 = TestHelper.newMemberViewBean(24L, "Jimmy", "Jones", "jw@email.com", "0131991188", null, "Quarterly", "Premium Legacy", "New member", "Open");
        premiumLegacyMember2.setId(4L);
        MemberViewBean legacyMember3 = TestHelper.newMemberViewBean(24L, "Ann", "Smith", "smithy@email.com", "0131991188", null, "Annual", "Legacy", "New member", "Open");
        legacyMember3.setId(89L);
        MemberViewBean closedLegacyMember = TestHelper.newMemberViewBean(99L, "Boris", "Loser", "bl@email.com", "0131991188", null, "Annual", "Legacy", "New member", "Closed");
        MemberViewBean cancelledLegacyMember = TestHelper.newMemberViewBean(98L, "Theresa", "Left", "tl@email.com", "0131991188", null, "Annual", "Legacy", "New member", "Cancelled");

        List<MemberViewBean> allMembers = new ArrayList<>();
        allMembers.add(legacyMember1);
        allMembers.add(premiumLegacyMember2);
        allMembers.add(legacyMember3);
        allMembers.add(closedLegacyMember);
        allMembers.add(cancelledLegacyMember);
        when(memberRepository.findAllMembers()).thenReturn(allMembers);
        ArgumentCaptor<Date> monthlyLastPaymentCutoffDate = ArgumentCaptor.forClass(Date.class);
        ArgumentCaptor<Date> quarterlyLastPaymentCutoffDate = ArgumentCaptor.forClass(Date.class);
        ArgumentCaptor<Date> annualLastPaymentCutoffDate = ArgumentCaptor.forClass(Date.class);
        when(paymentRepository.getTotalLotteryPaymentSince(monthlyLastPaymentCutoffDate.capture(), eq(87L))).thenReturn(8.66D);
        when(paymentRepository.getTotalLotteryPaymentSince(quarterlyLastPaymentCutoffDate.capture(), eq(4L))).thenReturn(26.00D);
        when(paymentRepository.getTotalLotteryPaymentSince(annualLastPaymentCutoffDate.capture(), eq(89L))).thenReturn(103.00D);

        List<MemberViewBean> memberDrawEntries = memberService.fetchMemberDrawEntries();

        DateTime now = new DateTime();
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime expectedLastPaymentForMonthly = now.minus(Period.months(1)).minus(Period.days(30));
        DateTime expectedLastPaymentForQuarterly = now.minus(Period.months(3)).minus(Period.days(30));
        DateTime expectedLastPaymentForAnnual = now.minus(Period.years(1)).minus(Period.days(30));
        assertEquals(expectedLastPaymentForMonthly.toString(dtf), new DateTime(monthlyLastPaymentCutoffDate.getValue()).toString(dtf));
        assertEquals(expectedLastPaymentForQuarterly.toString(dtf), new DateTime(quarterlyLastPaymentCutoffDate.getValue()).toString(dtf));
        assertEquals(expectedLastPaymentForAnnual.toString(dtf), new DateTime(annualLastPaymentCutoffDate.getValue()).toString(dtf));
        assertEquals(2, memberDrawEntries.size());
        assertTrue(memberDrawEntries.contains(legacyMember1));
        assertTrue(memberDrawEntries.contains(premiumLegacyMember2));
        assertFalse(memberDrawEntries.contains(closedLegacyMember));
        assertFalse(memberDrawEntries.contains(cancelledLegacyMember));
    }

    @Test
    public void getLastExpectedPaymentDate() {
        DateTime fromDate = new DateTime(2017, 5, 2, 0, 0);
        System.out.println(fromDate.toString("dd/MM/yyyy"));

        DateTime lastExpectedPaymentDate = memberService.getLastExpectedPaymentDate(fromDate, "Monthly");

        DateTime lastDate = new DateTime(2017, 4, 2, 0, 0);
        assertEquals(lastDate, lastExpectedPaymentDate);

        lastExpectedPaymentDate = memberService.getLastExpectedPaymentDate(fromDate, "Quarterly");

        lastDate = new DateTime(2017, 2, 2, 0, 0);
        assertEquals(lastDate, lastExpectedPaymentDate);

        lastExpectedPaymentDate = memberService.getLastExpectedPaymentDate(fromDate, "Annual");

        lastDate = new DateTime(2016, 5, 2, 0, 0);
        assertEquals(lastDate, lastExpectedPaymentDate);
    }

    @Test
    public void closeLapsedMemberships() {
        List<MemberViewBean> members = new ArrayList<>();
        MemberViewBean lapsedMember1 = TestHelper.newMemberViewBean(8888L, "Bob", "Lateman", "bl@email.com", null, null, "Monthly", "Lifeline", null, "Open");
        lapsedMember1.setId(1L);
        members.add(lapsedMember1);
        MemberViewBean lapsedMember2 = TestHelper.newMemberViewBean(8999L, "Sally", "Lateman", "sl@email.com", null, null, "Monthly", "Lifeline", null, "Open");
        lapsedMember2.setId(2L);
        members.add(lapsedMember2);
        MemberViewBean activeMember = TestHelper.newMemberViewBean(1111L, "Steve", "Timely", "steveT@email.com", null, null, "Monthly", "Lifeline", null, "Open");
        activeMember.setId(3L);
        members.add(activeMember);
        when(memberRepository.findCurrentMembers()).thenReturn(members);
        Member lapsed1 = lapsedMember1.toEntity();
        when(memberRepository.findOne(lapsedMember1.getId())).thenReturn(lapsed1);
        Member lapsed2 = lapsedMember2.toEntity();
        when(memberRepository.findOne(lapsedMember2.getId())).thenReturn(lapsed2);
        when(paymentRepository.getTotalLotteryPaymentSince(any(Date.class), eq(lapsedMember1.getId()))).thenReturn(19.99D);
        when(paymentRepository.getTotalLotteryPaymentSince(any(Date.class), eq(lapsedMember2.getId()))).thenReturn(0.00D);
        when(paymentRepository.getTotalLotteryPaymentSince(any(Date.class), eq(activeMember.getId()))).thenReturn(20.00D);
        Configuration refreshRequired = new Configuration();
        refreshRequired.setBooleanValue(false);
        when(configurationRepository.findByName(ELIGIBILITY_REFRESH_REQUIRED)).thenReturn(refreshRequired);
        assertEquals("Open", lapsedMember1.getStatus());
        assertEquals("Open", lapsedMember2.getStatus());
        assertEquals("Open", activeMember.getStatus());

        int closedAccountCount = memberService.closeLapsedMemberships();

        verify(memberRepository, times(2)).findOne(any(Long.class));
        verify(memberRepository, times(2)).save(any(Member.class));
        verify(memberRepository, times(1)).save(lapsed1);
        verify(memberRepository, times(1)).save(lapsed2);
        assertEquals("Closed", lapsed1.getStatus());
        assertEquals("Closed", lapsed2.getStatus());
        assertEquals(2, closedAccountCount);
    }


}