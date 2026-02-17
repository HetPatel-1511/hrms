package com.example.hrmsbackend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class JobOpeningRequestDTO {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Summary is required")
    private String summary;

    @NotNull(message = "Description media is required")
    private MultipartFile descriptionMedia;

    private List<Long> cvReviewerIds;

    @NotNull(message = "HR ID is required")
    private Long hrId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public MultipartFile getDescriptionMedia() {
        return descriptionMedia;
    }

    public void setDescriptionMedia(MultipartFile descriptionMedia) {
        this.descriptionMedia = descriptionMedia;
    }

    public List<Long> getCvReviewerIds() {
        return cvReviewerIds;
    }

    public void setCvReviewerIds(List<Long> cvReviewerIds) {
        this.cvReviewerIds = cvReviewerIds;
    }

    public Long getHrId() {
        return hrId;
    }

    public void setHrId(Long hrId) {
        this.hrId = hrId;
    }
}
