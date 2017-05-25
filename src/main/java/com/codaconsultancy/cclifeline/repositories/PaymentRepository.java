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

    @Query(value =
            "SELECT IFNULL(SUM(PAYMENT_AMOUNT), 0) " +
                    "FROM PAYMENTS WHERE MEMBER_ID = :member " +
                    "AND PAYMENT_DATE >= :lastExpectedPaymentDate",
            nativeQuery = true)
    Double getTotalPaymentSince(@Param("lastExpectedPaymentDate") Date lastExpectedPaymentDate, @Param("member") Long memberId);
}
