package com.codaconsultancy.cclifeline.view;

import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.domain.Prize;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LotteryDrawViewBeanTest {

    private LotteryDrawViewBean lotteryDraw;

    @Before
    public void setup() throws Exception {
        lotteryDraw = new LotteryDrawViewBean();
        lotteryDraw.setId(98L);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        Date drawDate = sdf.parse("01/05/2017");
        lotteryDraw.setDrawDate(drawDate);
        lotteryDraw.setName("March Mayhem");
        lotteryDraw.setDrawMaster("Ross");

        List<Prize> prizes = new ArrayList<>();
        Prize prize1 = new Prize();
        prize1.setId(1L);
        prize1.setWinner(newMember(9988L));
        prizes.add(prize1);

        Prize prize2 = new Prize();
        prize2.setId(2L);
        prize2.setWinner(newMember(1010L));
        prizes.add(prize2);
        lotteryDraw.setNumberOfPrizes(prizes.size());
        lotteryDraw.setPrizes(prizes);
    }

    private Member newMember(long membershipNumber) {
        Member member = new Member();
        member.setMembershipNumber(membershipNumber);
        return member;
    }

    @Test
    public void getId() throws Exception {
        assertEquals(98L, lotteryDraw.getId().longValue());
    }

    @Test
    public void getDrawDate() throws Exception {
        Date drawDate = lotteryDraw.getDrawDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        assertEquals("01/05/2017", sdf.format(drawDate));
    }

    @Test
    public void getName() throws Exception {
        assertEquals("March Mayhem", lotteryDraw.getName());
    }

    @Test
    public void getDrawMaster() throws Exception {
        assertEquals("Ross", lotteryDraw.getDrawMaster());
    }

    @Test
    public void getPrizes() throws Exception {
        assertEquals(2, lotteryDraw.getPrizes().size());
        assertEquals(1L, lotteryDraw.getPrizes().get(0).getId().longValue());
        assertEquals(2L, lotteryDraw.getPrizes().get(1).getId().longValue());
    }

    @Test
    public void getNumberOfPrizes() throws Exception {
        assertEquals(2, lotteryDraw.getNumberOfPrizes().intValue());
    }

}