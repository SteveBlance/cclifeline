package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.SecuritySubject;
import com.codaconsultancy.cclifeline.exceptions.SubjectUsernameExistsException;
import com.codaconsultancy.cclifeline.service.SecuritySubjectService;
import com.codaconsultancy.cclifeline.view.SecuritySubjectViewBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class AdminController extends LifelineController {

    @Autowired
    private SecuritySubjectService securitySubjectService;

    private Logger logger = LoggerFactory.getLogger(AdminController.class);


    @RequestMapping(value = "/administrators", method = RequestMethod.GET)
    public ModelAndView navigateToAdministrators() {
        List<SecuritySubject> administrators = securitySubjectService.findAllSecuritySubjects();

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
            return modelAndView("add-administrator").addObject("administrator", securitySubjectViewBean);
        }
        if (!securitySubjectViewBean.getPassword().equals(securitySubjectViewBean.getConfirmPassword())) {
            ModelAndView modelAndView = modelAndView("add-administrator").addObject("administrator", securitySubjectViewBean);
            return addAlertMessage(modelAndView, "danger", "Password and Confirmation don't match");
        }
        if (!passwordRulesMet(securitySubjectViewBean.getPassword())) {
            ModelAndView modelAndView = modelAndView("add-administrator").addObject("administrator", securitySubjectViewBean);
            return addAlertMessage(modelAndView, "danger", "Password must be between 8 and 100 characters and a mixture of uppercase characters, lowercase characters and numbers.");
        }
        try {
            securitySubjectService.registerNewSecuritySubject(securitySubjectViewBean);
        } catch (SubjectUsernameExistsException e) {
            ModelAndView modelAndView = modelAndView("add-administrator").addObject("administrator", securitySubjectViewBean);
            return addAlertMessage(modelAndView, "danger", "Username already exists");
        }

        return navigateToAdministrators();
    }

    private boolean passwordRulesMet(String password) {
        //Between 8 and 100 characters. Must be a mixture of uppercase characters, lowercase characters and numbers.
        StringBuilder patternBuilder = new StringBuilder();
        patternBuilder.append("((?=.*[a-z])");
        patternBuilder.append("(?=.*[A-Z])");
        patternBuilder.append("(?=.*[0-9])");
        patternBuilder.append(".{8,100})");
        String pattern = patternBuilder.toString();
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(password);
        boolean passwordMatches = m.matches();
        return passwordMatches;
    }

    @EventListener
    public void authenticationSuccess(AuthenticationSuccessEvent event) {
        User username = (User) event.getAuthentication().getPrincipal();
        securitySubjectService.registerSuccessfulLogin(username.getUsername());
    }

    @EventListener
    public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) {
        String username = (String) event.getAuthentication().getPrincipal();
        securitySubjectService.registerFailedLoginAttempt(username);
    }
}
