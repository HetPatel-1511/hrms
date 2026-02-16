package com.example.hrmsbackend.controllers;

import com.example.hrmsbackend.dtos.Create;
import com.example.hrmsbackend.dtos.Update;
import com.example.hrmsbackend.dtos.request.ConfigurationRequestDTO;
import com.example.hrmsbackend.dtos.request.CustomUserDetails;
import com.example.hrmsbackend.dtos.response.ApiResponse;
import com.example.hrmsbackend.dtos.response.ConfigurationResponseDTO;
import com.example.hrmsbackend.services.ConfigurationService;
import com.example.hrmsbackend.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/configurations")
@Validated
public class ConfigurationController {
    private ConfigurationService configurationService;

    @Autowired
    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ConfigurationResponseDTO>> createConfiguration(@Validated(Create.class) @RequestBody ConfigurationRequestDTO configurationRequestDTO, @AuthenticationPrincipal CustomUserDetails currentUser) {
        ConfigurationResponseDTO createdConfiguration = configurationService.createConfiguration(configurationRequestDTO, currentUser);
        return ResponseEntity.ok(ResponseUtil.success(createdConfiguration, "Configuration created successfully", 201));
    }

    @GetMapping("/{key}")
    public ResponseEntity<ApiResponse<ConfigurationResponseDTO>> getConfigurationByKey(@PathVariable String key) {
        ConfigurationResponseDTO configuration = configurationService.getConfigurationByKey(key);
        return ResponseEntity.ok(ResponseUtil.success(configuration, "Configuration fetched successfully", 200));
    }

    @PutMapping("/{key}")
    public ResponseEntity<ApiResponse<ConfigurationResponseDTO>> updateConfiguration(@PathVariable String key, @Validated(Update.class) @RequestBody ConfigurationRequestDTO configurationRequestDTO, @AuthenticationPrincipal CustomUserDetails currentUser) {
        ConfigurationResponseDTO updatedConfiguration = configurationService.updateConfiguration(key, configurationRequestDTO, currentUser);
        return ResponseEntity.ok(ResponseUtil.success(updatedConfiguration, "Configuration updated successfully", 200));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ConfigurationResponseDTO>>> getAllConfigurations() {
        List<ConfigurationResponseDTO> configurations = configurationService.getAllConfigurations();
        return ResponseEntity.ok(ResponseUtil.success(configurations, "Configurations fetched successfully", 200));
    }
}
