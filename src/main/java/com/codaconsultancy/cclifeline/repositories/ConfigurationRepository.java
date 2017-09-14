package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.domain.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

    Configuration findByName(String name);
}

