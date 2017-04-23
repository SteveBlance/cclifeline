package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.repositories.MemberRepository;
import com.codaconsultancy.cclifeline.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

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

    @RequestMapping(value = "/member/{number}", method = RequestMethod.GET)
    public String memberDetails(Map<String, Object> model, @PathVariable Long number) {
        model.put("memberNumber", number);
        Member member = memberService.findMemberByMembershipNumber(number);
        model.put("member", member);
        return "member";
    }

    @RequestMapping(value = "/member", method = RequestMethod.POST)
    public ResponseEntity<?> addMember(@RequestBody Member member, UriComponentsBuilder ucBuilder) {

        memberService.saveMember(member);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/member/{number}").buildAndExpand(member.getMembershipNumber()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

}
