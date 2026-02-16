package com.example.hrmsbackend.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class ReferFriendRequestDTO {
    @NotNull(message = "Job opening cannot be null")
    private Long jobOpeningId;

    @NotNull(message = "Friend name cannot be null")
    @Size(max = 100)
    private String name;

    private String email;

    private MultipartFile cvMedia;

    public Long getJobOpeningId() {
        return jobOpeningId;
    }

    public void setJobOpeningId(Long jobOpeningId) {
        this.jobOpeningId = jobOpeningId;
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

    public MultipartFile getCvMedia() {
        return cvMedia;
    }

    public void setCvMedia(MultipartFile cvMedia) {
        this.cvMedia = cvMedia;
    }
}
