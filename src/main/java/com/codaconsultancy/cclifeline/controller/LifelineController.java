package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
public class LifelineController {

    @Autowired
    MemberRepository memberRepository;

    // inject via application.properties
    @Value("${welcome.message:test}")
    private String message = "Hello World";

    @RequestMapping("/")
    public String home(Map<String, Object> model) {
        long count = memberRepository.count();
        model.put("message", this.message);
        model.put("memberCount", count);
        List<Member> allMembers = memberRepository.findAll();
        model.put("members", allMembers);
        return "index";
    }

    @RequestMapping("/member/{number}")
    public String member(Map<String, Object> model, @PathVariable String number) {
        model.put("memberNumber", number);
        Long memberNumber = Long.parseLong(number);
        Member member = memberRepository.findByMembershipNumber(memberNumber);
        model.put("member", member);
        return "member";
    }

}
