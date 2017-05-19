package com.codaconsultancy.cclifeline.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LotteryDrawController {

    @RequestMapping(value = "/winners", method = RequestMethod.GET)
    public ModelAndView navigateToWinners() {
        return modelAndView("winners");
    }

    @RequestMapping(value = "/make-draw", method = RequestMethod.GET)
    public ModelAndView navigateMakeDraw() {
        return modelAndView("make-draw");
    }

    private ModelAndView modelAndView(String page) {
        return new ModelAndView(page);
    }
}
