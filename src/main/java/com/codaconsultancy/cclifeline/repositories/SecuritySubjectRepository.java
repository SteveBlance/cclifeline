package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.domain.SecuritySubject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecuritySubjectRepository extends JpaRepository<SecuritySubject, Long> {

    SecuritySubject findByUsername(String username);

    int countByUsername(String username);
}
