package com.example.hrmsbackend.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class ShareJobRequestDTO {
    @NotNull(message = "Job opening ID cannot be null")
    private Long jobOpeningId;

    @NotNull(message = "Recipient email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

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
}
