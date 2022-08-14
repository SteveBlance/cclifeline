package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.EventLog;
import com.codaconsultancy.cclifeline.domain.SecuritySubject;
import com.codaconsultancy.cclifeline.exceptions.SubjectUsernameExistsException;
import com.codaconsultancy.cclifeline.repositories.SecuritySubjectRepository;
import com.codaconsultancy.cclifeline.view.SecuritySubjectViewBean;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SecuritySubjectService.class)
public class SecuritySubjectServiceTest extends LifelineServiceTest {

    @Autowired
    private SecuritySubjectService securityService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private SecuritySubjectRepository securitySubjectRepository;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void findAllSecuritySubjects() throws Exception {

        List<SecuritySubject> securitySubjects = new ArrayList<>();
        SecuritySubject securitySubject1 = new SecuritySubject();
        securitySubject1.setForename("Jim");
        SecuritySubject securitySubject2 = new SecuritySubject();
        securitySubject2.setForename("Debbie");
        SecuritySubject securitySubject3 = new SecuritySubject();
        securitySubject3.setForename("Helen");
        securitySubjects.add(securitySubject1);
        securitySubjects.add(securitySubject2);
        securitySubjects.add(securitySubject3);
        when(securitySubjectRepository.findAll()).thenReturn(securitySubjects);

        List<SecuritySubject> foundSecuritySubjects = securityService.findAllSecuritySubjects();

        assertEquals(3, foundSecuritySubjects.size());
        assertEquals("Jim", foundSecuritySubjects.get(0).getForename());
        assertEquals("Debbie", foundSecuritySubjects.get(1).getForename());
        assertEquals("Helen", foundSecuritySubjects.get(2).getForename());
    }

    @Test
    public void registerNewSecuritySubject_success() throws Exception {
        SecuritySubjectViewBean securitySubjectViewBean = new SecuritySubjectViewBean();
        securitySubjectViewBean.setForename("Bobby");
        securitySubjectViewBean.setSurname("Smith");
        securitySubjectViewBean.setUsername("bobby");
        securitySubjectViewBean.setPassword("Pa55w0rd");
        securitySubjectViewBean.setConfirmPassword("Pa55w0rd");
        when(securitySubjectRepository.save(any(SecuritySubject.class))).thenReturn(securitySubjectViewBean.toEntity());
        when(securitySubjectRepository.countByUsername("bobby")).thenReturn(0);
        when(passwordEncoder.encode("Pa55w0rd")).thenReturn("EncodedPassword");

        SecuritySubject savedSubject = securityService.registerNewSecuritySubject(securitySubjectViewBean);

        ArgumentCaptor<SecuritySubject> securitySubjectArgumentCaptor = ArgumentCaptor.forClass(SecuritySubject.class);
        verify(securitySubjectRepository, times(1)).save(securitySubjectArgumentCaptor.capture());
        assertEquals("Bobby", savedSubject.getForename());
        assertEquals("Smith", savedSubject.getSurname());
        assertEquals("bobby", savedSubject.getUsername());
        assertEquals("Pa55w0rd", savedSubject.getPassword());

        assertEquals("bobby", securitySubjectArgumentCaptor.getValue().getUsername());
        assertEquals("Bobby", securitySubjectArgumentCaptor.getValue().getForename());
        assertEquals("Smith", securitySubjectArgumentCaptor.getValue().getSurname());
        assertEquals("EncodedPassword", securitySubjectArgumentCaptor.getValue().getPassword());

    }

    @Test
    public void registerNewSecuritySubject_failUsernameExists() throws Exception {

        expectedException.expect(SubjectUsernameExistsException.class);

        SecuritySubjectViewBean securitySubjectViewBean = new SecuritySubjectViewBean();
        securitySubjectViewBean.setForename("Bobby");
        securitySubjectViewBean.setSurname("Smith");
        securitySubjectViewBean.setUsername("bobby");
        securitySubjectViewBean.setPassword("pa55w0rd");
        when(securitySubjectRepository.save(any(SecuritySubject.class))).thenReturn(securitySubjectViewBean.toEntity());
        when(securitySubjectRepository.countByUsername("bobby")).thenReturn(1);
        when(passwordEncoder.encode("pa55w0rd")).thenReturn("EncodedPassword");

        securityService.registerNewSecuritySubject(securitySubjectViewBean);

        ArgumentCaptor<SecuritySubject> securitySubjectArgumentCaptor = ArgumentCaptor.forClass(SecuritySubject.class);
        verify(securitySubjectRepository, never()).save(securitySubjectArgumentCaptor.capture());

    }

    @Test
    public void loadUserByUsername_failUsernameNotFound() {
        when(securitySubjectRepository.findByUsername("shuggy")).thenReturn(null);

        UserDetails userDetails = securityService.loadUserByUsername("shuggy");

        verify(securitySubjectRepository, times(1)).findByUsername("shuggy");
        assertNull(userDetails);
    }

    @Test
    public void loadUserByUsername_success() {
        SecuritySubject shaggy = new SecuritySubject();
        shaggy.setUsername("shaggy");
        shaggy.setPassword("jenkinZ");
        shaggy.setAccountLocked(false);
        shaggy.setFailedLoginAttempts(1);
        when(securitySubjectRepository.findByUsername("shaggy")).thenReturn(shaggy);

        UserDetails userDetails = securityService.loadUserByUsername("shaggy");

        verify(securitySubjectRepository, times(1)).findByUsername("shaggy");
        assertNotNull(userDetails);
        assertEquals("shaggy", userDetails.getUsername());
        assertEquals("jenkinZ", userDetails.getPassword());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isEnabled());

    }

    @Test
    public void registerFailedLoginAttempt() {
        SecuritySubject bobby = new SecuritySubject();
        bobby.setFailedLoginAttempts(0);
        when(securitySubjectRepository.findByUsername("bobby")).thenReturn(bobby);
        assertFalse(bobby.isAccountLocked());

        securityService.registerFailedLoginAttempt("bobby");

        verify(securitySubjectRepository, times(1)).findByUsername("bobby");
        verify(securitySubjectRepository, times(1)).save(bobby);
        ArgumentCaptor<EventLog> eventLogArgumentCaptor = ArgumentCaptor.forClass(EventLog.class);
        verify(eventLogRepository, times(1)).save(eventLogArgumentCaptor.capture());
        assertEquals("Failed login attempt (1) by user 'bobby'", eventLogArgumentCaptor.getValue().getMessage());
        assertEquals(1, bobby.getFailedLoginAttempts());
        assertFalse(bobby.isAccountLocked());
    }

    @Test
    public void registerLastFailedLoginAttempt_accountLocked() {
        SecuritySubject bobby = new SecuritySubject();
        bobby.setFailedLoginAttempts(3);
        when(securitySubjectRepository.findByUsername("bobby")).thenReturn(bobby);
        assertFalse(bobby.isAccountLocked());

        securityService.registerFailedLoginAttempt("bobby");

        verify(securitySubjectRepository, times(1)).findByUsername("bobby");
        verify(securitySubjectRepository, times(1)).save(bobby);
        ArgumentCaptor<EventLog> eventLogArgumentCaptor = ArgumentCaptor.forClass(EventLog.class);
        verify(eventLogRepository, times(1)).save(eventLogArgumentCaptor.capture());
        assertEquals("Failed login attempt (4) by user 'bobby'", eventLogArgumentCaptor.getValue().getMessage());
        assertEquals(4, bobby.getFailedLoginAttempts());
        assertTrue(bobby.isAccountLocked());
    }

    @Test
    public void registerSuccessfulLoginAttempt_resetsFailureCount() {
        SecuritySubject bobby = new SecuritySubject();
        bobby.setFailedLoginAttempts(3);
        when(securitySubjectRepository.findByUsername("fred")).thenReturn(bobby);
        assertFalse(bobby.isAccountLocked());

        securityService.registerSuccessfulLogin("fred");

        verify(securitySubjectRepository, times(1)).findByUsername("fred");
        verify(securitySubjectRepository, times(1)).save(bobby);
        ArgumentCaptor<EventLog> eventLogArgumentCaptor = ArgumentCaptor.forClass(EventLog.class);
        verify(eventLogRepository, times(1)).save(eventLogArgumentCaptor.capture());
        assertEquals("User 'fred' logged in successfully", eventLogArgumentCaptor.getValue().getMessage());
        assertEquals(0, bobby.getFailedLoginAttempts());
        assertFalse(bobby.isAccountLocked());
    }

    @Test
    public void registerSuccessfulLoginAttempt_noPreviousFailures() {
        SecuritySubject bobby = new SecuritySubject();
        bobby.setFailedLoginAttempts(0);
        when(securitySubjectRepository.findByUsername("bobby")).thenReturn(bobby);
        assertFalse(bobby.isAccountLocked());

        securityService.registerSuccessfulLogin("bobby");

        verify(securitySubjectRepository, times(1)).findByUsername("bobby");
        verify(securitySubjectRepository, never()).save(bobby);
        assertEquals(0, bobby.getFailedLoginAttempts());
        assertFalse(bobby.isAccountLocked());
    }

    @Test
    public void updatePassword_success() throws Exception {
        Long subjectId = 567L;
        String oldPassword = "pa55word";
        String newPassword = "N3Wpa55word";
        SecuritySubjectViewBean securitySubjectViewBean = new SecuritySubjectViewBean();
        securitySubjectViewBean.setId(subjectId);
        securitySubjectViewBean.setUsername("jimbo");
        securitySubjectViewBean.setForename("Jim");
        securitySubjectViewBean.setSurname("Bowen");
        securitySubjectViewBean.setPreviousPassword(oldPassword);
        securitySubjectViewBean.setPassword(newPassword);
        securitySubjectViewBean.setConfirmPassword(newPassword);
        SecuritySubject securitySubject = securitySubjectViewBean.toEntity();
        securitySubject.setPasswordToBeChanged(true);
        securitySubject.setPassword(oldPassword);
        when(securitySubjectRepository.getOne(subjectId)).thenReturn(securitySubject);
        when(passwordEncoder.matches(oldPassword, oldPassword)).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("YaSwReHt&t%$");
        assertTrue(securitySubject.isPasswordToBeChanged());

        securityService.updatePassword(securitySubjectViewBean);

        verify(securitySubjectRepository, times(1)).getOne(subjectId);
        verify(passwordEncoder, times(1)).matches(oldPassword, oldPassword);
        verify(securitySubjectRepository, times(1)).save(securitySubject);
        assertFalse(securitySubject.isPasswordToBeChanged());
        assertEquals("YaSwReHt&t%$", securitySubject.getPassword());
    }

}