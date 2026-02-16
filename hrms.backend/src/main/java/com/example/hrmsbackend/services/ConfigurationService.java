package com.example.hrmsbackend.services;

import com.example.hrmsbackend.dtos.Create;
import com.example.hrmsbackend.dtos.Update;
import com.example.hrmsbackend.dtos.request.ConfigurationRequestDTO;
import com.example.hrmsbackend.dtos.request.CustomUserDetails;
import com.example.hrmsbackend.dtos.response.ConfigurationResponseDTO;
import com.example.hrmsbackend.entities.Configuration;
import com.example.hrmsbackend.entities.Employee;
import com.example.hrmsbackend.exceptions.ResourceNotFoundException;
import com.example.hrmsbackend.mappers.EntityMapper;
import com.example.hrmsbackend.repos.ConfigurationRepo;
import com.example.hrmsbackend.repos.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class ConfigurationService {
    private ConfigurationRepo configurationRepo;
    private EmployeeRepo employeeRepo;
    private EntityMapper entityMapper;

    @Autowired
    public ConfigurationService(ConfigurationRepo configurationRepo, EmployeeRepo employeeRepo, EntityMapper entityMapper) {
        this.configurationRepo = configurationRepo;
        this.employeeRepo = employeeRepo;
        this.entityMapper = entityMapper;
    }

    public List<ConfigurationResponseDTO> getAllConfigurations() {
        List<Configuration> configurations = configurationRepo.findAll();
        return entityMapper.toConfigurationResponseDTOList(configurations);
    }

    public ConfigurationResponseDTO createConfiguration(@Validated(Create.class) ConfigurationRequestDTO configurationRequestDTO, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (configurationRepo.existsByConfigKey(configurationRequestDTO.getConfigKey())) {
            throw new IllegalArgumentException("Configuration with key '" + configurationRequestDTO.getConfigKey() + "' already exists");
        }

        Employee employee = employeeRepo.findByEmail(currentUser.getUsername());

        Configuration configuration = entityMapper.toConfiguration(configurationRequestDTO);
        configuration.setUpdatedBy(employee);
        Configuration savedConfiguration = configurationRepo.save(configuration);
        return entityMapper.toConfigurationResponseDTO(savedConfiguration);
    }

    @Transactional
    public ConfigurationResponseDTO updateConfiguration(String key, @Validated(Update.class) ConfigurationRequestDTO configurationRequestDTO, @AuthenticationPrincipal CustomUserDetails currentUser) {
        Configuration existingConfiguration = configurationRepo.findByConfigKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("Configuration not found with key: " + key));

        Employee employee = employeeRepo.findByEmail(currentUser.getUsername());

        existingConfiguration.setConfigValue(configurationRequestDTO.getConfigValue());
        existingConfiguration.setUpdatedBy(employee);
        Configuration updatedConfiguration = configurationRepo.save(existingConfiguration);
        return entityMapper.toConfigurationResponseDTO(updatedConfiguration);
    }

    public ConfigurationResponseDTO getConfigurationByKey(String key) {
        Configuration existingConfiguration = configurationRepo.findByConfigKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("Configuration not found with key: " + key));

        return entityMapper.toConfigurationResponseDTO(existingConfiguration);
    }
}
