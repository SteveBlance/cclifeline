package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.LotteryDraw;
import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.domain.MemberTypeTotal;
import com.codaconsultancy.cclifeline.domain.Prize;
import com.codaconsultancy.cclifeline.service.*;
import com.codaconsultancy.cclifeline.view.MemberViewBean;
import com.codaconsultancy.cclifeline.view.ReportViewBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ReportsController extends LifelineController {

    private final ReportsService reportsService;

    private Logger logger = LoggerFactory.getLogger(ReportsController.class);

    public ReportsController(SecuritySubjectService securitySubjectService, NotificationService notificationService, ReportsService reportsService, MemberService memberService, LotteryDrawService lotteryDrawService) {
        super(securitySubjectService, notificationService, memberService, lotteryDrawService);
        this.reportsService = reportsService;
    }

    @RequestMapping(value = "/reports", method = RequestMethod.GET)
    public ModelAndView navigateToReports() {
        return modelAndView("reports").addObject("reportConfig", new ReportViewBean());
    }

    @RequestMapping(value = "/memberTypeTotals", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<MemberTypeTotal> helloRest() {
        return reportsService.getMemberTypeTotals();
    }

    @RequestMapping(value = "/export-report", method = RequestMethod.POST, produces = "text/csv")
    @ResponseBody
    public String exportReport(@Valid @ModelAttribute("reportConfig") ReportViewBean reportConfig, BindingResult bindingResult) {
        return allMembersCSV();
    }

    private String allDrawsCSV() {
        List<LotteryDraw> lotteryDraws = lotteryDrawService.fetchAllLotteryDraws();
        StringBuilder drawsCSV = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<Prize> prizes;
        StringBuilder winners = new StringBuilder();
        Member winner;
        for (LotteryDraw lotteryDraw : lotteryDraws) {
            drawsCSV.append(sdf.format(lotteryDraw.getDrawDate())).append(",")
                    .append(lotteryDraw.getName()).append(",")
                    .append(",[");
            prizes = lotteryDraw.getPrizes();
            for (Prize prize : prizes) {
                winner = prize.getWinner();
                winners.append(prize.getPrize()).append(":")
                        .append(winner.getMembershipNumber()).append("-")
                        .append(winner.getForename()).append(" ")
                        .append(winner.getSurname()).append(" ");
            }
            drawsCSV.append(winners.toString()).append("]\n");
        }
        return drawsCSV.toString();
    }

    private String allMembersCSV() {
        List<MemberViewBean> allMembers = memberService.findAllMembers();
        StringBuilder memberCSV = new StringBuilder();
        for (MemberViewBean member : allMembers) {
            memberCSV.append(member.getMembershipNumber()).append(",")
                    .append(member.getForename()).append(",")
                    .append(member.getSurname()).append(",")
                    .append(member.getStatus()).append(",")
                    .append(member.getMembershipType()).append(",")
                    .append(member.getPayerType()).append(",")
                    .append(member.getEmail()).append(",")
                    .append(member.getLandlineNumber()).append(",")
                    .append(member.getMobileNumber()).append(",")
                    .append("eligible-for-draw:").append(member.isEligibleForDrawStored())
                    .append("\n");
        }
        return memberCSV.toString();
    }
}
