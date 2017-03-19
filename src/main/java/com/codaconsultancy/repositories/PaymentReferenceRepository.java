package com.codaconsultancy.repositories;

import com.codaconsultancy.domain.Address;
import com.codaconsultancy.domain.Member;
import com.codaconsultancy.domain.PaymentReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentReferenceRepository extends JpaRepository<PaymentReference, Long> {

    public List<Address> findByMember(Member member);

}
