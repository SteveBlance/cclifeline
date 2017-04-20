package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.repositories.MemberRepository;
import com.codaconsultancy.cclifeline.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
public class LifelineController {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @RequestMapping("/")
    public String home(Map<String, Object> model) {
        long count = memberService.countAllMembers();
        model.put("message", "Hello World");
        model.put("memberCount", count);
        List<Member> allMembers = memberService.findAllMembers();
        model.put("members", allMembers);
        return "index";
    }

    @RequestMapping("/member/{number}")
    public String member(Map<String, Object> model, @PathVariable String number) {
        model.put("memberNumber", number);
        Long memberNumber = Long.parseLong(number);
        Member member = memberService.findMemberByMembershipNumber(memberNumber);
        model.put("member", member);
        return "member";
    }

}
