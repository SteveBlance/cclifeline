package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.domain.Prize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrizeRepository extends JpaRepository<Prize, Long> {
}
