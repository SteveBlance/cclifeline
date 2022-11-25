package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.domain.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

    Configuration findByName(String name);
}

