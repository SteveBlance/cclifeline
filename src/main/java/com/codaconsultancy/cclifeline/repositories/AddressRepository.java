package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.domain.Address;
import com.codaconsultancy.cclifeline.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByMember(Member member);

    List<Address> findByMemberAndIsActive(Member member, boolean isActive);
}
