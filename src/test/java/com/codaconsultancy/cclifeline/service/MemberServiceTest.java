package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.common.TestHelper;
import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.repositories.MemberRepository;
import com.codaconsultancy.cclifeline.repositories.PaymentRepository;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
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
    public void countMembers() {
        when(memberRepository.count()).thenReturn(87L);

        Long memberCount = memberService.countAllMembers();

        verify(memberRepository, times(1)).count();
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
        Member member = TestHelper.newMember(0L, "Billy", "Whiz", "bw@email.com", "0131991188", null, "Annual", "Lifeline", "New member", "Open");

        when(memberRepository.nextMembershipNumber()).thenReturn(2566L);
        when(memberRepository.save(member)).thenReturn(member);

        Member newMember = memberService.saveMember(member);

        verify(memberRepository, times(1)).nextMembershipNumber();
        verify(memberRepository, times(1)).save(member);
        assertEquals(2566L, newMember.getMembershipNumber().longValue());
        assertEquals("Billy", newMember.getForename());
        assertEquals("Whiz", newMember.getSurname());

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
        Member lifelineMember1 = TestHelper.newMember(23L, "Billy", "Whiz", "bw@email.com", "0131991188", null, "Annual", "Lifeline", "New member", "Open");
        Member lifelineMember2 = TestHelper.newMember(24L, "Jimmy", "Whiz", "jw@email.com", "0131991188", null, "Annual", "Lifeline", "New member", "Open");
        Member closedLifelineMember = TestHelper.newMember(99L, "Boris", "Loser", "bl@email.com", "0131991188", null, "Annual", "Lifeline", "New member", "Closed");
        Member cancelledLifelineMember = TestHelper.newMember(98L, "Theresa", "Left", "tl@email.com", "0131991188", null, "Annual", "Lifeline", "New member", "Cancelled");

        List<Member> allMembers = new ArrayList<>();
        allMembers.add(lifelineMember1);
        allMembers.add(lifelineMember2);
        allMembers.add(closedLifelineMember);
        allMembers.add(cancelledLifelineMember);
        when(memberRepository.findAll()).thenReturn(allMembers);

        List<Member> memberDrawEntries = memberService.fetchMemberDrawEntries();

        assertEquals(6, memberDrawEntries.size());
        assertTrue(memberDrawEntries.contains(lifelineMember1));
        assertTrue(memberDrawEntries.contains(lifelineMember2));
        assertFalse(memberDrawEntries.contains(closedLifelineMember));
        assertFalse(memberDrawEntries.contains(cancelledLifelineMember));
    }

    @Test
    public void fetchLegacyMemberDrawEntries() {
        Member legacyMember1 = TestHelper.newMember(23L, "David", "Jones", "bw@email.com", "0131991188", null, "Annual", "Legacy", "New member", "Open");
        Member premiumLegacyMember2 = TestHelper.newMember(24L, "Jimmy", "Jones", "jw@email.com", "0131991188", null, "Annual", "Premium Legacy", "New member", "Open");
        Member closedLegacyMember = TestHelper.newMember(99L, "Boris", "Loser", "bl@email.com", "0131991188", null, "Annual", "Legacy", "New member", "Closed");
        Member cancelledLegacyMember = TestHelper.newMember(98L, "Theresa", "Left", "tl@email.com", "0131991188", null, "Annual", "Legacy", "New member", "Cancelled");

        List<Member> allMembers = new ArrayList<>();
        allMembers.add(legacyMember1);
        allMembers.add(premiumLegacyMember2);
        allMembers.add(closedLegacyMember);
        allMembers.add(cancelledLegacyMember);
        when(memberRepository.findAll()).thenReturn(allMembers);

        List<Member> memberDrawEntries = memberService.fetchMemberDrawEntries();

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