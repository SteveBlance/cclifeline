package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.LotteryDraw;
import com.codaconsultancy.cclifeline.domain.SecuritySubject;
import com.codaconsultancy.cclifeline.service.LotteryDrawService;
import com.codaconsultancy.cclifeline.service.SecurityService;
import com.codaconsultancy.cclifeline.view.LotteryDrawViewBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class LotteryDrawController {

    @Autowired
    private LotteryDrawService lotteryDrawService;

    @Autowired
    private SecurityService securityService;

    @RequestMapping(value = "/draws", method = RequestMethod.GET)
    public ModelAndView navigateToWinners() {
        List<LotteryDraw> lotteryDraws = lotteryDrawService.fetchAllLotteryDraws();
        return modelAndView("draws").addObject("lotteryDraws", lotteryDraws);
    }

    @RequestMapping(value = "/make-draw", method = RequestMethod.GET)
    public ModelAndView navigateMakeDraw() {
        LotteryDrawViewBean lotteryDrawViewBean = new LotteryDrawViewBean();
        List<SecuritySubject> securitySubjects = securityService.findAllSecuritySubjects();
        return modelAndView("make-draw").addObject("lotteryDraw", lotteryDrawViewBean).addObject("securitySubjects", securitySubjects);
    }

    private ModelAndView modelAndView(String page) {
        return new ModelAndView(page);
    }
}
