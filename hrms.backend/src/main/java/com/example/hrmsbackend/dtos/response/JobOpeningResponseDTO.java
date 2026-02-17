package com.example.hrmsbackend.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobOpeningResponseDTO {
    private Long id;
    private String title;
    private String summary;
    private Boolean isActive;
    private EmployeeSummaryDTO hr;
    private MediaResponseDTO descriptionMedia;
    private List<EmployeeSummaryDTO> cvReviewers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public EmployeeSummaryDTO getHr() {
        return hr;
    }

    public void setHr(EmployeeSummaryDTO hr) {
        this.hr = hr;
    }

    public MediaResponseDTO getDescriptionMedia() {
        return descriptionMedia;
    }

    public void setDescriptionMedia(MediaResponseDTO descriptionMedia) {
        this.descriptionMedia = descriptionMedia;
    }

    public List<EmployeeSummaryDTO> getCvReviewers() {
        return cvReviewers;
    }

    public void setCvReviewers(List<EmployeeSummaryDTO> cvReviewers) {
        this.cvReviewers = cvReviewers;
    }
}
