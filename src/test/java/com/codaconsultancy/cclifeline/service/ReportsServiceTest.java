package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.MemberTypeTotal;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ReportsServiceTest {

    @Test
    public void getMemberTypeTotals() throws Exception {
        List<MemberTypeTotal> memberTypeTotals = new ReportsService().getMemberTypeTotals();
        assertTrue(memberTypeTotals instanceof ArrayList);
        assertTrue(memberTypeTotals.isEmpty());
    }

}