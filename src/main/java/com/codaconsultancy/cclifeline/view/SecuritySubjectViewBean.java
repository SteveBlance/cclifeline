package com.codaconsultancy.cclifeline.view;

import com.codaconsultancy.cclifeline.domain.SecuritySubject;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;


public class SecuritySubjectViewBean {

    private Long id;

    private String forename;
    private String surname;
    private String username;
    private String previousPassword;
    private String password;
    private String confirmPassword;
    private int failedLoginAttempts;
    private boolean accountLocked;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPreviousPassword() {
        return previousPassword;
    }

    public void setPreviousPassword(String previousPassword) {
        this.previousPassword = previousPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public SecuritySubject toEntity() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        return mapper.map(this, SecuritySubject.class);
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }
}
