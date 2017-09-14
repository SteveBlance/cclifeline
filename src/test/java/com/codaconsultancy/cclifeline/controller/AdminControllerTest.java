package com.codaconsultancy.cclifeline.controller;

import com.codaconsultancy.cclifeline.domain.SecuritySubject;
import com.codaconsultancy.cclifeline.exceptions.SubjectPasswordIncorrectException;
import com.codaconsultancy.cclifeline.exceptions.SubjectUsernameExistsException;
import com.codaconsultancy.cclifeline.repositories.BaseTest;
import com.codaconsultancy.cclifeline.service.NotificationService;
import com.codaconsultancy.cclifeline.view.SecuritySubjectViewBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static com.codaconsultancy.cclifeline.controller.AdminController.CHANGE_PASSWORD_PAGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdminController.class)
public class AdminControllerTest extends BaseTest {

    @Autowired
    private AdminController adminController;

    @MockBean
    NotificationService notificationService;

    @Test
    public void navigateToAdministrators() throws Exception {
        List<SecuritySubject> administrators = new ArrayList<>();
        SecuritySubject ross = new SecuritySubject();
        ross.setUsername("ross");
        SecuritySubject steve = new SecuritySubject();
        administrators.add(ross);
        administrators.add(steve);
        when(securitySubjectService.findAllSecuritySubjects()).thenReturn(administrators);
        when(securitySubjectService.findByUsername(any(String.class))).thenReturn(new SecuritySubject());


        ModelAndView modelAndView = adminController.navigateToAdministrators();

        assertEquals("administrators", modelAndView.getViewName());
        List foundAdministrators = (List) modelAndView.getModel().get("administrators");
        assertEquals(2, foundAdministrators.size());
        assertEquals("ross", ((SecuritySubject) foundAdministrators.get(0)).getUsername());
    }

    @Test
    public void navigateToAddAdministrator() throws Exception {
        when(securitySubjectService.findByUsername(any(String.class))).thenReturn(new SecuritySubject());
        ModelAndView modelAndView = adminController.navigateToAddAdministrator();
        assertTrue(modelAndView.getModel().get("administrator") instanceof SecuritySubjectViewBean);
        assertEquals("add-administrator", modelAndView.getViewName());
    }

    @Test
    public void addNewAdministrator_success() throws Exception {
        SecuritySubjectViewBean securitySubjectViewBean = new SecuritySubjectViewBean();

        ModelAndView modelAndView = adminController.addNewAdministrator(securitySubjectViewBean, getBindingResult("subject"));

        verify(securitySubjectService, times(1)).registerNewSecuritySubject(securitySubjectViewBean);
        assertEquals("administrators", modelAndView.getViewName());
    }

    @Test
    public void addNewAdministrator_failUserExists() throws Exception {
        SecuritySubjectViewBean securitySubjectViewBean = new SecuritySubjectViewBean();
        when(securitySubjectService.registerNewSecuritySubject(securitySubjectViewBean)).thenThrow(new SubjectUsernameExistsException("Username already exists"));

        ModelAndView modelAndView = adminController.addNewAdministrator(securitySubjectViewBean, getBindingResult("subject"));

        verify(securitySubjectService, times(1)).registerNewSecuritySubject(securitySubjectViewBean);
        assertEquals("add-administrator", modelAndView.getViewName());
        String alertMessage = (String) modelAndView.getModel().get("alertMessage");
        assertEquals("Username already exists", alertMessage);
        String alertClass = (String) modelAndView.getModel().get("alertClass");
        assertEquals("alert alert-danger", alertClass);
    }

    @Test
    public void addNewAdministrator_failPasswordAndConfirmMismatch() throws Exception {
        SecuritySubjectViewBean securitySubjectViewBean = new SecuritySubjectViewBean();
        when(securitySubjectService.registerNewSecuritySubject(securitySubjectViewBean)).thenThrow(new SubjectPasswordIncorrectException("Password must match confirmation"));

        ModelAndView modelAndView = adminController.addNewAdministrator(securitySubjectViewBean, getBindingResult("subject"));

        verify(securitySubjectService, times(1)).registerNewSecuritySubject(securitySubjectViewBean);
        assertEquals("add-administrator", modelAndView.getViewName());
        String alertMessage = (String) modelAndView.getModel().get("alertMessage");
        assertEquals("Password must match confirmation", alertMessage);
        String alertClass = (String) modelAndView.getModel().get("alertClass");
        assertEquals("alert alert-danger", alertClass);
    }

    @Test
    public void addNewAdministrator__validationErrors() throws Exception {
        BindingResult bindingResult = getBindingResult("administrator");
        bindingResult.addError(new ObjectError("administrator", "Problem with administrator"));
        SecuritySubjectViewBean securitySubjectViewBean = new SecuritySubjectViewBean();


        ModelAndView modelAndView = adminController.addNewAdministrator(securitySubjectViewBean, bindingResult);

        verify(securitySubjectService, never()).registerNewSecuritySubject(securitySubjectViewBean);

        assertEquals("add-administrator", modelAndView.getViewName());
    }

    @Test
    public void navigateToChangePassword() {

        SecuritySubject dave = new SecuritySubject();
        dave.setForename("David");
        when(securitySubjectService.findByUsername("dave")).thenReturn(dave);

        ModelAndView modelAndView = adminController.navigateToChangePassword("dave");

        SecuritySubjectViewBean user = (SecuritySubjectViewBean) modelAndView.getModel().get("user");
        assertEquals("David", user.getForename());
        assertEquals(CHANGE_PASSWORD_PAGE, modelAndView.getViewName());
    }

    @Test
    public void changePassword_success() throws Exception {
        BindingResult bindingResult = getBindingResult("user");
        SecuritySubjectViewBean subjectViewBean = new SecuritySubjectViewBean();

        ModelAndView modelAndView = adminController.changePassword(subjectViewBean, bindingResult);

        verify(securitySubjectService, times(1)).updatePassword(subjectViewBean);

        assertEquals("index", modelAndView.getViewName());
    }

    @Test
    public void changePassword_failInvalidPassword() throws Exception {
        BindingResult bindingResult = getBindingResult("user");
        SecuritySubjectViewBean subjectViewBean = new SecuritySubjectViewBean();
        subjectViewBean.setUsername("Dave");
        doThrow(new SubjectPasswordIncorrectException("The New Password must not be the same as the Current Password")).when(securitySubjectService).updatePassword(subjectViewBean);
        when(securitySubjectService.findByUsername("Dave")).thenReturn(subjectViewBean.toEntity());

        ModelAndView modelAndView = adminController.changePassword(subjectViewBean, bindingResult);

        verify(securitySubjectService, times(1)).updatePassword(subjectViewBean);

        assertEquals("change-password", modelAndView.getViewName());
        assertEquals("The New Password must not be the same as the Current Password", modelAndView.getModel().get("alertMessage"));
        assertEquals("alert alert-danger", modelAndView.getModel().get("alertClass"));
    }

//
//    @Test
//    public void encryptPassword() {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String encodedPassword = encoder.encode("password");
//        assertEquals("", encodedPassword);
//    }

}