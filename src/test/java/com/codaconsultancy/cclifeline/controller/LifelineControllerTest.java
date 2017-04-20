package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.common.TestHelper;
import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.repositories.BaseTest;
import com.codaconsultancy.cclifeline.service.MemberService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@EnableJpaRepositories(basePackages = {"com.codaconsultancy.cclifeline.repositories"})
@SpringBootTest(classes = LifelineController.class)
public class LifelineControllerTest extends BaseTest {

    @Autowired
    LifelineController lifelineController;

    @MockBean
    MemberService memberService;


    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    /*
        public String home(Map<String, Object> model) {
        long count = memberService.countAllMembers();
        model.put("message", this.message);
        model.put("memberCount", count);
        List<Member> allMembers = memberService.findAllMembers();
        model.put("members", allMembers);
        return "index";
    }
     */

    @Test
    public void home() throws Exception {
        Map<String, Object> model = new HashMap<>();
        when(memberService.countAllMembers()).thenReturn(2L);
        List<Member> members = new ArrayList<>();
        Member member1 = TestHelper.newMember(123, "Bobby", "Smith", "bs@email.com", "01383 776655", "077665544", "Monthly", "Lifeline", "", "Open");
        Member member2 = TestHelper.newMember(124, "Jane", "Wilkinson", "jw@email.com", "01383 414141", "077889900", "Monthly", "Lifeline", "", "Open");
        members.add(member1);
        members.add(member2);
        when(memberService.findAllMembers()).thenReturn(members);

        lifelineController.home(model);

        verify(memberService, times(1)).countAllMembers();
        verify(memberService, times(1)).findAllMembers();
        assertEquals("Hello World", model.get("message"));
        assertEquals(2L, model.get("memberCount"));
        assertSame(members, model.get("members"));

    }

    @Test
    @Ignore("to do")
    public void member() throws Exception {
        //TODO: implement and increase PIT threshold
    }

}