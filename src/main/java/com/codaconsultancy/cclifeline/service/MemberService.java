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
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    public List<Member> findCurrentMembers() {
        return memberRepository.findAllByStatusOrderBySurnameAscForenameAsc("Open");
    }

    public List<Member> findEligibleMembers() {
        List<Member> currentMembers = findCurrentMembers();
        List<Member> eligibleMembers = new ArrayList<>();
        for (Member member : currentMembers) {
            if (isEligibleForDraw(member)) {
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
                ineligibleMembers.add(member);
            }
        }
        return ineligibleMembers;
    }

    public List<Member> findFormerMembers() {
        List<Member> formerMembers = new ArrayList<>();
        formerMembers.addAll(memberRepository.findAllByStatusOrderBySurnameAscForenameAsc("Cancelled"));
        formerMembers.addAll(memberRepository.findAllByStatusOrderBySurnameAscForenameAsc("Closed"));
        return formerMembers;
    }

    public Long countAllMembers() {
        return memberRepository.count();
    }

    public Member findMemberByMembershipNumber(Long memberNumber) {
        return memberRepository.findByMembershipNumber(memberNumber);
    }

    public Member saveMember(Member member) {
        Long nextMembershipNumber = memberRepository.nextMembershipNumber();
        member.setMembershipNumber(nextMembershipNumber);

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
            if (isEligibleForDraw(member)) {
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
        return member.getMembershipType().equalsIgnoreCase("Lifeline");
    }

    public boolean isEligibleForDraw(Member member) {
        //TODO: use stored config for gracePeriodInDays
        int gracePeriodInDays = 30;

        return member.getStatus().equalsIgnoreCase("Open") && (paymentsAreUpToDate(member, gracePeriodInDays));
    }

    private boolean paymentsAreUpToDate(Member member, int gracePeriodInDays) {
        String payerType = member.getPayerType();
        String membershipType = member.getMembershipType();
        DateTime today = new DateTime();

        DateTime lastExpectedPaymentDate = getLastExpectedPaymentDate(today, payerType).minus(Period.days(gracePeriodInDays));

        Double paymentTotalThisPeriod = paymentRepository.getTotalPaymentSince(lastExpectedPaymentDate.toDate(), member.getId());

        return (paymentTotalThisPeriod >= requiredPaymentFrom(payerType, membershipType));
    }

    private Double requiredPaymentFrom(String payerType, String membershipType) {
        Double requiredPayment;
        if ("Lifeline".equalsIgnoreCase(membershipType)) {
            if ("Annual".equalsIgnoreCase(payerType)) {
                requiredPayment = 240.00D;
            } else if ("Quarterly".equalsIgnoreCase(payerType)) {
                requiredPayment = 60.00D;
            } else {
                requiredPayment = 20.00D;
            }
        } else {
            if ("Annual".equalsIgnoreCase(payerType)) {
                requiredPayment = 104.00D;
            } else if ("Quarterly".equalsIgnoreCase(payerType)) {
                requiredPayment = 26.00D;
            } else {
                requiredPayment = 8.66D;
            }
        }
        return requiredPayment;
    }

    DateTime getLastExpectedPaymentDate(DateTime fromDate, String payerType) {
        DateTime lastExpectedPaymentDate;
        if ("Annual".equalsIgnoreCase(payerType)) {
            lastExpectedPaymentDate = fromDate.minus(Period.years(1));
        } else if ("Quarterly".equalsIgnoreCase(payerType)) {
            lastExpectedPaymentDate = fromDate.minus(Period.months(3));
        } else {
            lastExpectedPaymentDate = fromDate.minus(Period.months(1));
        }
        return lastExpectedPaymentDate;
    }
}
