package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.SecuritySubject;
import com.codaconsultancy.cclifeline.repositories.SecuritySubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityService {

    @Autowired
    private SecuritySubjectRepository securitySubjectRepository;

    public List<SecuritySubject> findAllSecuritySubjects() {
        return securitySubjectRepository.findAll();
    }

}
