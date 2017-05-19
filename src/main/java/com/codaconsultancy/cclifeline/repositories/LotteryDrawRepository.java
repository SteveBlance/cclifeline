package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.domain.LotteryDraw;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotteryDrawRepository extends JpaRepository<LotteryDraw, Long> {
}
