package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.SecuritySubject;
import com.codaconsultancy.cclifeline.repositories.SecuritySubjectRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@EntityScan("com.codaconsultancy.cclifeline.domain")
@SpringBootTest(classes = SecurityService.class)
public class SecurityServiceTest {

    @Autowired
    private SecurityService securityService;

    @MockBean
    private SecuritySubjectRepository securitySubjectRepository;

    @Test
    public void findAllSecuritySubjects() throws Exception {

        List<SecuritySubject> securitySubjects = new ArrayList<>();
        SecuritySubject securitySubject1 = new SecuritySubject();
        securitySubject1.setForename("Jim");
        SecuritySubject securitySubject2 = new SecuritySubject();
        securitySubject2.setForename("Debbie");
        SecuritySubject securitySubject3 = new SecuritySubject();
        securitySubject3.setForename("Helen");
        securitySubjects.add(securitySubject1);
        securitySubjects.add(securitySubject2);
        securitySubjects.add(securitySubject3);
        when(securitySubjectRepository.findAll()).thenReturn(securitySubjects);

        List<SecuritySubject> foundSecuritySubjects = securityService.findAllSecuritySubjects();

        assertEquals(3, foundSecuritySubjects.size());
        assertEquals("Jim", foundSecuritySubjects.get(0).getForename());
        assertEquals("Debbie", foundSecuritySubjects.get(1).getForename());
        assertEquals("Helen", foundSecuritySubjects.get(2).getForename());
    }

}