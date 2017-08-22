package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.SecuritySubject;
import com.codaconsultancy.cclifeline.repositories.BaseTest;
import com.codaconsultancy.cclifeline.service.NotificationService;
import com.codaconsultancy.cclifeline.service.SecuritySubjectService;
import com.codaconsultancy.cclifeline.view.SecuritySubjectViewBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdminController.class)
public class AdminControllerTest extends BaseTest {

    @Autowired
    private AdminController adminController;

    @MockBean
    NotificationService notificationService;

    @MockBean
    private SecuritySubjectService securitySubjectService;


    @Test
    public void navigateToAdministrators() throws Exception {
        List<SecuritySubject> administrators = new ArrayList<>();
        SecuritySubject ross = new SecuritySubject();
        ross.setUsername("ross");
        SecuritySubject steve = new SecuritySubject();
        administrators.add(ross);
        administrators.add(steve);
        when(securitySubjectService.findAllSecuritySubjects()).thenReturn(administrators);

        ModelAndView modelAndView = adminController.navigateToAdministrators();

        assertEquals("administrators", modelAndView.getViewName());
        List foundAdministrators = (List) modelAndView.getModel().get("administrators");
        assertEquals(2, foundAdministrators.size());
        assertEquals("ross", ((SecuritySubject) foundAdministrators.get(0)).getUsername());
    }

    @Test
    public void navigateToAddAdministrator() throws Exception {

        ModelAndView modelAndView = adminController.navigateToAddAdministrator();
        assertTrue(modelAndView.getModel().get("administrator") instanceof SecuritySubjectViewBean);
        assertEquals("add-administrator", modelAndView.getViewName());


    }
//
//    @Test
//    public void encryptPassword() {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String encodedPassword = encoder.encode("password");
//        assertEquals("", encodedPassword);
//    }

}