package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.LotteryDraw;
import com.codaconsultancy.cclifeline.repositories.LotteryDrawRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@EntityScan("com.codaconsultancy.cclifeline.domain")
@SpringBootTest(classes = LotteryDrawService.class)
public class LotteryDrawServiceTest {

    @Autowired
    private LotteryDrawService lotteryDrawService;

    @MockBean
    private LotteryDrawRepository lotteryDrawRepository;

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

}