package com.codaconsultancy.cclifeline.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SecuritySubjectTest {

    private SecuritySubject securitySubject;

    @Before
    public void setUp() throws Exception {
        securitySubject = new SecuritySubject();
        securitySubject.setId(93L);
        securitySubject.setForename("Ross");
        securitySubject.setSurname("Lindsay");
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

}