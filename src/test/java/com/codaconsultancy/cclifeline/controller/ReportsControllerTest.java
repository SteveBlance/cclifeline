package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.MemberTypeTotal;
import com.codaconsultancy.cclifeline.repositories.BaseTest;
import com.codaconsultancy.cclifeline.service.NotificationService;
import com.codaconsultancy.cclifeline.service.ReportsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReportsController.class)
public class ReportsControllerTest extends BaseTest {

    @Autowired
    ReportsController reportsController;

    @MockBean
    ReportsService reportsService;


    @MockBean
    NotificationService notificationService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void navigateToReports() {
        assertEquals("reports", reportsController.navigateToReports().getViewName());
    }

    @Test
    public void helloRest() throws Exception {
        ArrayList<MemberTypeTotal> memberTypeTotals = new ArrayList<>();

        when(reportsService.getMemberTypeTotals()).thenReturn(memberTypeTotals);

        List modelAndView = reportsController.helloRest();

        verify(reportsService, times(1)).getMemberTypeTotals();
        assertSame(modelAndView, memberTypeTotals);
    }

}