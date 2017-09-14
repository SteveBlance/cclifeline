package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.domain.Configuration;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest(classes = Configuration.class)
public class ConfigurationRepositoryTest extends BaseTest {

    @Autowired
    private ConfigurationRepository configurationRepository;

    private Configuration configurationString;
    private Configuration configurationBoolean;
    private Configuration configurationDate;

    @Before
    public void setup() {
        configurationString = new Configuration();
        configurationString.setName("S1");
        configurationString.setStringValue("S1: ABC");

        configurationBoolean = new Configuration();
        configurationBoolean.setName("B1");
        configurationBoolean.setBooleanValue(true);

        configurationDate = new Configuration();
        configurationDate.setName("D1");
        configurationDate.setDateValue(new Date());

        entityManager.persist(configurationString);
        entityManager.persist(configurationBoolean);
        entityManager.persist(configurationDate);
    }

    @After
    public void teardown() {
        entityManager.remove(configurationString);
        entityManager.remove(configurationBoolean);
        entityManager.remove(configurationDate);
    }

    @Test
    public void findAll() throws Exception {
        assertEquals(3, configurationRepository.findAll().size());
    }

    @Test
    public void findStringByName() {
        Configuration foundConfig1 = configurationRepository.findByName("S1");
        assertEquals("S1: ABC", foundConfig1.getStringValue());
    }


    @Test
    public void findBooleanByName() {
        Configuration foundConfig1 = configurationRepository.findByName("B1");
        assertTrue(foundConfig1.getBooleanValue());
    }

    @Test
    public void findDateByName() {
        Configuration foundConfig1 = configurationRepository.findByName("D1");
        assertEquals(DateTime.now().dayOfYear(), new DateTime(foundConfig1.getDateValue()).dayOfYear());
    }

}