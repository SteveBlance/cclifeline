package com.codaconsultancy.cclifeline.domain;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class PrizeTest {

    private Prize prize;

    @Before
    public void setUp() throws Exception {
        LotteryDraw lotteryDraw = new LotteryDraw();
        lotteryDraw.setId(555L);
        prize = new Prize();
        prize.setId(68L);
        prize.setLotteryDraw(lotteryDraw);
        prize.setPrize("£100");
        Member winner = new Member();
        winner.setMembershipNumber(1234L);
        prize.setWinner(winner);
        prize.setPrizeCollected(false);
    }

    @Test
    public void getId() throws Exception {
        assertEquals(68L, prize.getId().longValue());
    }

    @Test
    public void getPrize() throws Exception {
        assertEquals("£100", prize.getPrize());
    }

    @Test
    public void getWinner() throws Exception {
        assertEquals(1234L, prize.getWinner().getMembershipNumber().longValue());

    }

    @Test
    public void isPrizeCollected() throws Exception {
        assertFalse(prize.isPrizeCollected());
        prize.setPrizeCollected(true);
        assertTrue(prize.isPrizeCollected());
    }

    @Test
    public void getPrizeCollectedDate() throws Exception {
        assertNull(prize.getPrizeCollectedDate());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        Date collectedDate = sdf.parse("23/05/2017");

        prize.setPrizeCollectedDate(collectedDate);

        assertEquals("23/05/2017", sdf.format(prize.getPrizeCollectedDate()));
    }

    @Test
    public void getLotteryDraw() throws Exception {
        assertEquals(555L, prize.getLotteryDraw().getId().longValue());
    }

}