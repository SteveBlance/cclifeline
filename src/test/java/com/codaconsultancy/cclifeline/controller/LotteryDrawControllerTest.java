package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.repositories.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@EnableJpaRepositories(basePackages = {"com.codaconsultancy.cclifeline.repositories"})
@SpringBootTest(classes = LotteryDrawController.class)
public class LotteryDrawControllerTest extends BaseTest {

    @Autowired
    LotteryDrawController lotteryDrawController;

    @Test
    public void navigateToWinners() {
        assertEquals("winners", lotteryDrawController.navigateToWinners().getViewName());
    }

    @Test
    public void navigateMakeDraw() {
        assertEquals("make-draw", lotteryDrawController.navigateMakeDraw().getViewName());
    }
}
