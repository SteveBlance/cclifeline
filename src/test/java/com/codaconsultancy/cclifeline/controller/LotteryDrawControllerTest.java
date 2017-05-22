package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.LotteryDraw;
import com.codaconsultancy.cclifeline.repositories.BaseTest;
import com.codaconsultancy.cclifeline.service.LotteryDrawService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@EnableJpaRepositories(basePackages = {"com.codaconsultancy.cclifeline.repositories"})
@SpringBootTest(classes = LotteryDrawController.class)
public class LotteryDrawControllerTest extends BaseTest {

    @Autowired
    private LotteryDrawController lotteryDrawController;

    @MockBean
    private LotteryDrawService lotteryDrawService;

    @Test
    public void navigateToWinners() {
        List<LotteryDraw> draws = new ArrayList<>();
        when(lotteryDrawService.fetchAllLotteryDraws()).thenReturn(draws);

        ModelAndView modelAndView = lotteryDrawController.navigateToWinners();

        verify(lotteryDrawService, times(1)).fetchAllLotteryDraws();

        assertEquals("draws", modelAndView.getViewName());
        Object lotteryDraws = modelAndView.getModel().get("lotteryDraws");
        assertNotNull(lotteryDraws);
        assertSame(draws, lotteryDraws);

    }

    @Test
    public void navigateMakeDraw() {
        assertEquals("make-draw", lotteryDrawController.navigateMakeDraw().getViewName());
    }
}
