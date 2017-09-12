package com.codaconsultancy.cclifeline.common;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.repositories.BaseTest;
import com.codaconsultancy.cclifeline.repositories.MemberRepository;
import com.codaconsultancy.cclifeline.view.MemberViewBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest(classes = Member.class)
public class MemberRepositoryTest extends BaseTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member member1;
    private Member member2;
    private Member member3;
    private Member member4;
    private Member member5;
    private Member member6;

    @Before
    public void setUp() throws Exception {
        member1 = TestHelper.newMember(1818L, "Frank", "Jones", "frank@email.com", "013177766655", "07712312312", "Monthly", "Lifeline", "New member", "Open");
        member2 = TestHelper.newMember(1819L, "Jane", "Jones", "jsmith@email.com", "01383772233", "07777777766", "Monthly", "Lifeline", "Was a legacy member", "Open");
        member3 = TestHelper.newMember(1820L, "Bill", "Wilson", "billy@email.com", "01383999999", null, "Annual", "Legacy", "Old member", "Open");
        member4 = TestHelper.newMember(1821L, "Jimmy", "Jimmieson", "jj@email.com", "01383000111", null, "Monthly", "Legacy", "Old member", "Closed");
        member5 = TestHelper.newMember(1822L, "Bart", "Simpson", "bart@email.com", "01383000911", null, "Monthly", "Lifeline", "Old member", "Cancelled");
        member6 = TestHelper.newMember(1823L, "Ace", "Simmonds", "ace@email.com", "01383000921", null, "Monthly", "Lifeline", "New member", "TBC");
        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        entityManager.persist(member4);
        entityManager.persist(member5);
        entityManager.persist(member6);
    }


    @After
    public void tearDown() throws Exception {
        entityManager.remove(member1);
        entityManager.remove(member2);
        entityManager.remove(member3);
        entityManager.remove(member4);
        entityManager.remove(member5);
        entityManager.remove(member6);
    }

    @Test
    public void count() throws Exception {
        long membersInDatabase = memberRepository.count();
        assertEquals(6L, membersInDatabase);
    }

    @Test
    public void countByStatusOpen() throws Exception {
        long membersInDatabase = memberRepository.countByStatus("Open");
        assertEquals(3L, membersInDatabase);
    }

    @Test
    public void findByMembershipNumber() {
        Member foundMember = memberRepository.findByMembershipNumber(1818L);
        assertEquals("Frank", foundMember.getForename());
        assertEquals("Jones", foundMember.getSurname());
        assertEquals("frank@email.com", foundMember.getEmail());

        foundMember = memberRepository.findByMembershipNumber(1819L);
        assertEquals("Jane", foundMember.getForename());
        assertEquals("Jones", foundMember.getSurname());
        assertEquals("jsmith@email.com", foundMember.getEmail());

        foundMember = memberRepository.findByMembershipNumber(9900L);
        assertNull(foundMember);
    }

    @Test
    public void nextMembershipNumber() {
        Long nextMemberShipNumber = memberRepository.nextMembershipNumber();
        Long expectedNextNumber = member6.getMembershipNumber() + 1L;
        assertEquals(expectedNextNumber, nextMemberShipNumber);

    }

    @Test
    public void findAllWithStatusOpenBySurname() {
        List<Member> foundMembers = memberRepository.findAllBySurnameIgnoreCaseAndStatusOrderByForename("JONES", "Open");
        assertEquals(2, foundMembers.size());
    }

    @Test
    public void findAllMembers() {
        List<MemberViewBean> foundMembers = memberRepository.findAllMembers();
        assertEquals(6, foundMembers.size());
        assertEquals("Frank", foundMembers.get(0).getForename());
        assertEquals("Open", foundMembers.get(0).getStatus());
        assertEquals("Jane", foundMembers.get(1).getForename());
        assertEquals("Open", foundMembers.get(1).getStatus());
        assertEquals("Bill", foundMembers.get(2).getForename());
        assertEquals("Open", foundMembers.get(2).getStatus());
        assertEquals("Jimmy", foundMembers.get(3).getForename());
        assertEquals("Closed", foundMembers.get(3).getStatus());
        assertEquals("Bart", foundMembers.get(4).getForename());
        assertEquals("Cancelled", foundMembers.get(4).getStatus());
        assertEquals("Ace", foundMembers.get(5).getForename());
        assertEquals("TBC", foundMembers.get(5).getStatus());
    }

    @Test
    public void findCurrentMembers() {
        List<MemberViewBean> foundMembers = memberRepository.findCurrentMembers();
        assertEquals(3, foundMembers.size());
        assertEquals("Frank", foundMembers.get(0).getForename());
        assertEquals("Jane", foundMembers.get(1).getForename());
        assertEquals("Bill", foundMembers.get(2).getForename());
        assertEquals("Open", foundMembers.get(0).getStatus());
        assertEquals("Open", foundMembers.get(1).getStatus());
        assertEquals("Open", foundMembers.get(2).getStatus());
    }

    @Test
    public void findTBCMembers() {
        List<MemberViewBean> foundMembers = memberRepository.findTBCMembers();
        assertEquals(1, foundMembers.size());
        assertEquals("Ace", foundMembers.get(0).getForename());
    }

    @Test
    public void findFormerMembers() {
        List<MemberViewBean> foundMembers = memberRepository.findFormerMembers();
        assertEquals(2, foundMembers.size());
        assertEquals("Jimmy", foundMembers.get(0).getForename());
        assertEquals("Bart", foundMembers.get(1).getForename());
        assertEquals("Closed", foundMembers.get(0).getStatus());
        assertEquals("Cancelled", foundMembers.get(1).getStatus());

    }

    @Test
    public void findAllOrderBySurname() {
        List<Member> foundMembers = memberRepository.findAllByOrderBySurnameAscForenameAsc();
        assertEquals(6, foundMembers.size());
        assertEquals("Jimmieson", foundMembers.get(0).getSurname());
    }
}