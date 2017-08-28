package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByMember(Member member);

    List<Payment> findByMemberIsNullAndIsLotteryPayment(boolean isLotteryPayment);

    List<Payment> findByMemberIsNotNullAndIsLotteryPayment(boolean isLotteryPayment);

    List<Payment> findByIsLotteryPayment(boolean isLotteryPayment);

    Payment findTopByMemberAndIsLotteryPaymentOrderByPaymentDateDesc(Member member, boolean isLotteryPayment);

    @Query(value =
            "SELECT IFNULL(SUM(PAYMENT_AMOUNT), 0) " +
                    "FROM PAYMENTS WHERE MEMBER_ID = :member " +
                    "AND PAYMENT_DATE >= :lastExpectedPaymentDate " +
                    "AND IS_LOTTERY_PAYMENT = TRUE ",
            nativeQuery = true)
    Double getTotalLotteryPaymentSince(@Param("lastExpectedPaymentDate") Date lastExpectedPaymentDate, @Param("member") Long memberId);
}
