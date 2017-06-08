package com.codaconsultancy.cclifeline.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.ModelAndView;

class LifelineController {
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
        return new ModelAndView(page).addObject("loggedInUser", loggedInUser());
    }
}
