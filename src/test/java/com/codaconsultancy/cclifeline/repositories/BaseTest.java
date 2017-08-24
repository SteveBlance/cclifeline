package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.service.SecuritySubjectService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@DataJpaTest
@EntityScan("com.codaconsultancy.cclifeline.domain")
public abstract class BaseTest {

    @Autowired
    protected TestEntityManager entityManager;

    @MockBean
    protected SecuritySubjectService securitySubjectService;

    @Before
    public void onSetUp() {
        User user = new User("Bob", "password123", new ArrayList<>());
        Authentication auth = new UsernamePasswordAuthenticationToken(user, "password");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

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
