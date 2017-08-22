package com.codaconsultancy.cclifeline.domain;

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
        securitySubject.setForename("Ross");
        securitySubject.setSurname("Lindsay");
        securitySubject.setUsername("ross");
        securitySubject.setPassword("passw0rD");
        securitySubject.setFailedLoginAttempts(3);
        securitySubject.setAccountLocked(true);
    }

    @Test
    public void getId() {
        assertEquals(93L, securitySubject.getId().longValue());
    }

    @Test
    public void getForename() {
        assertEquals("Ross", securitySubject.getForename());
    }

    @Test
    public void getSurname() {
        assertEquals("Lindsay", securitySubject.getSurname());
    }

    @Test
    public void getUsername() {
        assertEquals("ross", securitySubject.getUsername());
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

}