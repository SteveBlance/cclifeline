package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> findAllMembers() {
        List<Member> members = memberRepository.findAll();
        return members;
    }

    public Long countAllMembers() {
        return memberRepository.count();
    }

    public Member findMemberByMembershipNumber(Long memberNumber) {
        return memberRepository.findByMembershipNumber(memberNumber);
    }

    public Member saveMember(Member member) {
        Long nextMembershipNumber = memberRepository.nextMembershipNumber();
        member.setMembershipNumber(nextMembershipNumber);
        return memberRepository.save(member);
    }

    public Member updateMember(Member member) {
        return memberRepository.save(member);
    }
}
