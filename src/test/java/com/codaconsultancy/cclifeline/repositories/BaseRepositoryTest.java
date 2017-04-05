package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.domain.Member;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;

@RunWith(SpringRunner.class)
@DataJpaTest
@EntityScan("com.codaconsultancy.cclifeline.domain")
public abstract class BaseRepositoryTest {

    @Autowired
    protected TestEntityManager entityManager;

    protected Member newMember(long membershipNumber, String forename, String surname, String email, String landlineNumber, String mobileNumber, String payerType, String membershipType, String comments, String status) {
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
