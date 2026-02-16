package com.example.hrmsbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "job_opening_cv_reviewers")
public class JobOpeningCvReviewer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_job_opening_cv_reviewer_id")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_job_opening_id", nullable = false)
    private JobOpening jobOpening;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_reviewer_id", nullable = false)
    private Employee reviewer;

    public Long getId() {
        return id;
    }

    public JobOpening getJobOpening() {
        return jobOpening;
    }

    public void setJobOpening(JobOpening jobOpening) {
        this.jobOpening = jobOpening;
    }

    public Employee getReviewer() {
        return reviewer;
    }

    public void setReviewer(Employee reviewer) {
        this.reviewer = reviewer;
    }
}
