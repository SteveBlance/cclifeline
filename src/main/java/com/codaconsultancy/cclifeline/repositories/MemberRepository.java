package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.view.MemberViewBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByMembershipNumber(Long membershipNumber);

    @Query("select max(m.membershipNumber)+1 from Member m")
    Long nextMembershipNumber();

    @Query(value =
            "SELECT new com.codaconsultancy.cclifeline.view.MemberViewBean(m.id, m.membershipNumber, m.forename, m.surname, m.membershipType, m.status, m.payerType, m.joinDate, m.leaveDate, m.email, m.landlineNumber, m.mobileNumber, m.isEligibleForDrawStored) " +
                    "FROM Member m ")
    List<MemberViewBean> findAllMembers();

    @Query(value =
            "SELECT new com.codaconsultancy.cclifeline.view.MemberViewBean(m.id, m.membershipNumber, m.forename, m.surname, m.membershipType, m.status, m.payerType, m.joinDate, m.leaveDate, m.email, m.landlineNumber, m.mobileNumber, m.isEligibleForDrawStored) " +
                    "FROM Member m " +
                    "WHERE m.status = 'Open' ")
    List<MemberViewBean> findCurrentMembers();

    @Query(value =
            "SELECT new com.codaconsultancy.cclifeline.view.MemberViewBean(m.id, m.membershipNumber, m.forename, m.surname, m.membershipType, m.status, m.payerType, m.joinDate, m.leaveDate, m.email, m.landlineNumber, m.mobileNumber, m.isEligibleForDrawStored) " +
                    "FROM Member m " +
                    "WHERE m.status = 'TBC' ")
    List<MemberViewBean> findTBCMembers();

    @Query(value =
            "SELECT new com.codaconsultancy.cclifeline.view.MemberViewBean(m.id, m.membershipNumber, m.forename, m.surname, m.membershipType, m.status, m.payerType, m.joinDate, m.leaveDate, m.email, m.landlineNumber, m.mobileNumber, m.isEligibleForDrawStored) " +
                    "FROM Member m " +
                    "WHERE m.status in ('Closed', 'Cancelled') ")
    List<MemberViewBean> findFormerMembers();

    List<Member> findAllByOrderBySurnameAscForenameAsc();

    List<Member> findAllByStatusOrderBySurnameAscForenameAsc(String status);

    List<Member> findAllBySurnameIgnoreCaseAndStatusOrderByForename(String surname, String status);

    Long countByStatus(String status);
}
