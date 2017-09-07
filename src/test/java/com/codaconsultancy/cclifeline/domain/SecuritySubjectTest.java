package com.codaconsultancy.cclifeline.domain;

import com.codaconsultancy.cclifeline.view.SecuritySubjectViewBean;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SecuritySubjectTest {

    private SecuritySubject securitySubject;

    @Before
    public void setUp() throws Exception {
        securitySubject = new SecuritySubject();
        securitySubject.setId(93L);
        securitySubject.setForename("Bill");
        securitySubject.setSurname("Lindsay");
        securitySubject.setUsername("bill");
        securitySubject.setPassword("passw0rD");
        securitySubject.setFailedLoginAttempts(3);
        securitySubject.setAccountLocked(true);
        securitySubject.setPasswordToBeChanged(true);
    }

    @Test
    public void getId() {
        assertEquals(93L, securitySubject.getId().longValue());
    }

    @Test
    public void getForename() {
        assertEquals("Bill", securitySubject.getForename());
    }

    @Test
    public void getSurname() {
        assertEquals("Lindsay", securitySubject.getSurname());
    }

    @Test
    public void getUsername() {
        assertEquals("bill", securitySubject.getUsername());
    }

    @Test
    public void getPassword() {
        assertEquals("passw0rD", securitySubject.getPassword());
    }

    @Test
    public void getFailedLoginAttempts() {
        assertEquals(3, securitySubject.getFailedLoginAttempts());
    }

    @Test
    public void isAccountLocked() {
        assertTrue(securitySubject.isAccountLocked());
    }

    @Test
    public void isPasswordToBeChanged() {
        assertTrue(securitySubject.isPasswordToBeChanged());
    }

    @Test
    public void toViewBean() {
        SecuritySubjectViewBean securitySubjectViewBean = securitySubject.toViewBean();
        assertEquals("bill", securitySubjectViewBean.getUsername());
        assertEquals("Bill", securitySubjectViewBean.getForename());
        assertEquals("Lindsay", securitySubjectViewBean.getSurname());
        assertEquals("passw0rD", securitySubjectViewBean.getPassword());
        assertEquals(93L, securitySubjectViewBean.getId().longValue());
        assertTrue(securitySubjectViewBean.isAccountLocked());
    }

}