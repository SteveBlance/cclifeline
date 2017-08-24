package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.SecuritySubject;
import com.codaconsultancy.cclifeline.exceptions.SubjectPasswordIncorrectException;
import com.codaconsultancy.cclifeline.exceptions.SubjectUsernameExistsException;
import com.codaconsultancy.cclifeline.service.SecuritySubjectService;
import com.codaconsultancy.cclifeline.view.SecuritySubjectViewBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AdminController extends LifelineController {

    private Logger logger = LoggerFactory.getLogger(AdminController.class);


    @RequestMapping(value = "/administrators", method = RequestMethod.GET)
    public ModelAndView navigateToAdministrators() {
        List<SecuritySubject> administrators = securitySubjectService.findAllSecuritySubjects();

        return modelAndView("administrators").addObject("administrators", administrators);
    }

    @RequestMapping(value = "/add-administrator", method = RequestMethod.GET)
    public ModelAndView navigateToAddAdministrator() {
        SecuritySubjectViewBean administrator = new SecuritySubjectViewBean();
        return addAlertMessage(new ModelAndView("add-administrator").addObject("administrator", administrator), "info", SecuritySubjectService.PASSWORD_RULES_MESSAGE);
    }

    @RequestMapping(value = "/administrator", method = RequestMethod.POST)
    public ModelAndView addNewAdministrator(@Valid @ModelAttribute("subject") SecuritySubjectViewBean securitySubjectViewBean, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.debug("Validation errors for securitySubject: ", securitySubjectViewBean);
            return modelAndView("add-administrator").addObject("administrator", securitySubjectViewBean);
        }
        try {
            securitySubjectService.registerNewSecuritySubject(securitySubjectViewBean);
        } catch (SubjectUsernameExistsException e) {
            ModelAndView modelAndView = modelAndView("add-administrator").addObject("administrator", securitySubjectViewBean);
            return addAlertMessage(modelAndView, "danger", "Username already exists");
        } catch (SubjectPasswordIncorrectException e) {
            ModelAndView modelAndView = modelAndView("add-administrator").addObject("administrator", securitySubjectViewBean);
            return addAlertMessage(modelAndView, "danger", e.getMessage());
        }

        return navigateToAdministrators();
    }

    @RequestMapping(value = "/change-password/{username}", method = RequestMethod.GET)
    private ModelAndView navigateToChangePassword(@PathVariable String username) {
        SecuritySubject securitySubject = securitySubjectService.findByUsername(username);
        return modelAndView("change-password").addObject("user", securitySubject.toViewBean());
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public ModelAndView changePassword(@Valid @ModelAttribute("user") SecuritySubjectViewBean securitySubjectViewBean, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.debug("Validation errors for securitySubject: ", securitySubjectViewBean);
            return navigateToChangePassword(securitySubjectViewBean.getUsername());
        }
        try {
            securitySubjectService.updatePassword(securitySubjectViewBean);
        } catch (SubjectPasswordIncorrectException e) {
            return addAlertMessage(navigateToChangePassword(securitySubjectViewBean.getUsername()), "danger", e.getMessage());
        }

        return modelAndView("index");
    }

//    private boolean passwordRulesMet(String password) {
//        //Between 8 and 100 characters. Must be a mixture of uppercase characters, lowercase characters and numbers.
//        StringBuilder patternBuilder = new StringBuilder();
//        patternBuilder.append("((?=.*[a-z])");
//        patternBuilder.append("(?=.*[A-Z])");
//        patternBuilder.append("(?=.*[0-9])");
//        patternBuilder.append(".{8,100})");
//        String pattern = patternBuilder.toString();
//        Pattern p = Pattern.compile(pattern);
//        Matcher m = p.matcher(password);
//        boolean passwordMatches = m.matches();
//        return passwordMatches;
//    }

    @EventListener
    public void authenticationSuccess(AuthenticationSuccessEvent event) {
        User user = (User) event.getAuthentication().getPrincipal();
        securitySubjectService.registerSuccessfulLogin(user.getUsername());
    }

    @EventListener
    public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) {
        String username = (String) event.getAuthentication().getPrincipal();
        securitySubjectService.registerFailedLoginAttempt(username);
    }
}
