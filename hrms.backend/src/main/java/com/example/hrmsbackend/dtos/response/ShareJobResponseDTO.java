package com.example.hrmsbackend.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShareJobResponseDTO {
    private Long shareId;
    private Long jobOpeningId;
    private String email;
    private LocalDateTime sharedAt;
    private EmployeeSummaryDTO sharedBy;

    public Long getShareId() {
        return shareId;
    }

    public void setShareId(Long shareId) {
        this.shareId = shareId;
    }

    public Long getJobOpeningId() {
        return jobOpeningId;
    }

    public void setJobOpeningId(Long jobOpeningId) {
        this.jobOpeningId = jobOpeningId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getSharedAt() {
        return sharedAt;
    }

    public void setSharedAt(LocalDateTime sharedAt) {
        this.sharedAt = sharedAt;
    }

    public EmployeeSummaryDTO getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(EmployeeSummaryDTO sharedBy) {
        this.sharedBy = sharedBy;
    }
}
