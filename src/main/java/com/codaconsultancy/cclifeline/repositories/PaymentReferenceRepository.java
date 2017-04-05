package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.domain.PaymentReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentReferenceRepository extends JpaRepository<PaymentReference, Long> {

    List<PaymentReference> findByMember(Member member);

    List<PaymentReference> findByMemberAndIsActive(Member member, boolean isActive);
}
