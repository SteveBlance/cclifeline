package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.common.TestHelper;
import com.codaconsultancy.cclifeline.domain.Member;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Member.class)
public class MemberRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member member1;
    private Member member2;
    private Member member3;
    private Member member4;

    @Before
    public void setUp() throws Exception {
        member1 = TestHelper.newMember(1818L, "Frank", "Jones", "frank@email.com", "013177766655", "07712312312", "Monthly", "Lifeline", "New member", "Open");
        member2 = TestHelper.newMember(1819L, "Jane", "Smith", "jsmith@email.com", "01383772233", "07777777766", "Monthly", "Lifeline", "Was a legacy member", "Open");
        member3 = TestHelper.newMember(1820L, "Bill", "Wilson", "billy@email.com", "01383999999", null, "Annual", "Legacy", "Old member", "Open");
        member4 = TestHelper.newMember(1821L, "Jimmy", "Jimmieson", "jj@email.com", "01383000111", null, "Monthly", "Legacy", "Old member", "Closed");
        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        entityManager.persist(member4);
    }


    @After
    public void tearDown() throws Exception {
        entityManager.remove(member1);
        entityManager.remove(member2);
        entityManager.remove(member3);
        entityManager.remove(member4);
    }

    @Test
    public void count() throws Exception {
        long membersInDatabase = memberRepository.count();
        Assert.assertEquals(4L, membersInDatabase);
    }

    @Test
    public void findByMembershipNumber() {
        Member foundMember = memberRepository.findByMembershipNumber(1818L);
        Assert.assertEquals("Frank", foundMember.getForename());
        Assert.assertEquals("Jones", foundMember.getSurname());
        Assert.assertEquals("frank@email.com", foundMember.getEmail());

        foundMember = memberRepository.findByMembershipNumber(1819L);
        Assert.assertEquals("Jane", foundMember.getForename());
        Assert.assertEquals("Smith", foundMember.getSurname());
        Assert.assertEquals("jsmith@email.com", foundMember.getEmail());

        foundMember = memberRepository.findByMembershipNumber(9900L);
        Assert.assertNull(foundMember);

    }

}