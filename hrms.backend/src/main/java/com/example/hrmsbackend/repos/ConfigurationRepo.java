package com.example.hrmsbackend.repos;

import com.example.hrmsbackend.entities.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigurationRepo extends JpaRepository<Configuration, Long> {
    boolean existsByConfigKey(String configKey);
    Optional<Configuration> findByConfigKey(String configKey);
}
