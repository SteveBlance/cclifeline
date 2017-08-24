package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.SecuritySubject;
import com.codaconsultancy.cclifeline.exceptions.SubjectUsernameExistsException;
import com.codaconsultancy.cclifeline.repositories.SecuritySubjectRepository;
import com.codaconsultancy.cclifeline.view.SecuritySubjectViewBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecuritySubjectService extends LifelineService implements UserDetailsService {

    @Autowired
    private SecuritySubjectRepository securitySubjectRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<SecuritySubject> findAllSecuritySubjects() {
        return securitySubjectRepository.findAll();
    }

    public SecuritySubject registerNewSecuritySubject(SecuritySubjectViewBean securitySubjectViewBean) throws SubjectUsernameExistsException {
        if (usernameExists(securitySubjectViewBean.getUsername())) {
            throw new SubjectUsernameExistsException(
                    "There is an account with that username:" + securitySubjectViewBean.getUsername());
        }
        SecuritySubject securitySubject = new SecuritySubject();
        securitySubject.setForename(securitySubjectViewBean.getForename());
        securitySubject.setSurname(securitySubjectViewBean.getSurname());
        securitySubject.setUsername(securitySubjectViewBean.getUsername());

        securitySubject.setPassword(passwordEncoder.encode(securitySubjectViewBean.getPassword()));

        return securitySubjectRepository.save(securitySubject);
    }

    private boolean usernameExists(String username) {
        return securitySubjectRepository.countByUsername(username) != 0;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        SecuritySubject securitySubject = securitySubjectRepository.findByUsername(username);
        if (securitySubject == null) {
            return null;
        }

        List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

        String password = securitySubject.getPassword();
        boolean accountNonLocked = !securitySubject.isAccountLocked();
        UserDetails user = new User(
                username, password, true, true, true, accountNonLocked, auth);
        return user;
    }

    public void registerFailedLoginAttempt(String username) {
        SecuritySubject securitySubject = securitySubjectRepository.findByUsername(username);
        int failedLoginAttempts = securitySubject.getFailedLoginAttempts() + 1;
        logMessage("Failed login attempt (" + failedLoginAttempts + ") by user '" + username + "'");
        securitySubject.setFailedLoginAttempts(failedLoginAttempts);
        if (failedLoginAttempts == 4) {
            securitySubject.setAccountLocked(true);
        }
        securitySubjectRepository.save(securitySubject);
    }

    public void registerSuccessfulLogin(String username) {
        logMessage("User '" + username + "' logged in successfully");
        SecuritySubject securitySubject = securitySubjectRepository.findByUsername(username);
        int failedLoginAttempts = securitySubject.getFailedLoginAttempts();
        if (failedLoginAttempts > 0) {
            securitySubject.setFailedLoginAttempts(0);
            securitySubjectRepository.save(securitySubject);
        }
    }

    private void logMessage(String message) {
        eventLogRepository.save(new EventLog(message));
    }

    public void updatePassword(SecuritySubjectViewBean securitySubjectViewBean) {
        SecuritySubject securitySubject = securitySubjectRepository.getOne(securitySubjectViewBean.getId());
        securitySubject.setPassword(passwordEncoder.encode(securitySubjectViewBean.getPassword()));
        securitySubjectRepository.save(securitySubject);
    }

    public SecuritySubject findByUsername(String username) {
        return securitySubjectRepository.findByUsername(username);
    }
}