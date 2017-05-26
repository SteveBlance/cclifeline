package com.codaconsultancy.cclifeline.repositories;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BindingResult;

@RunWith(SpringRunner.class)
@DataJpaTest
@EntityScan("com.codaconsultancy.cclifeline.domain")
public abstract class BaseTest {

    @Autowired
    protected TestEntityManager entityManager;

    protected BindingResult getBindingResult(final String objectName) {
        return new AbstractBindingResult(objectName) {
            @Override
            public Object getTarget() {
                return null;
            }

            @Override
            protected Object getActualFieldValue(String s) {
                return null;
            }
        };
    }
}
