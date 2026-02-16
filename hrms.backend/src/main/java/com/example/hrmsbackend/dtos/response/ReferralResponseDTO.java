package com.example.hrmsbackend.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReferralResponseDTO {
    private Long id;
    private JobOpeningResponseDTO jobOpening;
    private String name;
    private String email;
    private EmployeeSummaryDTO referredBy;
    private LocalDateTime createdAt;

    public JobOpeningResponseDTO getJobOpening() {
        return jobOpening;
    }

    public void setJobOpening(JobOpeningResponseDTO jobOpening) {
        this.jobOpening = jobOpening;
    }

    public EmployeeSummaryDTO getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(EmployeeSummaryDTO referredBy) {
        this.referredBy = referredBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
