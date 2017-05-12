package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.common.TestHelper;
import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.repositories.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@EntityScan("com.codaconsultancy.cclifeline.domain")
@SpringBootTest(classes = MemberService.class)
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @MockBean
    MemberRepository memberRepository;

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


}