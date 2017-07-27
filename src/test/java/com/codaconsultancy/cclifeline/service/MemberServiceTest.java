package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.common.TestHelper;
import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.repositories.MemberRepository;
import com.codaconsultancy.cclifeline.repositories.PaymentRepository;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@EntityScan("com.codaconsultancy.cclifeline.domain")
@SpringBootTest(classes = MemberService.class)
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    PaymentRepository paymentRepository;

    @Test
    public void findAllMembers() {
        List<Member> members = new ArrayList<>();
        Member member1 = TestHelper.newMember(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        Member member2 = TestHelper.newMember(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        Member member3 = TestHelper.newMember(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        members.add(member1);
        members.add(member2);
        members.add(member3);
        when(memberRepository.findAll()).thenReturn(members);

        List<Member> allMembers = memberService.findAllMembers();

        verify(memberRepository, times(1)).findAll();
        assertEquals(3, allMembers.size());
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
        List<Member> members = new ArrayList<>();
        Member member1 = TestHelper.newMember(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        Member member2 = TestHelper.newMember(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        members.add(member1);
        members.add(member2);
        when(memberRepository.findAllByStatusOrderBySurnameAscForenameAsc("Open")).thenReturn(members);

        List<Member> allMembers = memberService.findCurrentMembers();
        verify(memberRepository, times(1)).findAllByStatusOrderBySurnameAscForenameAsc("Open");
        assertEquals(2, allMembers.size());
    }

    @Test
    public void findFormerMembers() {
        List<Member> cancelledMembers = new ArrayList<>();
        Member member1 = TestHelper.newMember(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        Member member2 = TestHelper.newMember(1237L, "Bob", "Zippo", "bz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        cancelledMembers.add(member1);
        cancelledMembers.add(member2);
        List<Member> closedMembers = new ArrayList<>();
        Member member3 = TestHelper.newMember(1235L, "Dave", "Zippo", "dz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        closedMembers.add(member3);
        when(memberRepository.findAllByStatusOrderBySurnameAscForenameAsc("Cancelled")).thenReturn(cancelledMembers);
        when(memberRepository.findAllByStatusOrderBySurnameAscForenameAsc("Closed")).thenReturn(closedMembers);

        List<Member> formerMembers = memberService.findFormerMembers();

        verify(memberRepository, times(1)).findAllByStatusOrderBySurnameAscForenameAsc("Cancelled");
        verify(memberRepository, times(1)).findAllByStatusOrderBySurnameAscForenameAsc("Closed");
        assertEquals(3, formerMembers.size());
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
        assertEquals("Open", newMember.getStatus());
        assertEquals(DateTime.now().getDayOfYear(), new DateTime(newMember.getJoinDate()).getDayOfYear());

    }

    @Test
    public void updateMember() {
        Member member = TestHelper.newMember(23L, "Billy", "Whiz", "bw@email.com", "0131991188", null, "Annual", "Lifeline", "New member", "Open");

        when(memberRepository.save(member)).thenReturn(member);

        Member newMember = memberService.updateMember(member);

        verify(memberRepository, never()).nextMembershipNumber();
        verify(memberRepository, times(1)).save(member);
        assertEquals(23L, newMember.getMembershipNumber().longValue());
        assertEquals("Billy", newMember.getForename());
        assertEquals("Whiz", newMember.getSurname());

    }

    @Test
    public void fetchLifelineMemberDrawEntries() {
        Member lifelineMember1 = TestHelper.newMember(23L, "Billy", "Whiz", "bw@email.com", "0131991188", null, "Monthly", "Lifeline", "New member", "Open");
        lifelineMember1.setId(66L);
        Member lifelineMember2 = TestHelper.newMember(24L, "Jimmy", "Whiz", "jw@email.com", "0131991188", null, "Quarterly", "Lifeline", "New member", "Open");
        lifelineMember2.setId(404L);
        Member underpaidMember = TestHelper.newMember(97L, "Jen", "Underwood", "under@email.com", "0131991188", null, "Annual", "Lifeline", "New member", "Open");
        underpaidMember.setId(999L);
        Member closedLifelineMember = TestHelper.newMember(99L, "Boris", "Loser", "bl@email.com", "0131991188", null, "Annual", "Lifeline", "New member", "Closed");
        Member cancelledLifelineMember = TestHelper.newMember(98L, "Theresa", "Left", "tl@email.com", "0131991188", null, "Annual", "Lifeline", "New member", "Cancelled");

        List<Member> allMembers = new ArrayList<>();
        allMembers.add(lifelineMember1);
        allMembers.add(lifelineMember2);
        allMembers.add(underpaidMember);
        allMembers.add(closedLifelineMember);
        allMembers.add(cancelledLifelineMember);
        when(memberRepository.findAll()).thenReturn(allMembers);
        when(memberRepository.findAllByStatusOrderBySurnameAscForenameAsc("Open")).thenReturn(allMembers);
        ArgumentCaptor<Date> monthlyLastPaymentCutoffDate = ArgumentCaptor.forClass(Date.class);
        ArgumentCaptor<Date> quarterlyLastPaymentCutoffDate = ArgumentCaptor.forClass(Date.class);
        ArgumentCaptor<Date> annualLastPaymentCutoffDate = ArgumentCaptor.forClass(Date.class);
        when(paymentRepository.getTotalPaymentSince(monthlyLastPaymentCutoffDate.capture(), eq(66L))).thenReturn(20.00D);
        when(paymentRepository.getTotalPaymentSince(quarterlyLastPaymentCutoffDate.capture(), eq(404L))).thenReturn(60.00D);
        when(paymentRepository.getTotalPaymentSince(annualLastPaymentCutoffDate.capture(), eq(999L))).thenReturn(239.00D);

        List<Member> memberDrawEntries = memberService.fetchMemberDrawEntries();

        DateTime now = new DateTime();
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime expectedLastPaymentForMonthly = now.minus(Period.months(1)).minus(Period.days(30));
        DateTime expectedLastPaymentForQuarterly = now.minus(Period.months(3)).minus(Period.days(30));
        DateTime expectedLastPaymentForAnnual = now.minus(Period.years(1)).minus(Period.days(30));
        assertEquals(expectedLastPaymentForMonthly.toString(dtf), new DateTime(monthlyLastPaymentCutoffDate.getValue()).toString(dtf));
        assertEquals(expectedLastPaymentForQuarterly.toString(dtf), new DateTime(quarterlyLastPaymentCutoffDate.getValue()).toString(dtf));
        assertEquals(expectedLastPaymentForAnnual.toString(dtf), new DateTime(annualLastPaymentCutoffDate.getValue()).toString(dtf));
        verify(paymentRepository, times(3)).getTotalPaymentSince(any(Date.class), any(Long.class));
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
        Member legacyMember1 = TestHelper.newMember(23L, "David", "Jones", "bw@email.com", "0131991188", null, "Monthly", "Legacy", "New member", "Open");
        legacyMember1.setId(87L);
        Member premiumLegacyMember2 = TestHelper.newMember(24L, "Jimmy", "Jones", "jw@email.com", "0131991188", null, "Quarterly", "Premium Legacy", "New member", "Open");
        premiumLegacyMember2.setId(4L);
        Member legacyMember3 = TestHelper.newMember(24L, "Ann", "Smith", "smithy@email.com", "0131991188", null, "Annual", "Legacy", "New member", "Open");
        legacyMember3.setId(89L);
        Member closedLegacyMember = TestHelper.newMember(99L, "Boris", "Loser", "bl@email.com", "0131991188", null, "Annual", "Legacy", "New member", "Closed");
        Member cancelledLegacyMember = TestHelper.newMember(98L, "Theresa", "Left", "tl@email.com", "0131991188", null, "Annual", "Legacy", "New member", "Cancelled");

        List<Member> allMembers = new ArrayList<>();
        allMembers.add(legacyMember1);
        allMembers.add(premiumLegacyMember2);
        allMembers.add(legacyMember3);
        allMembers.add(closedLegacyMember);
        allMembers.add(cancelledLegacyMember);
        when(memberRepository.findAll()).thenReturn(allMembers);
        ArgumentCaptor<Date> monthlyLastPaymentCutoffDate = ArgumentCaptor.forClass(Date.class);
        ArgumentCaptor<Date> quarterlyLastPaymentCutoffDate = ArgumentCaptor.forClass(Date.class);
        ArgumentCaptor<Date> annualLastPaymentCutoffDate = ArgumentCaptor.forClass(Date.class);
        when(paymentRepository.getTotalPaymentSince(monthlyLastPaymentCutoffDate.capture(), eq(87L))).thenReturn(8.66D);
        when(paymentRepository.getTotalPaymentSince(quarterlyLastPaymentCutoffDate.capture(), eq(4L))).thenReturn(26.00D);
        when(paymentRepository.getTotalPaymentSince(annualLastPaymentCutoffDate.capture(), eq(89L))).thenReturn(103.00D);

        List<Member> memberDrawEntries = memberService.fetchMemberDrawEntries();

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


}