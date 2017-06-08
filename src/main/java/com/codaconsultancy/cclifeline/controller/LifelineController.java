package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.Notification;
import com.codaconsultancy.cclifeline.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
class LifelineController {

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
        List<Notification> notifications = notificationService.fetchLatestNotifications();

        return new ModelAndView(page).addObject("loggedInUser", loggedInUser())
                .addObject("notifications", notifications);
    }
}
