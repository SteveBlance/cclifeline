package com.codaconsultancy.cclifeline.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"com.codaconsultancy"})
@EntityScan("com.codaconsultancy.cclifeline.domain")
@EnableJpaRepositories("com.codaconsultancy.cclifeline.repositories")
public class SpringBootThymeleafApplicationWar extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootThymeleafApplicationWar.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootThymeleafApplicationWar.class, args);
    }
}
