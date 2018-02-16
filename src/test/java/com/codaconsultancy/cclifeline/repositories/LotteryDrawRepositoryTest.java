package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.common.TestHelper;
import com.codaconsultancy.cclifeline.domain.LotteryDraw;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = LotteryDraw.class)

public class LotteryDrawRepositoryTest extends BaseTest {

    @Autowired
    private LotteryDrawRepository lotteryDrawRepository;

    private LotteryDraw oldDraw;
    private LotteryDraw lastDraw;
    private LotteryDraw futureDraw;


    @Before
    public void setup() {
        oldDraw = TestHelper.newLotteryDraw("Celtic", DateTime.now().minusWeeks(2).toDate(), "Steve");
        lastDraw = TestHelper.newLotteryDraw("Dundee", DateTime.now().minusWeeks(1).toDate(), "Steve");
        futureDraw = TestHelper.newLotteryDraw("Aberdeen", DateTime.now().plusWeeks(2).toDate(), "Steve");
        entityManager.persist(oldDraw);
        entityManager.persist(lastDraw);
        entityManager.persist(futureDraw);
    }

    @Test
    public void findLastDraw() {
        LotteryDraw draw = lotteryDrawRepository.findLastDraw();
        assertEquals(3, lotteryDrawRepository.count());
        assertEquals("Dundee", draw.getName());
    }

    @After
    public void teardown() {
        entityManager.remove(oldDraw);
        entityManager.remove(lastDraw);
        entityManager.remove(futureDraw);
    }
}