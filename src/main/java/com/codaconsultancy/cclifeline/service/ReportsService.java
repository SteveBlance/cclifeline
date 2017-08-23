package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.MemberTypeTotal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportsService extends LifelineService {

    public List<MemberTypeTotal> getMemberTypeTotals() {
        return new ArrayList<>();
    }
}

