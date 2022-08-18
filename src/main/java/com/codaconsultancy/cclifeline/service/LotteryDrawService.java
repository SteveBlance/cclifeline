package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.LotteryDraw;
import com.codaconsultancy.cclifeline.domain.Prize;
import com.codaconsultancy.cclifeline.repositories.LotteryDrawRepository;
import com.codaconsultancy.cclifeline.repositories.PrizeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LotteryDrawService extends LifelineService {

    private final LotteryDrawRepository lotteryDrawRepository;

    private final PrizeRepository prizeRepository;

    public LotteryDrawService(LotteryDrawRepository lotteryDrawRepository, PrizeRepository prizeRepository) {
        this.lotteryDrawRepository = lotteryDrawRepository;
        this.prizeRepository = prizeRepository;
    }

    public List<LotteryDraw> fetchAllLotteryDraws() {
        return lotteryDrawRepository.findAll();
    }

    public LotteryDraw fetchLotteryDraw(Long id) {
        return lotteryDrawRepository.getOne(id);
    }

    public LotteryDraw fetchLastDraw() {
        return lotteryDrawRepository.findLastDraw();
    }

    public LotteryDraw saveLotteryDraw(LotteryDraw lotteryDraw) {
        LotteryDraw draw = lotteryDrawRepository.save(lotteryDraw);
        List<Prize> prizes = draw.getPrizes();
        for (Prize prize : prizes) {
            prize.setLotteryDraw(draw);
        }
        prizeRepository.saveAll(prizes);
        return draw;
    }

    public Long countAllWinners() {
        return prizeRepository.count();
    }
}
