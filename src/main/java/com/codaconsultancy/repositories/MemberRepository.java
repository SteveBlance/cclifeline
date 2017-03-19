package com.codaconsultancy.repositories;

import com.codaconsultancy.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
