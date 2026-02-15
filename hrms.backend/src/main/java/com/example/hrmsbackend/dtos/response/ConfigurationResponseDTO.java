package com.example.hrmsbackend.dtos.response;

import com.example.hrmsbackend.entities.Employee;

import java.time.LocalDateTime;

public class ConfigurationResponseDTO {
    
    private Long id;
    private String configKey;
    private String configValue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private EmployeeSummaryDTO updatedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public EmployeeSummaryDTO getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(EmployeeSummaryDTO updatedBy) {
        this.updatedBy = updatedBy;
    }
}
