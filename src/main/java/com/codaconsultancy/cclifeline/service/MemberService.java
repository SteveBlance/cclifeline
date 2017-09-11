package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.repositories.MemberRepository;
import com.codaconsultancy.cclifeline.repositories.PaymentRepository;
import com.codaconsultancy.cclifeline.view.MemberViewBean;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MemberService extends LifelineService {

    private static final String MONTHLY = "Monthly";
    private static final String QUARTERLY = "Quarterly";
    private static final String ANNUAL = "Annual";
    private static final String LIFELINE = "Lifeline";
    private static final int PAYMENT_GRACE_PERIOD_IN_DAYS = 30;
    private static final String TBC_STATUS = "TBC";
    private static final String OPEN_STATUS = "Open";
    private static final String CLOSED_STATUS = "Closed";

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public List<MemberViewBean> findAllMembers() {
        List<MemberViewBean> members = memberRepository.findAllMembers();
        for (MemberViewBean member : members) {
            if (isEligibleForDraw(member)) {
                member.setIsEligibleForDraw(true);
            } else {
                member.setIsEligibleForDraw(false);
            }
        }
        return members;
    }

    public List<MemberViewBean> findCurrentMembers() {
        List<MemberViewBean> members = memberRepository.findCurrentMembers();
        for (MemberViewBean member : members) {
            if (isEligibleForDraw(member)) {
                member.setIsEligibleForDraw(true);
            } else {
                member.setIsEligibleForDraw(false);
            }
        }
        return members;
    }

    public List<MemberViewBean> findEligibleMembers() {
        List<MemberViewBean> currentMembers = findCurrentMembers();
        List<MemberViewBean> eligibleMembers = new ArrayList<>();
        for (MemberViewBean member : currentMembers) {
            if (isEligibleForDraw(member)) {
                member.setIsEligibleForDraw(true);
                eligibleMembers.add(member);
            }
        }
        return eligibleMembers;
    }

    public List<MemberViewBean> findIneligibleMembers() {
        List<MemberViewBean> currentMembers = findCurrentMembers();
        List<MemberViewBean> ineligibleMembers = new ArrayList<>();
        for (MemberViewBean member : currentMembers) {
            if (!isEligibleForDraw(member)) {
                member.setIsEligibleForDraw(false);
                ineligibleMembers.add(member);
            }
        }
        return ineligibleMembers;
    }

    public List<MemberViewBean> findFormerMembers() {
        return memberRepository.findFormerMembers();
    }

    public List<Member> findAllMembersOrderedBySurname() {
        return memberRepository.findAllByOrderBySurnameAscForenameAsc();
    }

    public Long countAllCurrentMembers() {
        return memberRepository.countByStatus(OPEN_STATUS);
    }

    public Member findMemberByMembershipNumber(Long memberNumber) {
        return memberRepository.findByMembershipNumber(memberNumber);
    }

    public Member saveMember(Member member) {
        Long nextMembershipNumber = memberRepository.nextMembershipNumber();
        member.setMembershipNumber(nextMembershipNumber);
        member.setStatus(TBC_STATUS);
        member.setJoinDate(DateTime.now().toDate());

        return memberRepository.save(member);
    }

    public Member updateMember(Member member) {
        return memberRepository.save(member);
    }

    public Member findMemberById(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    public List<MemberViewBean> fetchMemberDrawEntries() {
        List<MemberViewBean> memberDrawEntries = new ArrayList<>();
        List<MemberViewBean> allMembers = findAllMembers();
        for (MemberViewBean member : allMembers) {
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

    public int closeLapsedMemberships() {
        int accountsClosed = 0;
        List<MemberViewBean> currentMembers = memberRepository.findCurrentMembers();
        Member member;
        for (MemberViewBean currentMember : currentMembers) {
            if (!paymentsAreUpToDate(currentMember, PAYMENT_GRACE_PERIOD_IN_DAYS)) {
                member = memberRepository.findOne(currentMember.getId());
                member.setStatus(CLOSED_STATUS);
                updateMember(member);
                accountsClosed++;
            }
        }
        return accountsClosed;
    }

    private boolean isLifelineMember(MemberViewBean member) {
        return member.getMembershipType().equalsIgnoreCase(LIFELINE);
    }

    public boolean isEligibleForDraw(MemberViewBean member) {
        return OPEN_STATUS.equals(member.getStatus()) && (paymentsAreUpToDate(member, PAYMENT_GRACE_PERIOD_IN_DAYS));
    }

    private boolean paymentsAreUpToDate(MemberViewBean member, int gracePeriodInDays) {
        String payerType = member.getPayerType();
        String membershipType = member.getMembershipType();
        DateTime today = new DateTime();

        Date lastExpectedPaymentDate = getLastExpectedPaymentDate(today, payerType).minus(Period.days(gracePeriodInDays)).toDate();

        Double paymentTotalThisPeriod = paymentRepository.getTotalLotteryPaymentSince(lastExpectedPaymentDate, member.getId());

        return (paymentTotalThisPeriod >= requiredPaymentFrom(payerType, membershipType));
    }

    private Double requiredPaymentFrom(String payerType, String membershipType) {
        Double requiredPayment;
        if (LIFELINE.equals(membershipType)) {
            switch (payerType) {
                case MONTHLY:
                    requiredPayment = 20.00D;
                    break;
                case QUARTERLY:
                    requiredPayment = 60.00D;
                    break;
                case ANNUAL:
                    requiredPayment = 240.00D;
                    break;
                default:
                    requiredPayment = 20.00D;
                    break;
            }
        } else {
            switch (payerType) {
                case MONTHLY:
                    requiredPayment = 8.66D;
                    break;
                case QUARTERLY:
                    requiredPayment = 26.00D;
                    break;
                case ANNUAL:
                    requiredPayment = 104.00D;
                    break;
                default:
                    requiredPayment = 8.66D;
                    break;
            }
        }
        return requiredPayment;
    }

    DateTime getLastExpectedPaymentDate(DateTime fromDate, String payerType) {
        DateTime lastExpectedPaymentDate;
        switch (payerType) {
            case MONTHLY:
                lastExpectedPaymentDate = fromDate.minus(Period.months(1));
                break;
            case QUARTERLY:
                lastExpectedPaymentDate = fromDate.minus(Period.months(3));
                break;
            case ANNUAL:
                lastExpectedPaymentDate = fromDate.minus(Period.years(1));
                break;
            default:
                lastExpectedPaymentDate = fromDate.minus(Period.months(1));
                break;
        }
        return lastExpectedPaymentDate;
    }
}
