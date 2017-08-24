package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.Notification;
import com.codaconsultancy.cclifeline.domain.SecuritySubject;
import com.codaconsultancy.cclifeline.service.NotificationService;
import com.codaconsultancy.cclifeline.service.SecuritySubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
class LifelineController {

    @Autowired
    protected SecuritySubjectService securitySubjectService;

    @Autowired
    NotificationService notificationService;

    String loggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

    ModelAndView modelAndView(String page) {
        ModelAndView modelAndView;
        List<Notification> notifications = notificationService.fetchLatestNotifications();
        String username = loggedInUser();
        SecuritySubject securitySubject = !username.equals("anonymousUser") ? securitySubjectService.findByUsername(username) : null;
        if (securitySubject != null && securitySubject.isPasswordToBeChanged()) {
            modelAndView = addAlertMessage(new ModelAndView("change-password").addObject("user", securitySubject.toViewBean()), "info", SecuritySubjectService.PASSWORD_RULES_MESSAGE);
        } else {
            modelAndView = new ModelAndView(page).addObject("loggedInUser", username)
                    .addObject("notifications", notifications).addObject("alertMessage", "");
        }
        return modelAndView;
    }


    ModelAndView addAlertMessage(ModelAndView modelAndView, String alertType, String messageText) {
        return modelAndView.addObject("alertClass", "alert alert-" + alertType).addObject("alertMessage", messageText);
    }
}
