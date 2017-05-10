package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.common.TestHelper;
import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.repositories.BaseTest;
import com.codaconsultancy.cclifeline.service.MemberService;
import com.codaconsultancy.cclifeline.view.MemberViewBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@EnableJpaRepositories(basePackages = {"com.codaconsultancy.cclifeline.repositories"})
@SpringBootTest(classes = LifelineController.class)
public class LifelineControllerTest extends BaseTest {

    @Autowired
    LifelineController lifelineController;

    @MockBean
    MemberService memberService;

    @Test
    public void home() throws Exception {
        when(memberService.countAllMembers()).thenReturn(22L);

        ModelAndView response = lifelineController.home();

        verify(memberService, times(1)).countAllMembers();
        assertEquals(22L, response.getModel().get("memberCount"));
        assertEquals("index", response.getViewName());
    }

    @Test
    public void members() throws Exception {
        List<Member> members = new ArrayList<>();
        Member member1 = TestHelper.newMember(123L, "Bobby", "Smith", "bs@email.com", "01383 776655", "077665544", "Monthly", "Lifeline", "", "Open");
        Member member2 = TestHelper.newMember(124L, "Jane", "Wilkinson", "jw@email.com", "01383 414141", "077889900", "Monthly", "Lifeline", "", "Open");
        members.add(member1);
        members.add(member2);
        when(memberService.countAllMembers()).thenReturn(22L);
        when(memberService.findAllMembers()).thenReturn(members);

        ModelAndView response = lifelineController.members();

        verify(memberService, times(1)).findAllMembers();

        verify(memberService, times(1)).countAllMembers();
        assertEquals(22L, response.getModel().get("memberCount"));
        assertSame(members, response.getModel().get("members"));
        assertEquals("members", response.getViewName());
    }

    @Test
    public void getMemberDetails() throws Exception {
        long memberNumber = 1234L;
        Member member1234 = TestHelper.newMember(1234L, "Bobby", "Smith", "bs@email.com", "01383 776655", "077665544", "Monthly", "Lifeline", "", "Open");
        when(memberService.findMemberByMembershipNumber(1234L)).thenReturn(member1234);

        ModelAndView response = lifelineController.memberDetails(memberNumber);

        verify(memberService, times(1)).findMemberByMembershipNumber(1234L);
        assertEquals("Bobby", ((Member) response.getModel().get("member")).getForename());
        assertEquals("member", response.getViewName());

    }

    @Test
    public void addMember_success() {

        MemberViewBean memberViewBean = TestHelper.newMemberViewBean(2L, "Bobby", "Smith", "bs@email.com", "01383 776655", "077665544", "Monthly", "Lifeline", "", "Open");
        Member member = memberViewBean.toEntity();
        when(memberService.saveMember(any(Member.class))).thenReturn(member);
        BindingResult bindingResult = new AbstractBindingResult("member") {
            @Override
            public Object getTarget() {
                return null;
            }

            @Override
            protected Object getActualFieldValue(String s) {
                return null;
            }
        };
        ModelAndView modelAndView = lifelineController.addMember(memberViewBean, bindingResult);

        verify(memberService, times(1)).saveMember(any(Member.class));

        assertEquals("member", modelAndView.getViewName());
    }

    @Test
    public void addMember_validationErrors() {

        MemberViewBean memberViewBean = TestHelper.newMemberViewBean(2L, "Bobby", "Smith", "bs@email.com", "01383 776655", "077665544", "Monthly", "Lifeline", "", "Open");
        Member member = memberViewBean.toEntity();
        BindingResult bindingResult = new AbstractBindingResult("member") {
            @Override
            public Object getTarget() {
                return null;
            }

            @Override
            protected Object getActualFieldValue(String s) {
                return null;
            }
        };
        bindingResult.addError(new ObjectError("surname", "Surname cannot be blank"));
        ModelAndView modelAndView = lifelineController.addMember(memberViewBean, bindingResult);

        verify(memberService, never()).saveMember(member);

        assertEquals("add-member", modelAndView.getViewName());
    }

    @Test
    public void navigateToAddMember() {
        ModelAndView response = lifelineController.navigateToAddMember();
        assertEquals("add-member", response.getViewName());
        assertTrue(response.getModel().get("member") instanceof MemberViewBean);
    }

    @Test
    public void navigateToPayments() {
        assertEquals("payments", lifelineController.navigateToPayments().getViewName());
    }

    @Test
    public void navigateToAddPayment() {
        assertEquals("add-payment", lifelineController.navigateToAddPayment().getViewName());
    }

    @Test
    public void navigateToReports() {
        assertEquals("reports", lifelineController.navigateToReports().getViewName());
    }

    @Test
    public void navigateToWinners() {
        assertEquals("winners", lifelineController.navigateToWinners().getViewName());
    }

    @Test
    public void navigateMakeDraw() {
        assertEquals("make-draw", lifelineController.navigateMakeDraw().getViewName());
    }

    @Test
    public void navigateExportData() {
        assertEquals("export-data", lifelineController.navigateExportData().getViewName());
    }

    private HttpRequest getHttpRequest() {
        return new HttpRequest() {
            @Override
            public HttpMethod getMethod() {
                return HttpMethod.POST;
            }

            @Override
            public URI getURI() {
                try {
                    return new URI("http://localhost:8080");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public HttpHeaders getHeaders() {
                return new HttpHeaders();
            }
        };
    }

}