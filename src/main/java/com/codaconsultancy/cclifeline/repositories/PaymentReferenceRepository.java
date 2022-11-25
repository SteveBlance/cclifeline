package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.domain.PaymentReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentReferenceRepository extends JpaRepository<PaymentReference, Long> {

    List<PaymentReference> findByMember(Member member);

    List<PaymentReference> findByMemberAndIsActive(Member member, boolean isActive);
}
