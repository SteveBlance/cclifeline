package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.repositories.BaseTest;
import com.codaconsultancy.cclifeline.service.NotificationService;
import com.codaconsultancy.cclifeline.view.AdministratorViewBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
//@EnableJpaRepositories(basePackages = {"com.codaconsultancy.cclifeline.repositories"})
@SpringBootTest(classes = AdminController.class)
public class AdminControllerTest extends BaseTest {

    @Autowired
    private AdminController adminController;

    @MockBean
    NotificationService notificationService;

    @Test
    public void navigateToAdministrators() throws Exception {
        ModelAndView modelAndView = adminController.navigateToAdministrators();
        assertEquals("administrators", modelAndView.getViewName());
        List administrators = (List) modelAndView.getModel().get("administrators");
        assertEquals(2, administrators.size());
        assertEquals("stevej", ((AdministratorViewBean) administrators.get(0)).getUsername());
    }

    @Test
    public void navigateToAddAdministrator() throws Exception {

        ModelAndView modelAndView = adminController.navigateToAddAdministrator();
        assertTrue(modelAndView.getModel().get("administrator") instanceof AdministratorViewBean);
        assertEquals("add-administrator", modelAndView.getViewName());


    }

}