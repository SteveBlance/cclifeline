package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByMembershipNumber(Long membershipNumber);

    @Query("select max(m.membershipNumber)+1 from Member m")
    Long nextMembershipNumber();

    List<Member> findAllByOrderBySurnameAscForenameAsc();

    List<Member> findAllByStatusOrderBySurnameAscForenameAsc(String status);

    List<Member> findAllBySurnameIgnoreCaseAndStatusOrderByForename(String surname, String status);

    Long countByStatus(String status);
}
