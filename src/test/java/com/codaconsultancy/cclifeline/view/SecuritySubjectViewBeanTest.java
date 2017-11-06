package com.codaconsultancy.cclifeline.view;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SecuritySubjectViewBeanTest {

    private SecuritySubjectViewBean securitySubjectViewBean;

    @Before
    public void setUp() throws Exception {
        securitySubjectViewBean = new SecuritySubjectViewBean();
        securitySubjectViewBean.setForename("Ross");
        securitySubjectViewBean.setSurname("Lindsay");
        securitySubjectViewBean.setUsername("ross");
        securitySubjectViewBean.setPassword("passw0rD");
        securitySubjectViewBean.setConfirmPassword("passwirS");
        securitySubjectViewBean.setFailedLoginAttempts(3);
        securitySubjectViewBean.setAccountLocked(true);
        securitySubjectViewBean.setPreviousPassword("p455w0rd");
    }

    @Test
    public void getForename() {
        assertEquals("Ross", securitySubjectViewBean.getForename());
    }

    @Test
    public void getSurname() {
        assertEquals("Lindsay", securitySubjectViewBean.getSurname());
    }

    @Test
    public void getUsername() {
        assertEquals("ross", securitySubjectViewBean.getUsername());
    }

    @Test
    public void getPassword() {
        assertEquals("passw0rD", securitySubjectViewBean.getPassword());
    }

    @Test
    public void getConfirmPassword() {
        assertEquals("passwirS", securitySubjectViewBean.getConfirmPassword());
    }

    @Test
    public void getFailedLoginAttempts() {
        assertEquals(3, securitySubjectViewBean.getFailedLoginAttempts());
    }

    @Test
    public void isAccountLocked() {
        assertTrue(securitySubjectViewBean.isAccountLocked());
    }

    @Test
    public void getPreviousPassword() {
        assertEquals("p455w0rd", securitySubjectViewBean.getPreviousPassword());
    }

}