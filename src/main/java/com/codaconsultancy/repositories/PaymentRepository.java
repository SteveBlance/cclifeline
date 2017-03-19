package com.codaconsultancy.repositories;

import com.codaconsultancy.domain.Address;
import com.codaconsultancy.domain.Member;
import com.codaconsultancy.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    public List<Address> findByMember(Member member);

}
