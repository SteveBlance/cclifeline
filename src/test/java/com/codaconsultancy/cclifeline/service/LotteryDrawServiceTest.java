package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.LotteryDraw;
import com.codaconsultancy.cclifeline.domain.Prize;
import com.codaconsultancy.cclifeline.repositories.LotteryDrawRepository;
import com.codaconsultancy.cclifeline.repositories.PrizeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LotteryDrawService.class)
public class LotteryDrawServiceTest extends LifelineServiceTest {

    @Autowired
    private LotteryDrawService lotteryDrawService;

    @MockBean
    private LotteryDrawRepository lotteryDrawRepository;

    @MockBean
    private PrizeRepository prizeRepository;

    @Test
    public void fetchAllLotteryDraws() throws Exception {
        List<LotteryDraw> lotteryDraws = new ArrayList<>();
        LotteryDraw draw1 = new LotteryDraw();
        draw1.setId(9L);
        LotteryDraw draw2 = new LotteryDraw();
        draw2.setId(10L);
        lotteryDraws.add(draw1);
        lotteryDraws.add(draw2);
        when(lotteryDrawRepository.findAll()).thenReturn(lotteryDraws);

        List<LotteryDraw> foundDraws = lotteryDrawService.fetchAllLotteryDraws();

        verify(lotteryDrawRepository, times(1)).findAll();

        assertEquals(2, foundDraws.size());
        assertEquals(9L, foundDraws.get(0).getId().longValue());
        assertEquals(10L, foundDraws.get(1).getId().longValue());
    }

    @Test
    public void fetchLotteryDraw() {
        LotteryDraw testDraw = new LotteryDraw();
        long drawId = 87L;
        testDraw.setId(drawId);
        when(lotteryDrawRepository.findOne(drawId)).thenReturn(testDraw);

        LotteryDraw draw = lotteryDrawService.fetchLotteryDraw(drawId);

        verify(lotteryDrawRepository, times(1)).findOne(drawId);
        assertSame(testDraw, draw);
    }

    @Test
    public void saveLotteryDraw() throws Exception {
        LotteryDraw lotteryDraw = new LotteryDraw();
        List<Prize> prizes = new ArrayList<>();
        Prize prize1 = new Prize();
        prize1.setPrize("£200");
        prizes.add(prize1);
        lotteryDraw.setPrizes(prizes);
        when(lotteryDrawRepository.save(lotteryDraw)).thenReturn(lotteryDraw);
        assertNull(prize1.getLotteryDraw());

        LotteryDraw saveDraw = lotteryDrawService.saveLotteryDraw(lotteryDraw);

        assertNotNull(prize1.getLotteryDraw());
        assertSame(lotteryDraw, prize1.getLotteryDraw());
        assertSame(saveDraw, prize1.getLotteryDraw());

        verify(lotteryDrawRepository, times(1)).save(lotteryDraw);
        verify((prizeRepository), times(1)).save(prizes);
    }

    @Test
    public void countAllWinners() {
        when(prizeRepository.count()).thenReturn(34L);

        Long winnerCount = lotteryDrawService.countAllWinners();

        verify(prizeRepository, times(1)).count();
        assertEquals(34L, winnerCount.longValue());
    }

}