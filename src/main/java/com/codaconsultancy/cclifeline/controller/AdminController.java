package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.exceptions.SubjectUsernameExistsException;
import com.codaconsultancy.cclifeline.service.SecuritySubjectService;
import com.codaconsultancy.cclifeline.view.AdministratorViewBean;
import com.codaconsultancy.cclifeline.view.SecuritySubjectViewBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController extends LifelineController {

    @Autowired
    private SecuritySubjectService securitySubjectService;

    private Logger logger = LoggerFactory.getLogger(AdminController.class);


    @RequestMapping(value = "/administrators", method = RequestMethod.GET)
    public ModelAndView navigateToAdministrators() {
        List<AdministratorViewBean> administrators = new ArrayList<>();
        //TODO: remove stubbed data and implement properly
        AdministratorViewBean steve = new AdministratorViewBean();
        steve.setForename("Steve");
        steve.setSurname("Jone");
        steve.setUsername("stevej");
        steve.setEmail("stevej@email.com");
        steve.setLandlineNumber("01383 766533");
        steve.setLandlineNumber("07766 766512");
        AdministratorViewBean ross = new AdministratorViewBean();
        ross.setForename("Ross");
        ross.setSurname("Wilson");
        ross.setUsername("rossW");
        ross.setEmail("rossw@email.co.uk");
        ross.setLandlineNumber("01383 644339");
        ross.setLandlineNumber("07754 456098");
        administrators.add(steve);
        administrators.add(ross);

        return modelAndView("administrators").addObject("administrators", administrators);
    }

    @RequestMapping(value = "/add-administrator", method = RequestMethod.GET)
    public ModelAndView navigateToAddAdministrator() {
        SecuritySubjectViewBean administrator = new SecuritySubjectViewBean();
        return modelAndView("add-administrator").addObject("administrator", administrator);
    }

    @RequestMapping(value = "/administrator", method = RequestMethod.POST)
    public ModelAndView addNewAdministrator(@Valid @ModelAttribute("subject") SecuritySubjectViewBean securitySubjectViewBean, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.debug("Validation errors for securitySubject: ", securitySubjectViewBean);
            return navigateToAddAdministrator();
        }
        if (!securitySubjectViewBean.getPassword().equals(securitySubjectViewBean.getConfirmPassword())) {
            return addAlertMessage(navigateToAddAdministrator(), "danger", "Confirm Password and Password don not match");
        }
        try {
            securitySubjectService.registerNewSecuritySubject(securitySubjectViewBean);
        } catch (SubjectUsernameExistsException e) {
            return addAlertMessage(navigateToAddAdministrator(), "danger", "Username already exists");
        }

        return navigateToAdministrators();
    }
}
