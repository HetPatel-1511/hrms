package com.example.hrmsbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "job_openings")
public class JobOpening {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_job_opening_id")
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Size(max = 5000)
    private String summary;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_hr_id", nullable = false)
    private Employee hr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_description_media_id")
    private Media descriptionMedia;

    @OneToMany(mappedBy = "jobOpening", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JobOpeningCvReviewer> cvReviewers;

    @OneToMany(mappedBy = "jobOpening", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SharedJob> sharedJobs;

    @OneToMany(mappedBy = "jobOpening", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Referal> referals;

    public Long getId() {
        return id;
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

    public Employee getHr() {
        return hr;
    }

    public void setHr(Employee hr) {
        this.hr = hr;
    }

    public Media getDescriptionMedia() {
        return descriptionMedia;
    }

    public void setDescriptionMedia(Media descriptionMedia) {
        this.descriptionMedia = descriptionMedia;
    }

    public List<JobOpeningCvReviewer> getCvReviewers() {
        return cvReviewers;
    }

    public void setCvReviewers(List<JobOpeningCvReviewer> cvReviewers) {
        this.cvReviewers = cvReviewers;
    }

    public List<SharedJob> getSharedJobs() {
        return sharedJobs;
    }

    public void setSharedJobs(List<SharedJob> sharedJobs) {
        this.sharedJobs = sharedJobs;
    }

    public List<Referal> getReferals() {
        return referals;
    }

    public void setReferals(List<Referal> referals) {
        this.referals = referals;
    }
}
