package com.codaconsultancy.cclifeline.domain;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfigurationTest {

    private Configuration stringConfig;
    private Configuration dateConfig;
    private Configuration booleanConfig;

    @Before
    public void setup() {
        stringConfig = new Configuration();
        stringConfig.setId(1L);
        stringConfig.setName("REFRESH_TYPE");
        stringConfig.setStringValue("IMMEDIATE");

        dateConfig = new Configuration();
        dateConfig.setId(2L);
        dateConfig.setName("REFRESH_DATE");
        dateConfig.setDateValue(DateTime.now().toDate());

        booleanConfig = new Configuration();
        booleanConfig.setId(3L);
        booleanConfig.setName("REFRESH_REQUIRED");
        booleanConfig.setBooleanValue(true);
    }

    @Test
    public void getId() throws Exception {
        assertEquals(1L, stringConfig.getId().longValue());
        assertEquals(2L, dateConfig.getId().longValue());
        assertEquals(3L, booleanConfig.getId().longValue());
    }

    @Test
    public void getName() throws Exception {
        assertEquals("REFRESH_TYPE", stringConfig.getName());
        assertEquals("REFRESH_DATE", dateConfig.getName());
        assertEquals("REFRESH_REQUIRED", booleanConfig.getName());
    }

    @Test
    public void getStringValue() throws Exception {
        assertEquals("IMMEDIATE", stringConfig.getStringValue());

    }

    @Test
    public void getBooleanValue() throws Exception {
        assertTrue(booleanConfig.getBooleanValue());
    }

    @Test
    public void getDateValue() throws Exception {
        assertEquals(DateTime.now().dayOfYear(), new DateTime(dateConfig.getDateValue()).dayOfYear());
    }

}