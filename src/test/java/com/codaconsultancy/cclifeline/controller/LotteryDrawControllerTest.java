package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.LotteryDraw;
import com.codaconsultancy.cclifeline.domain.SecuritySubject;
import com.codaconsultancy.cclifeline.repositories.BaseTest;
import com.codaconsultancy.cclifeline.service.LotteryDrawService;
import com.codaconsultancy.cclifeline.service.SecurityService;
import com.codaconsultancy.cclifeline.view.LotteryDrawViewBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.AbstractBindingResult;
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
    private SecurityService securityService;

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
        List<SecuritySubject> securitySubjects = new ArrayList<>();
        SecuritySubject securitySubject1 = new SecuritySubject();
        SecuritySubject securitySubject2 = new SecuritySubject();
        securitySubjects.add(securitySubject1);
        securitySubjects.add(securitySubject2);
        when(securityService.findAllSecuritySubjects()).thenReturn(securitySubjects);

        ModelAndView modelAndView = lotteryDrawController.navigateToPrepareDraw();

        verify(securityService, times(1)).findAllSecuritySubjects();
        assertEquals("prepare-draw", modelAndView.getViewName());
        assertTrue(modelAndView.getModel().get("lotteryDraw") instanceof LotteryDrawViewBean);
        List<SecuritySubject> retrievedSubjects = (List<SecuritySubject>) modelAndView.getModel().get("securitySubjects");
        assertEquals(2, retrievedSubjects.size());
    }

//    @Test
//    public void navigateToMakeDrawWithLotteryDrawBean() {
//        List<SecuritySubject> securitySubjects = new ArrayList<>();
//        SecuritySubject securitySubject1 = new SecuritySubject();
//        SecuritySubject securitySubject2 = new SecuritySubject();
//        securitySubjects.add(securitySubject1);
//        securitySubjects.add(securitySubject2);
//        when(securityService.findAllSecuritySubjects()).thenReturn(securitySubjects);
//        LotteryDrawViewBean lotteryDrawBean = new LotteryDrawViewBean();
//
//        ModelAndView modelAndView = lotteryDrawController.navigateToMakeDraw(lotteryDrawBean);
//
//        verify(securityService, times(1)).findAllSecuritySubjects();
//        assertEquals("make-draw", modelAndView.getViewName());
//        assertSame(lotteryDrawBean, modelAndView.getModel().get("lotteryDraw"));
//        List<SecuritySubject> retrievedSubjects = (List<SecuritySubject>) modelAndView.getModel().get("securitySubjects");
//        assertEquals(2, retrievedSubjects.size());
//    }

    @Test
    public void prepareDraw_success() {
        BindingResult bindingResult = new AbstractBindingResult("lotteryDraw") {
            @Override
            public Object getTarget() {
                return null;
            }

            @Override
            protected Object getActualFieldValue(String s) {
                return null;
            }
        };
        LotteryDrawViewBean lotteryDrawViewBean = new LotteryDrawViewBean();
        lotteryDrawViewBean.setNumberOfPrizes(7);

        ModelAndView modelAndView = lotteryDrawController.prepareDraw(lotteryDrawViewBean, bindingResult);

        assertEquals(7, lotteryDrawViewBean.getPrizes().size());
        assertEquals("make-draw", modelAndView.getViewName());
    }

    @Test
    public void prepareDraw_validationErrors() {
        BindingResult bindingResult = new AbstractBindingResult("lotteryDraw") {
            @Override
            public Object getTarget() {
                return null;
            }

            @Override
            protected Object getActualFieldValue(String s) {
                return null;
            }
        };
        bindingResult.addError(new ObjectError("surname", "Draw master not set"));
        LotteryDrawViewBean lotteryDrawViewBean = new LotteryDrawViewBean();
        lotteryDrawViewBean.setNumberOfPrizes(7);

        ModelAndView modelAndView = lotteryDrawController.prepareDraw(lotteryDrawViewBean, bindingResult);

        assertEquals(0, lotteryDrawViewBean.getPrizes().size());
        assertEquals("prepare-draw", modelAndView.getViewName());
    }

}
