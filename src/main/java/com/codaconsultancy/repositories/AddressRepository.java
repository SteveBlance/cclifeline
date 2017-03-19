package com.codaconsultancy.repositories;

import com.codaconsultancy.domain.Address;
import com.codaconsultancy.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    public List<Address> findByMember(Member member);
}
