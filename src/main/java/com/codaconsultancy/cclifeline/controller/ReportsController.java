package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.MemberTypeTotal;
import com.codaconsultancy.cclifeline.service.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ReportsController extends LifelineController {

    @Autowired
    private ReportsService reportsService;

    @RequestMapping(value = "/memberTypeTotals", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<MemberTypeTotal> helloRest() {
        return reportsService.getMemberTypeTotals();
    }
}
