package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.MemberTypeTotal;
import com.codaconsultancy.cclifeline.domain.Report;
import com.codaconsultancy.cclifeline.repositories.MemberRepository;
import com.codaconsultancy.cclifeline.repositories.ReportRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReportsService extends LifelineService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReportRepository reportRepository;

    public List<MemberTypeTotal> getMemberTypeTotals() {
        return new ArrayList<>();
    }

    public void captureStats() {
        int numberOfEligibleMembers = memberRepository.findEligibleMembers().size();
        int numberOfLapsedMembers = memberRepository.findLapsedMembers().size();
        int numberOfCancelledMembers = memberRepository.findFormerMembers().size();
        Date date = DateTime.now().monthOfYear().getDateTime().toDate();

        saveReport(Report.NUMBER_OF_ELIGIBLE_MEMBERS, numberOfEligibleMembers, date);
        saveReport(Report.NUMBER_OF_LAPSED_MEMBERS, numberOfLapsedMembers, date);
        saveReport(Report.NUMBER_OF_CANCELLED_MEMBERS, numberOfCancelledMembers, date);

        // todo: payments total v last month
    }

    private void saveReport(String name, int intValue, Date date) {
        Report report = new Report();
        report.setName(name);
        report.setReportDate(date);
        report.setIntValue(intValue);
        reportRepository.save(report);
    }
}

