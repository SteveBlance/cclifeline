package com.codaconsultancy.cclifeline.view;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AdministratorViewBeanTest {

    private AdministratorViewBean administratorViewBean;

    @Before
    public void setUp() throws Exception {
        administratorViewBean = new AdministratorViewBean();
        administratorViewBean.setId(98L);
        administratorViewBean.setForename("Jim");
        administratorViewBean.setSurname("Jackson");
        administratorViewBean.setUsername("jjackson");
        administratorViewBean.setPassword("pwd123");
        administratorViewBean.setEmail("jacko@email.com");
        administratorViewBean.setLandlineNumber("01393 7665544");
        administratorViewBean.setMobileNumber("07766 1235544");
    }

    @Test
    public void getId() throws Exception {
        assertEquals(98L, administratorViewBean.getId().longValue());
    }

    @Test
    public void getForename() throws Exception {
        assertEquals("Jim", administratorViewBean.getForename());
    }

    @Test
    public void getSurname() throws Exception {
        assertEquals("Jackson", administratorViewBean.getSurname());
    }

    @Test
    public void getUsername() throws Exception {
        assertEquals("jjackson", administratorViewBean.getUsername());
    }

    @Test
    public void getPassword() throws Exception {
        assertEquals("pwd123", administratorViewBean.getPassword());
    }

    @Test
    public void getEmail() throws Exception {
        assertEquals("jacko@email.com", administratorViewBean.getEmail());
    }

    @Test
    public void getLandlineNumber() throws Exception {
        assertEquals("01393 7665544", administratorViewBean.getLandlineNumber());
    }

    @Test
    public void getMobileNumber() throws Exception {
        assertEquals("07766 1235544", administratorViewBean.getMobileNumber());
    }

}