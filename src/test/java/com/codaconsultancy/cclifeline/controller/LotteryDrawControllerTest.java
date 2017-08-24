package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.LotteryDraw;
import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.domain.Prize;
import com.codaconsultancy.cclifeline.domain.SecuritySubject;
import com.codaconsultancy.cclifeline.repositories.BaseTest;
import com.codaconsultancy.cclifeline.service.LotteryDrawService;
import com.codaconsultancy.cclifeline.service.MemberService;
import com.codaconsultancy.cclifeline.service.NotificationService;
import com.codaconsultancy.cclifeline.view.LotteryDrawViewBean;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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

    @MockBean
    private MemberService memberService;

    @MockBean
    private NotificationService notificationService;

    @Before
    public void setup() {
        when(securitySubjectService.findByUsername(any(String.class))).thenReturn(new SecuritySubject());
    }

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
    public void navigateToPrepareDraw() {
        ModelAndView modelAndView = lotteryDrawController.navigateToPrepareDraw();

        assertEquals("prepare-draw", modelAndView.getViewName());
        Object lotteryDraw = modelAndView.getModel().get("lotteryDraw");
        assertTrue(lotteryDraw instanceof LotteryDrawViewBean);
        assertEquals("Bob", ((LotteryDrawViewBean) lotteryDraw).getDrawMaster());
        assertEquals(DateTime.now().getDayOfYear(), new DateTime(((LotteryDrawViewBean) lotteryDraw).getDrawDate()).getDayOfYear());
    }

    @Test
    public void prepareDraw_success() {
        BindingResult bindingResult = getBindingResult("lotteryDraw");
        LotteryDrawViewBean lotteryDrawViewBean = new LotteryDrawViewBean();
        lotteryDrawViewBean.setNumberOfPrizes(7);

        ModelAndView modelAndView = lotteryDrawController.prepareDraw(lotteryDrawViewBean, bindingResult);

        assertEquals(7, lotteryDrawViewBean.getPrizes().size());
        assertEquals("make-draw", modelAndView.getViewName());
    }

    @Test
    public void prepareDraw_validationErrors() {
        BindingResult bindingResult = getBindingResult("lotteryDraw");
        bindingResult.addError(new ObjectError("surname", "Draw master not set"));
        LotteryDrawViewBean lotteryDrawViewBean = new LotteryDrawViewBean();
        lotteryDrawViewBean.setNumberOfPrizes(7);

        ModelAndView modelAndView = lotteryDrawController.prepareDraw(lotteryDrawViewBean, bindingResult);

        assertEquals(0, lotteryDrawViewBean.getPrizes().size());
        assertEquals("prepare-draw", modelAndView.getViewName());
    }

    @Test
    public void makeDraw_success() {
        BindingResult bindingResult = getBindingResult("lotteryDraw");
        LotteryDrawViewBean lotteryDrawViewBean = new LotteryDrawViewBean();
        List<Prize> newPrizesWithoutWinners = new ArrayList<>();
        Prize prize1 = new Prize();
        prize1.setPrize("£250");
        newPrizesWithoutWinners.add(prize1);
        Prize prize2 = new Prize();
        prize2.setPrize("£100");
        newPrizesWithoutWinners.add(prize2);
        Prize prize3 = new Prize();
        prize2.setPrize("£50");
        newPrizesWithoutWinners.add(prize3);
        lotteryDrawViewBean.setPrizes(newPrizesWithoutWinners);
        List<Member> memberDrawEntries = new ArrayList<>();
        Member member1 = new Member();
        member1.setId(1L);
        Member member2 = new Member();
        member2.setId(2L);
        Member member3 = new Member();
        member3.setId(3L);
        memberDrawEntries.add(member1);
        memberDrawEntries.add(member1);
        memberDrawEntries.add(member1);
        memberDrawEntries.add(member2);
        memberDrawEntries.add(member3);
        when(memberService.fetchMemberDrawEntries()).thenReturn(memberDrawEntries);

        ArgumentCaptor<LotteryDraw> lotteryDrawArgumentCaptor = ArgumentCaptor.forClass(LotteryDraw.class);
        when(lotteryDrawService.saveLotteryDraw(lotteryDrawArgumentCaptor.capture())).thenReturn(new LotteryDraw());

        assertEquals(5, memberDrawEntries.size());
        assertNull(prize1.getWinner());
        assertNull(prize2.getWinner());
        assertNull(prize3.getWinner());

        ModelAndView modelAndView = lotteryDrawController.makeDraw(lotteryDrawViewBean, bindingResult);

        verify(memberService, times(1)).fetchMemberDrawEntries();
        verify(lotteryDrawService, times(1)).saveLotteryDraw(lotteryDrawArgumentCaptor.capture());
        verify(notificationService, times(1)).logLotteryDraw(lotteryDrawViewBean.getName());

        assertEquals(3, lotteryDrawArgumentCaptor.getValue().getPrizes().size());
        assertEquals("draw-result", modelAndView.getViewName());
        assertEquals(0, memberDrawEntries.size());
        assertNotNull(prize1.getWinner());
        assertNotNull(prize2.getWinner());
        assertNotNull(prize3.getWinner());
    }

    @Test
    public void makeDraw_validationErrors() {
        BindingResult bindingResult = getBindingResult("lotteryDraw");
        bindingResult.addError(new ObjectError("draw", "Draw date not set"));
        LotteryDrawViewBean lotteryDrawViewBean = new LotteryDrawViewBean();

        ModelAndView modelAndView = lotteryDrawController.makeDraw(lotteryDrawViewBean, bindingResult);

        verify(memberService, never()).fetchMemberDrawEntries();
        verify(lotteryDrawService, never()).saveLotteryDraw(any(LotteryDraw.class));
        verify(notificationService, never()).logLotteryDraw(lotteryDrawViewBean.getName());


        assertEquals("prepare-draw", modelAndView.getViewName());
    }

}
