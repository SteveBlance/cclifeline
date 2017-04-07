package com.codaconsultancy.cclifeline.common;

import com.codaconsultancy.cclifeline.domain.Member;

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
}
