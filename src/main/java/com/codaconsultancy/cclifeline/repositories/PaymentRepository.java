package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.domain.Payment;
import com.codaconsultancy.cclifeline.view.PaymentViewBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByMember(Member member);

    List<Payment> findByMemberIsNullAndIsLotteryPayment(boolean isLotteryPayment);

    List<Payment> findByMemberIsNotNullAndIsLotteryPayment(boolean isLotteryPayment);

    @Query(value =
            "SELECT new com.codaconsultancy.cclifeline.view.PaymentViewBean(p.paymentDate, p.paymentAmount, p.creditReference, p.creditedAccount, p.name, p.isLotteryPayment, m.id, CONCAT(m.membershipNumber,': ',m.forename,' ',m.surname) as displ) " +
                    "FROM Payment p JOIN p.member m " +
                    "WHERE p.isLotteryPayment = true ")
    List<PaymentViewBean> findMatchedLotteryPayments();

    @Query(value =
            "SELECT new com.codaconsultancy.cclifeline.view.PaymentViewBean(p.paymentDate, p.paymentAmount, p.creditReference, p.creditedAccount, p.name, p.isLotteryPayment, 0L, '') " +
                    "FROM Payment p " +
                    "WHERE p.isLotteryPayment = true " +
                    "and p.member is null ")
    List<PaymentViewBean> findUnmatchedLotteryPayments();

    @Query(value =
            "SELECT new com.codaconsultancy.cclifeline.view.PaymentViewBean(p.paymentDate, p.paymentAmount, p.creditReference, p.creditedAccount, p.name, p.isLotteryPayment, m.id, CONCAT(m.membershipNumber,': ',m.forename,' ',m.surname) as displ) " +
                    "FROM Payment p LEFT OUTER JOIN p.member m ")
    List<PaymentViewBean> findAllPayments();

    @Query(value =
            "SELECT new com.codaconsultancy.cclifeline.view.PaymentViewBean(p.paymentDate, p.paymentAmount, p.creditReference, p.creditedAccount, p.name, p.isLotteryPayment, 0L, '') " +
                    "FROM Payment p " +
                    "WHERE p.isLotteryPayment = false ")
    List<PaymentViewBean> findAllNonLotteryPayments();

    @Query(value =
            "SELECT new com.codaconsultancy.cclifeline.view.PaymentViewBean(p.paymentDate, p.paymentAmount, p.creditReference, p.creditedAccount, p.name, p.isLotteryPayment, m.id, CONCAT(m.membershipNumber,': ',m.forename,' ',m.surname) as displ) " +
                    "FROM Payment p LEFT OUTER JOIN p.member m " +
                    "where p.paymentDate > :date")
    List<PaymentViewBean> findAllPaymentsAfter(@Param("date") Date date);

    List<Payment> findByIsLotteryPayment(boolean isLotteryPayment);

    List<Payment> findByPaymentDateAfter(Date date);

    Payment findTopByMemberAndIsLotteryPaymentOrderByPaymentDateDesc(Member member, boolean isLotteryPayment);

    @Query(value =
            "SELECT IFNULL(SUM(PAYMENT_AMOUNT), 0) " +
                    "FROM PAYMENTS WHERE MEMBER_ID = :member " +
                    "AND PAYMENT_DATE >= :lastExpectedPaymentDate " +
                    "AND IS_LOTTERY_PAYMENT = TRUE ",
            nativeQuery = true)
    Double getTotalLotteryPaymentSince(@Param("lastExpectedPaymentDate") Date lastExpectedPaymentDate, @Param("member") Long memberId);
}
