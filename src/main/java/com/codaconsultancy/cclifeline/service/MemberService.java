package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.repositories.MemberRepository;
import com.codaconsultancy.cclifeline.repositories.PaymentRepository;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService extends LifelineService {

    public static final String ANNUAL = "Annual";
    public static final String QUARTERLY = "Quarterly";
    public static final String LIFELINE = "Lifeline";
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public List<Member> findAllMembers() {
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            if (isEligibleForDraw(member)) {
                member.setIsEligibleForDraw(true);
            } else {
                member.setIsEligibleForDraw(false);
            }
        }
        return members;
    }

    public List<Member> findCurrentMembers() {
        List<Member> members = memberRepository.findAllByStatusOrderBySurnameAscForenameAsc("Open");
        for (Member member : members) {
            if (isEligibleForDraw(member)) {
                member.setIsEligibleForDraw(true);
            } else {
                member.setIsEligibleForDraw(false);
            }
        }
        return members;
    }

    public List<Member> findEligibleMembers() {
        List<Member> currentMembers = findCurrentMembers();
        List<Member> eligibleMembers = new ArrayList<>();
        for (Member member : currentMembers) {
            if (isEligibleForDraw(member)) {
                member.setIsEligibleForDraw(true);
                eligibleMembers.add(member);
            }
        }
        return eligibleMembers;
    }

    public List<Member> findIneligibleMembers() {
        List<Member> currentMembers = findCurrentMembers();
        List<Member> ineligibleMembers = new ArrayList<>();
        for (Member member : currentMembers) {
            if (!isEligibleForDraw(member)) {
                member.setIsEligibleForDraw(false);
                ineligibleMembers.add(member);
            }
        }
        return ineligibleMembers;
    }

    public List<Member> findFormerMembers() {
        List<Member> formerMembers = new ArrayList<>();
        formerMembers.addAll(memberRepository.findAllByStatusOrderBySurnameAscForenameAsc("Cancelled"));
        formerMembers.addAll(memberRepository.findAllByStatusOrderBySurnameAscForenameAsc("Closed"));
        for (Member formerMember : formerMembers) {
            formerMember.setIsEligibleForDraw(false);
        }
        return formerMembers;
    }

    public List<Member> findAllMembersOrderedBySurname() {
        return memberRepository.findAllByOrderBySurnameAscForenameAsc();
    }

    public Long countAllCurrentMembers() {
        return memberRepository.countByStatus("Open");
    }

    public Member findMemberByMembershipNumber(Long memberNumber) {
        return memberRepository.findByMembershipNumber(memberNumber);
    }

    public Member saveMember(Member member) {
        Long nextMembershipNumber = memberRepository.nextMembershipNumber();
        member.setMembershipNumber(nextMembershipNumber);
        member.setStatus("TBC");
        member.setJoinDate(DateTime.now().toDate());

        return memberRepository.save(member);
    }

    public Member updateMember(Member member) {
        return memberRepository.save(member);
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    public List<Member> fetchMemberDrawEntries() {
        List<Member> memberDrawEntries = new ArrayList<>();
        List<Member> allMembers = findAllMembers();
        for (Member member : allMembers) {
            if (member.isEligibleForDraw()) {
                if (isLifelineMember(member)) {
                    // 3 entries for lifeline members
                    memberDrawEntries.add(member);
                    memberDrawEntries.add(member);
                    memberDrawEntries.add(member);
                } else {
                    // 1 entry for Legacy member
                    memberDrawEntries.add(member);
                }

            }
        }
        return memberDrawEntries;
    }

    private boolean isLifelineMember(Member member) {
        return member.getMembershipType().equalsIgnoreCase(LIFELINE);
    }

    public boolean isEligibleForDraw(Member member) {
        //TODO: use stored config for gracePeriodInDays
        int gracePeriodInDays = 30;

        return member.getStatus().equals("Open") && (paymentsAreUpToDate(member, gracePeriodInDays));
    }

    private boolean paymentsAreUpToDate(Member member, int gracePeriodInDays) {
        String payerType = member.getPayerType();
        String membershipType = member.getMembershipType();
        DateTime today = new DateTime();

        DateTime lastExpectedPaymentDate = getLastExpectedPaymentDate(today, payerType).minus(Period.days(gracePeriodInDays));

        Double paymentTotalThisPeriod = paymentRepository.getTotalLotteryPaymentSince(lastExpectedPaymentDate.toDate(), member.getId());

        return (paymentTotalThisPeriod >= requiredPaymentFrom(payerType, membershipType));
    }

    private Double requiredPaymentFrom(String payerType, String membershipType) {
        Double requiredPayment;
        if (LIFELINE.equals(membershipType)) {
            if (ANNUAL.equals(payerType)) {
                requiredPayment = 240.00D;
            } else if (QUARTERLY.equals(payerType)) {
                requiredPayment = 60.00D;
            } else {
                requiredPayment = 20.00D;
            }
        } else {
            if (ANNUAL.equals(payerType)) {
                requiredPayment = 104.00D;
            } else if (QUARTERLY.equals(payerType)) {
                requiredPayment = 26.00D;
            } else {
                requiredPayment = 8.66D;
            }
        }
        return requiredPayment;
    }

    DateTime getLastExpectedPaymentDate(DateTime fromDate, String payerType) {
        DateTime lastExpectedPaymentDate;
        if (ANNUAL.equals(payerType)) {
            lastExpectedPaymentDate = fromDate.minus(Period.years(1));
        } else if (QUARTERLY.equals(payerType)) {
            lastExpectedPaymentDate = fromDate.minus(Period.months(3));
        } else {
            lastExpectedPaymentDate = fromDate.minus(Period.months(1));
        }
        return lastExpectedPaymentDate;
    }
}
