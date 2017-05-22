package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.LotteryDraw;
import com.codaconsultancy.cclifeline.repositories.LotteryDrawRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LotteryDrawService {

    @Autowired
    private LotteryDrawRepository lotteryDrawRepository;

    public List<LotteryDraw> fetchAllLotteryDraws() {
        return lotteryDrawRepository.findAll();
    }
}
