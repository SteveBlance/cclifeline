package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.common.TestHelper;
import com.codaconsultancy.cclifeline.domain.Configuration;
import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.repositories.MemberRepository;
import com.codaconsultancy.cclifeline.repositories.PaymentRepository;
import com.codaconsultancy.cclifeline.view.MemberViewBean;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.codaconsultancy.cclifeline.service.LifelineService.ELIGIBILITY_REFRESH_REQUIRED;
import static com.codaconsultancy.cclifeline.service.LifelineService.LAST_ELIGIBILITY_REFRESH_DATE;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.ExpectedCount.once;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MemberService.class)
public class MemberServiceTest extends LifelineServiceTest {

    @Autowired
    private MemberService memberService;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    PaymentRepository paymentRepository;

    @Test
    public void findAllMembers() {
        List<MemberViewBean> members = new ArrayList<>();
        MemberViewBean member1 = TestHelper.newMemberViewBean(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        member1.setId(69L);
        member1.setEligibleForDrawStored(true);
        MemberViewBean member2 = TestHelper.newMemberViewBean(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        member2.setId(88L);
        member2.setEligibleForDrawStored(true);
        MemberViewBean member3 = TestHelper.newMemberViewBean(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        member3.setId(919L);
        member3.setEligibleForDrawStored(false);
        members.add(member1);
        members.add(member2);
        members.add(member3);
        when(memberRepository.findAllMembers()).thenReturn(members);

        List<MemberViewBean> allMembers = memberService.findAllMembers();

        verify(memberRepository, times(1)).findAllMembers();
        assertEquals(3, allMembers.size());
        assertEquals(member1.getId(), allMembers.get(0).getId());
        assertEquals(member2.getId(), allMembers.get(1).getId());
        assertEquals(member3.getId(), allMembers.get(2).getId());
    }

    @Test
    public void findAllMembersOrderedBySurname() {
        List<Member> members = new ArrayList<>();
        Member member1 = TestHelper.newMember(1234L, "Frank", "Alpha", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        Member member2 = TestHelper.newMember(1234L, "Frank", "Beta", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        Member member3 = TestHelper.newMember(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        members.add(member1);
        members.add(member2);
        members.add(member3);
        when(memberRepository.findAllByOrderBySurnameAscForenameAsc()).thenReturn(members);

        List<Member> allMembers = memberService.findAllMembersOrderedBySurname();

        verify(memberRepository, times(1)).findAllByOrderBySurnameAscForenameAsc();
        assertEquals(3, allMembers.size());
    }

    @Test
    public void findCurrentMembers() {
        List<MemberViewBean> members = new ArrayList<>();
        MemberViewBean member1 = TestHelper.newMemberViewBean(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        member1.setId(81L);
        MemberViewBean member2 = TestHelper.newMemberViewBean(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        member2.setId(50L);
        members.add(member1);
        members.add(member2);
        when(memberRepository.findCurrentMembers()).thenReturn(members);

        List<MemberViewBean> currentMembers = memberService.findCurrentMembers();

        verify(memberRepository, times(1)).findCurrentMembers();
        assertEquals(2, currentMembers.size());
        assertEquals(member1.getId(), currentMembers.get(0).getId());
        assertEquals(member2.getId(), currentMembers.get(1).getId());
    }

    @Test
    public void findFormerMembers() {
        List<MemberViewBean> formerMembers = new ArrayList<>();
        MemberViewBean member1 = TestHelper.newMemberViewBean(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Closed");
        MemberViewBean member2 = TestHelper.newMemberViewBean(1237L, "Bob", "Zippo", "bz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Closed");
        MemberViewBean member3 = TestHelper.newMemberViewBean(1235L, "Dave", "Zippo", "dz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Cancelled");
        formerMembers.add(member1);
        formerMembers.add(member2);
        formerMembers.add(member3);
        when(memberRepository.findFormerMembers()).thenReturn(formerMembers);

        List<MemberViewBean> foundFormerMembers = memberService.findFormerMembers();

        verify(memberRepository, times(1)).findFormerMembers();

        assertEquals(3, foundFormerMembers.size());
    }

    @Test
    public void countCurrentMembers() {
        when(memberRepository.countByStatus("Open")).thenReturn(87L);

        Long memberCount = memberService.countAllCurrentMembers();

        verify(memberRepository, times(1)).countByStatus("Open");
        assertEquals(87L, memberCount.longValue());
    }

    @Test
    public void findEligibleMembers() {
        List<MemberViewBean> eligibleMembers = new ArrayList<>();
        when(memberRepository.findEligibleMembers()).thenReturn(eligibleMembers);

        List<MemberViewBean> members = memberService.findEligibleMembers();

        verify(memberRepository, times(1)).findEligibleMembers();
        assertSame(members, eligibleMembers);
    }

    @Test
    public void findIneligibleMembers() {
        List<MemberViewBean> ineligibleMembers = new ArrayList<>();
        when(memberRepository.findIneligibleMembers()).thenReturn(ineligibleMembers);

        List<MemberViewBean> members = memberService.findIneligibleMembers();

        verify(memberRepository, times(1)).findIneligibleMembers();
        assertSame(members, ineligibleMembers);
    }

    @Test
    public void findPendingMembers() {
        List<MemberViewBean> pendingMembers = new ArrayList<>();
        when(memberRepository.findPendingMembers()).thenReturn(pendingMembers);

        List<MemberViewBean> members = memberService.findPendingMembers();

        verify(memberRepository, times(1)).findPendingMembers();
        assertSame(members, pendingMembers);
    }

    @Test
    public void findLapsedMembers() {
        List<MemberViewBean> lapsedMembers = new ArrayList<>();
        when(memberRepository.findLapsedMembers()).thenReturn(lapsedMembers);

        List<MemberViewBean> members = memberService.findRecentlyLapsedMembers();

        verify(memberRepository, times(1)).findLapsedMembers();
        assertSame(members, lapsedMembers);
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
    public void findMemberById_success() {
        Member member = TestHelper.newMember(1234L, "Frank", "Zippo", "fz@email.com", "0131999888", null, "Monthly", "Lifeline", "New member", "Open");
        member.setId(564L);
        when(memberRepository.getOne(564L)).thenReturn(member);

        Member foundMember = memberService.findMemberById(564L);

        verify(memberRepository, times(1)).getOne(564L);
        assertEquals("fz@email.com", foundMember.getEmail());
    }

    @Test
    public void saveMember() {
        Member member = TestHelper.newMember(2566L, "Billy", "Whiz", "bw@email.com", "0131991188", null, "Annual", "Lifeline", "New member", null);
        member.setJoinDate(null);
        when(memberRepository.save(member)).thenReturn(member);
        assertNull(member.getJoinDate());

        Member newMember = memberService.saveMember(member);

        verify(memberRepository, times(1)).save(member);
        assertEquals(2566L, newMember.getMembershipNumber().longValue());
        assertEquals("Billy", newMember.getForename());
        assertEquals("Whiz", newMember.getSurname());
        assertEquals("TBC", newMember.getStatus());
        assertNotNull(member.getJoinDate());
        assertEquals(DateTime.now().getDayOfYear(), new DateTime(newMember.getJoinDate()).getDayOfYear());

    }

    @Test
    public void getNextMembershipNumber() {
        when(memberRepository.nextMembershipNumber()).thenReturn(144L);
        long nextNumber = memberService.getNextMembershipNumber();
        assertEquals(144L, nextNumber);
    }

    @Test
    public void updateMember() {
        Member member = TestHelper.newMember(23L, "Billy", "Whiz", "bw@email.com", "0131991188", null, "Annual", "Lifeline", "New member", "Open");

        when(memberRepository.save(member)).thenReturn(member);
        Configuration refreshRequired = new Configuration();
        refreshRequired.setBooleanValue(false);
        when(configurationRepository.findByName(ELIGIBILITY_REFRESH_REQUIRED)).thenReturn(refreshRequired);

        Member newMember = memberService.updateMember(member);

        verify(configurationRepository, times(1)).findByName(ELIGIBILITY_REFRESH_REQUIRED);
        assertTrue(refreshRequired.getBooleanValue());
        verify(configurationRepository, times(1)).save(refreshRequired);

        verify(memberRepository, never()).nextMembershipNumber();
        verify(memberRepository, times(1)).save(member);
        assertEquals(23L, newMember.getMembershipNumber().longValue());
        assertEquals("Billy", newMember.getForename());
        assertEquals("Whiz", newMember.getSurname());

    }

    @Test
    public void fetchLifelineMemberDrawEntries() {
        MemberViewBean lifelineMember1 = TestHelper.newMemberViewBean(23L, "Billy", "Whiz", "bw@email.com", "0131991188", null, "Monthly", "Lifeline", "New member", "Open");
        lifelineMember1.setId(66L);
        lifelineMember1.setEligibleForDrawStored(true);
        MemberViewBean lifelineMember2 = TestHelper.newMemberViewBean(24L, "Jimmy", "Whiz", "jw@email.com", "0131991188", null, "Quarterly", "Lifeline", "New member", "Open");
        lifelineMember2.setId(404L);
        lifelineMember2.setEligibleForDrawStored(true);
        MemberViewBean underpaidMember = TestHelper.newMemberViewBean(97L, "Jen", "Underwood", "under@email.com", "0131991188", null, "Annual", "Lifeline", "New member", "Open");
        underpaidMember.setId(999L);
        underpaidMember.setEligibleForDrawStored(false);
        MemberViewBean closedLifelineMember = TestHelper.newMemberViewBean(99L, "Boris", "Loser", "bl@email.com", "0131991188", null, "Annual", "Lifeline", "New member", "Closed");
        closedLifelineMember.setEligibleForDrawStored(false);
        MemberViewBean cancelledLifelineMember = TestHelper.newMemberViewBean(98L, "Theresa", "Left", "tl@email.com", "0131991188", null, "Annual", "Lifeline", "New member", "Cancelled");
        cancelledLifelineMember.setEligibleForDrawStored(false);
        List<MemberViewBean> allMembers = new ArrayList<>();
        allMembers.add(lifelineMember1);
        allMembers.add(lifelineMember2);
        allMembers.add(underpaidMember);
        allMembers.add(closedLifelineMember);
        allMembers.add(cancelledLifelineMember);
        when(memberRepository.findAllMembers()).thenReturn(allMembers);
        when(memberRepository.findCurrentMembers()).thenReturn(allMembers);

        List<MemberViewBean> memberDrawEntries = memberService.fetchMemberDrawEntries();

        assertEquals(6, memberDrawEntries.size());
        assertTrue(memberDrawEntries.contains(lifelineMember1));
        assertTrue(memberDrawEntries.contains(lifelineMember2));
        assertFalse(memberDrawEntries.contains(closedLifelineMember));
        assertFalse(memberDrawEntries.contains(cancelledLifelineMember));
    }

    @Test
    public void fetchLegacyMemberDrawEntries() {
        MemberViewBean legacyMember1 = TestHelper.newMemberViewBean(23L, "David", "Jones", "bw@email.com", "0131991188", null, "Monthly", "Legacy", "New member", "Open");
        legacyMember1.setId(87L);
        legacyMember1.setEligibleForDrawStored(true);
        MemberViewBean premiumLegacyMember2 = TestHelper.newMemberViewBean(24L, "Jimmy", "Jones", "jw@email.com", "0131991188", null, "Quarterly", "Premium Legacy", "New member", "Open");
        premiumLegacyMember2.setId(4L);
        premiumLegacyMember2.setEligibleForDrawStored(true);
        MemberViewBean legacyMember3 = TestHelper.newMemberViewBean(24L, "Ann", "Smith", "smithy@email.com", "0131991188", null, "Annual", "Legacy", "New member", "Open");
        legacyMember3.setId(89L);
        legacyMember3.setEligibleForDrawStored(false);
        MemberViewBean closedLegacyMember = TestHelper.newMemberViewBean(99L, "Boris", "Loser", "bl@email.com", "0131991188", null, "Annual", "Legacy", "New member", "Closed");
        MemberViewBean cancelledLegacyMember = TestHelper.newMemberViewBean(98L, "Theresa", "Left", "tl@email.com", "0131991188", null, "Annual", "Legacy", "New member", "Cancelled");

        List<MemberViewBean> allMembers = new ArrayList<>();
        allMembers.add(legacyMember1);
        allMembers.add(premiumLegacyMember2);
        allMembers.add(legacyMember3);
        allMembers.add(closedLegacyMember);
        allMembers.add(cancelledLegacyMember);
        when(memberRepository.findAllMembers()).thenReturn(allMembers);

        List<MemberViewBean> memberDrawEntries = memberService.fetchMemberDrawEntries();

        assertEquals(2, memberDrawEntries.size());
        assertTrue(memberDrawEntries.contains(legacyMember1));
        assertTrue(memberDrawEntries.contains(premiumLegacyMember2));
        assertFalse(memberDrawEntries.contains(closedLegacyMember));
        assertFalse(memberDrawEntries.contains(cancelledLegacyMember));
    }

    @Test
    public void getLastExpectedPaymentDate() {
        DateTime fromDate = new DateTime(2017, 5, 2, 0, 0);
        System.out.println(fromDate.toString("dd/MM/yyyy"));

        DateTime lastExpectedPaymentDate = memberService.getLastExpectedPaymentDate(fromDate, "Monthly");

        DateTime lastDate = new DateTime(2017, 4, 2, 0, 0);
        assertEquals(lastDate, lastExpectedPaymentDate);

        lastExpectedPaymentDate = memberService.getLastExpectedPaymentDate(fromDate, "Quarterly");

        lastDate = new DateTime(2017, 2, 2, 0, 0);
        assertEquals(lastDate, lastExpectedPaymentDate);

        lastExpectedPaymentDate = memberService.getLastExpectedPaymentDate(fromDate, "Annual");

        lastDate = new DateTime(2016, 5, 2, 0, 0);
        assertEquals(lastDate, lastExpectedPaymentDate);
    }

    @Test
    public void closeLapsedMemberships() {
        List<MemberViewBean> members = new ArrayList<>();
        MemberViewBean lapsedMember1 = TestHelper.newMemberViewBean(8888L, "Bob", "Lateman", "bl@email.com", null, null, "Monthly", "Lifeline", null, "Open");
        lapsedMember1.setId(1L);
        members.add(lapsedMember1);
        MemberViewBean lapsedMember2 = TestHelper.newMemberViewBean(8999L, "Sally", "Lateman", "sl@email.com", null, null, "Monthly", "Lifeline", null, "Open");
        lapsedMember2.setId(2L);
        members.add(lapsedMember2);
        MemberViewBean activeMember = TestHelper.newMemberViewBean(1111L, "Steve", "Timely", "steveT@email.com", null, null, "Monthly", "Lifeline", null, "Open");
        activeMember.setId(3L);
        members.add(activeMember);
        when(memberRepository.findCurrentMembers()).thenReturn(members);
        Member lapsed1 = lapsedMember1.toEntity();
        when(memberRepository.getOne(lapsedMember1.getId())).thenReturn(lapsed1);
        Member lapsed2 = lapsedMember2.toEntity();
        when(memberRepository.getOne(lapsedMember2.getId())).thenReturn(lapsed2);
        when(paymentRepository.getTotalLotteryPaymentSince(any(Date.class), eq(lapsedMember1.getId()))).thenReturn(19.99D);
        when(paymentRepository.getTotalLotteryPaymentSince(any(Date.class), eq(lapsedMember2.getId()))).thenReturn(0.00D);
        when(paymentRepository.getTotalLotteryPaymentSince(any(Date.class), eq(activeMember.getId()))).thenReturn(20.00D);
        Configuration refreshRequired = new Configuration();
        refreshRequired.setBooleanValue(false);
        when(configurationRepository.findByName(ELIGIBILITY_REFRESH_REQUIRED)).thenReturn(refreshRequired);
        assertEquals("Open", lapsedMember1.getStatus());
        assertEquals("Open", lapsedMember2.getStatus());
        assertEquals("Open", activeMember.getStatus());

        int closedAccountCount = memberService.closeLapsedMemberships();

        verify(memberRepository, times(2)).getOne(any(Long.class));
        verify(memberRepository, times(2)).save(any(Member.class));
        verify(memberRepository, times(1)).save(lapsed1);
        verify(memberRepository, times(1)).save(lapsed2);
        assertEquals("Closed", lapsed1.getStatus());
        assertEquals("Closed", lapsed2.getStatus());
        assertEquals(2, closedAccountCount);
    }

    @Test
    public void lotteryEligibilityStatusRefreshRequired_false() {
        Configuration lastRefreshConfig = new Configuration();
        lastRefreshConfig.setDateValue(DateTime.now().toDate());
        when(configurationRepository.findByName(LAST_ELIGIBILITY_REFRESH_DATE)).thenReturn(lastRefreshConfig);
        Configuration refreshRequired = new Configuration();
        refreshRequired.setBooleanValue(false);
        when(configurationRepository.findByName(ELIGIBILITY_REFRESH_REQUIRED)).thenReturn(refreshRequired);

        assertFalse(memberService.lotteryEligibilityStatusRefreshRequired());
    }

    @Test
    public void lotteryEligibilityStatusRefreshRequired_trueByDate() {
        Configuration lastRefreshConfig = new Configuration();
        lastRefreshConfig.setDateValue(DateTime.now().minusHours(25).toDate());
        when(configurationRepository.findByName(LAST_ELIGIBILITY_REFRESH_DATE)).thenReturn(lastRefreshConfig);
        Configuration refreshRequired = new Configuration();
        refreshRequired.setBooleanValue(false);
        when(configurationRepository.findByName(ELIGIBILITY_REFRESH_REQUIRED)).thenReturn(refreshRequired);

        assertTrue(memberService.lotteryEligibilityStatusRefreshRequired());
    }

    @Test
    public void lotteryEligibilityStatusRefreshRequired_trueByDateEquals() {
        Configuration lastRefreshConfig = new Configuration();
        lastRefreshConfig.setDateValue(DateTime.now().minusHours(24).toDate());
        when(configurationRepository.findByName(LAST_ELIGIBILITY_REFRESH_DATE)).thenReturn(lastRefreshConfig);
        Configuration refreshRequired = new Configuration();
        refreshRequired.setBooleanValue(false);
        when(configurationRepository.findByName(ELIGIBILITY_REFRESH_REQUIRED)).thenReturn(refreshRequired);

        assertTrue(memberService.lotteryEligibilityStatusRefreshRequired());
    }

    @Test
    public void lotteryEligibilityStatusRefreshRequired_trueDataChange() {
        Configuration lastRefreshConfig = new Configuration();
        lastRefreshConfig.setDateValue(DateTime.now().toDate());
        when(configurationRepository.findByName(LAST_ELIGIBILITY_REFRESH_DATE)).thenReturn(lastRefreshConfig);
        Configuration refreshRequired = new Configuration();
        refreshRequired.setBooleanValue(true);
        when(configurationRepository.findByName(ELIGIBILITY_REFRESH_REQUIRED)).thenReturn(refreshRequired);

        assertTrue(memberService.lotteryEligibilityStatusRefreshRequired());
    }

    @Test
    public void updateEligibilityStatuses_notRequired() {
        Configuration lastRefreshConfig = new Configuration();
        lastRefreshConfig.setDateValue(DateTime.now().toDate());
        when(configurationRepository.findByName(LAST_ELIGIBILITY_REFRESH_DATE)).thenReturn(lastRefreshConfig);
        Configuration refreshRequired = new Configuration();
        refreshRequired.setBooleanValue(false);
        when(configurationRepository.findByName(ELIGIBILITY_REFRESH_REQUIRED)).thenReturn(refreshRequired);

        memberService.scheduledUpdateEligibilityCall();

        verify(memberRepository, never()).findCurrentMembers();
        verify(configurationRepository, never()).save(any(Configuration.class));
    }

    @Test
    public void updateEligibilityStatuses() {
        Configuration lastRefreshConfig = new Configuration();
        lastRefreshConfig.setDateValue(DateTime.now().toDate());
        when(configurationRepository.findByName(LAST_ELIGIBILITY_REFRESH_DATE)).thenReturn(lastRefreshConfig);
        Configuration refreshRequired = new Configuration();
        refreshRequired.setBooleanValue(true);
        when(configurationRepository.findByName(ELIGIBILITY_REFRESH_REQUIRED)).thenReturn(refreshRequired);
        List<MemberViewBean> members = new ArrayList<>();
        MemberViewBean joe = new MemberViewBean();
        MemberViewBean jess = new MemberViewBean();
        joe.setEligibleForDrawStored(false);
        joe.setStatus("Open");
        joe.setPayerType("Monthly");
        joe.setMembershipType("Lifeline");
        jess.setEligibleForDrawStored(true);
        jess.setStatus("Open");
        jess.setPayerType("Monthly");
        jess.setMembershipType("Lifeline");
        members.add(joe);
        members.add(jess);
        when(memberRepository.findCurrentMembers()).thenReturn(members);
        when(paymentRepository.getTotalLotteryPaymentSince(any(), any())).thenReturn(20.00);

        memberService.scheduledUpdateEligibilityCall();

        verify(memberRepository, times(1)).findCurrentMembers();
        verify(configurationRepository, times(2)).save(any(Configuration.class));
        assertTrue(joe.isEligibleForDrawStored());
        assertTrue(jess.isEligibleForDrawStored());
    }

}