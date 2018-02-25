package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.MemberTypeTotal;
import com.codaconsultancy.cclifeline.domain.Report;
import com.codaconsultancy.cclifeline.repositories.MemberRepository;
import com.codaconsultancy.cclifeline.repositories.ReportRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReportsService.class)
public class ReportsServiceTest extends LifelineServiceTest {

    @Autowired
    private ReportsService reportsService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private ReportRepository reportRepository;

    @Test
    public void getMemberTypeTotals() throws Exception {
        List<MemberTypeTotal> memberTypeTotals = reportsService.getMemberTypeTotals();
        assertTrue(memberTypeTotals instanceof ArrayList);
        assertTrue(memberTypeTotals.isEmpty());
    }

    @Test
    public void captureStats() {
        reportsService.captureStats();
        verify(memberRepository, times(1)).findEligibleMembers();
        verify(memberRepository, times(1)).findLapsedMembers();
        verify(memberRepository, times(1)).findFormerMembers();

        verify(reportRepository, times(3)).save(Matchers.any(Report.class));
    }

}