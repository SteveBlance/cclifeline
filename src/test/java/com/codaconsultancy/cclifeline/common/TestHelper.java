package com.codaconsultancy.cclifeline.common;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.view.MemberViewBean;

import java.util.Calendar;

public class TestHelper {

    public static Member newMember(long membershipNumber, String forename, String surname, String email, String landlineNumber, String mobileNumber, String payerType, String membershipType, String comments, String status) {
        Member member = new Member();
        member.setMembershipNumber(membershipNumber);
        member.setForename(forename);
        member.setSurname(surname);
        member.setEmail(email);
        member.setLandlineNumber(landlineNumber);
        member.setMobileNumber(mobileNumber);
        member.setPayerType(payerType);
        member.setMembershipType(membershipType);
        member.setJoinDate(Calendar.getInstance().getTime());
        member.setComments(comments);
        member.setStatus(status);
        return member;
    }

    public static MemberViewBean newMemberViewBean(long membershipNumber, String forename, String surname, String email, String landlineNumber, String mobileNumber, String payerType, String membershipType, String comments, String status) {
        MemberViewBean memberViewBean = new MemberViewBean();
        memberViewBean.setMembershipNumber(membershipNumber);
        memberViewBean.setForename(forename);
        memberViewBean.setSurname(surname);
        memberViewBean.setEmail(email);
        memberViewBean.setLandlineNumber(landlineNumber);
        memberViewBean.setMobileNumber(mobileNumber);
        memberViewBean.setPayerType(payerType);
        memberViewBean.setMembershipType(membershipType);
        memberViewBean.setJoinDate(Calendar.getInstance().getTime());
        memberViewBean.setComments(comments);
        memberViewBean.setStatus(status);
        return memberViewBean;
    }
}
