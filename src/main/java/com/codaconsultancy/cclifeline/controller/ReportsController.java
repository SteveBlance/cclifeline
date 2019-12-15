package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.MemberTypeTotal;
import com.codaconsultancy.cclifeline.service.NotificationService;
import com.codaconsultancy.cclifeline.service.ReportsService;
import com.codaconsultancy.cclifeline.service.SecuritySubjectService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ReportsController extends LifelineController {

    private final ReportsService reportsService;

    public ReportsController(SecuritySubjectService securitySubjectService, NotificationService notificationService, ReportsService reportsService) {
        super(securitySubjectService, notificationService);
        this.reportsService = reportsService;
    }

    @RequestMapping(value = "/memberTypeTotals", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<MemberTypeTotal> helloRest() {
        return reportsService.getMemberTypeTotals();
    }
}
