package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.LotteryDraw;
import com.codaconsultancy.cclifeline.domain.Prize;
import com.codaconsultancy.cclifeline.repositories.LotteryDrawRepository;
import com.codaconsultancy.cclifeline.repositories.PrizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LotteryDrawService extends LifelineService {

    @Autowired
    private LotteryDrawRepository lotteryDrawRepository;

    @Autowired
    private PrizeRepository prizeRepository;

    public List<LotteryDraw> fetchAllLotteryDraws() {
        return lotteryDrawRepository.findAll();
    }

    public LotteryDraw saveLotteryDraw(LotteryDraw lotteryDraw) {
        LotteryDraw draw = lotteryDrawRepository.save(lotteryDraw);
        List<Prize> prizes = draw.getPrizes();
        for (Prize prize : prizes) {
            prize.setLotteryDraw(draw);
        }
        prizeRepository.save(prizes);
        return draw;
    }

    public Long countAllWinners() {
        return prizeRepository.count();
    }
}
