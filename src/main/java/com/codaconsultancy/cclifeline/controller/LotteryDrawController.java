package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.LotteryDraw;
import com.codaconsultancy.cclifeline.domain.Prize;
import com.codaconsultancy.cclifeline.service.LotteryDrawService;
import com.codaconsultancy.cclifeline.service.MemberService;
import com.codaconsultancy.cclifeline.view.LotteryDrawViewBean;
import com.codaconsultancy.cclifeline.view.MemberViewBean;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class LotteryDrawController extends LifelineController {

    public static final String LOTTERY_DRAWS_PAGE = "draws";
    public static final String PREPARE_DRAW_PAGE = "prepare-draw";
    public static final String MAKE_DRAW_PAGE = "make-draw";
    public static final String DRAW_RESULT_PAGE = "draw-result";
    @Autowired
    private LotteryDrawService lotteryDrawService;

    @Autowired
    private MemberService memberService;

    private Logger logger = LoggerFactory.getLogger(LotteryDrawController.class);


    @RequestMapping(value = "/draws", method = RequestMethod.GET)
    public ModelAndView navigateToWinners() {
        List<LotteryDraw> lotteryDraws = lotteryDrawService.fetchAllLotteryDraws();
        Long totalNumberOfWinners = lotteryDrawService.countAllWinners();
        return modelAndView(LOTTERY_DRAWS_PAGE).addObject("lotteryDraws", lotteryDraws).addObject("totalNumberOfWinners", totalNumberOfWinners);
    }

    @RequestMapping(value = "/prepare-draw", method = RequestMethod.GET)
    public ModelAndView navigateToPrepareDraw() {
        LotteryDrawViewBean lotteryDrawViewBean = new LotteryDrawViewBean();
        Date today = DateTime.now().toDate();
        lotteryDrawViewBean.setDrawDate(today);
        lotteryDrawViewBean.setDrawMaster(loggedInUser());
        return modelAndView(PREPARE_DRAW_PAGE).addObject("lotteryDraw", lotteryDrawViewBean);
    }

    @RequestMapping(value = "/prepare-draw", method = RequestMethod.POST)
    public ModelAndView prepareDraw(@Valid @ModelAttribute("lotteryDraw") LotteryDrawViewBean lotteryDrawViewBean, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.debug("Validation errors for lotteryDraw: ", lotteryDrawViewBean);
            return navigateToPrepareDraw();
        }
        Integer numberOfPrizes = lotteryDrawViewBean.getNumberOfPrizes();
        List<Prize> drawPrizes = new ArrayList<>(numberOfPrizes);
        for (int i = 0; i < numberOfPrizes; i++) {
            drawPrizes.add(new Prize());
        }
        lotteryDrawViewBean.setPrizes(drawPrizes);

        return navigateToMakeDraw(lotteryDrawViewBean);
    }

    @RequestMapping(value = "/make-draw", method = RequestMethod.GET)
    private ModelAndView navigateToMakeDraw(LotteryDrawViewBean lotteryDrawViewBean) {
        return modelAndView(MAKE_DRAW_PAGE).addObject("lotteryDraw", lotteryDrawViewBean);
    }

    @RequestMapping(value = "/make-draw", method = RequestMethod.POST)
    public ModelAndView makeDraw(@Valid @ModelAttribute("lotteryDraw") LotteryDrawViewBean lotteryDrawViewBean, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.debug("Validation errors for lotteryDraw: ", lotteryDrawViewBean);
            return navigateToPrepareDraw();
        }
        List<Prize> prizes = lotteryDrawViewBean.getPrizes();
        List<MemberViewBean> membersDrawEntries = memberService.fetchMemberDrawEntries();

        for (Prize prize : prizes) {
            int index = ThreadLocalRandom.current().nextInt(membersDrawEntries.size());
            MemberViewBean winner = membersDrawEntries.get(index);
            prize.setWinner(winner.toEntity());
            removeAllEntriesForWinner(winner.getId(), membersDrawEntries);
        }
        lotteryDrawViewBean.setNumberOfPrizes(prizes.size());

        lotteryDrawService.saveLotteryDraw(lotteryDrawViewBean.toEntity());

        notificationService.logLotteryDraw(lotteryDrawViewBean.getName());

        return navigateToViewDrawResult(lotteryDrawViewBean);
    }

    private void removeAllEntriesForWinner(Long winnerId, List<MemberViewBean> membersDrawEntries) {
        for (Iterator<MemberViewBean> iterator = membersDrawEntries.iterator(); iterator.hasNext(); ) {
            MemberViewBean member = iterator.next();
            Long id = member.getId();
            if (id.equals(winnerId)) {
                iterator.remove();
            }
        }
    }

    private ModelAndView navigateToViewDrawResult(LotteryDrawViewBean lotteryDrawViewBean) {
        return modelAndView(DRAW_RESULT_PAGE).addObject("lotteryDraw", lotteryDrawViewBean);
    }

}
