package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.view.AdministratorViewBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController extends LifelineController {

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
        AdministratorViewBean administrator = new AdministratorViewBean();
        return modelAndView("add-administrator").addObject("administrator", administrator);
    }
}
