package com.codaconsultancy.cclifeline.config;

import com.codaconsultancy.cclifeline.service.SecuritySubjectService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/resources/**", "css/**", "/dist/css/**", "/images/**",
                        "js/**", "less/**", "vendor/morrisjs/**",
                        "/vendor/bootstrap/css/**", "/vendor/bootstrap/fonts/**", "/vendor/bootstrap/js/**",
                        "/vendor/font-awesome/css/**", "/vendor/font-awesome/fonts/**", "/vendor/font-awesome/less/**", "/vendor/font-awesome/scss/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll();
        http.csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetailsService userDetailsServiceBean() {
        return new SecuritySubjectService();
    }


}
