package com.codaconsultancy.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class LifelineController {

    @RequestMapping("/")
    @ResponseBody
    public String home() {
        return "home";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(LifelineController.class, args);
    }

}
