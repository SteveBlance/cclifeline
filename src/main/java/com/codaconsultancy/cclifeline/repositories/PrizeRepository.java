package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.domain.Prize;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrizeRepository extends JpaRepository<Prize, Long> {
}
