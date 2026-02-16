package com.example.hrmsbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "referal")
public class Referal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_referal_id")
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_cv_media_id")
    private Media cvMedia;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_job_opening_id", nullable = false)
    private JobOpening jobOpening;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_referred_by_id", nullable = false)
    private Employee referredBy;

    public Long getId() {
        return id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Media getCvMedia() {
        return cvMedia;
    }

    public void setCvMedia(Media cvMedia) {
        this.cvMedia = cvMedia;
    }

    public JobOpening getJobOpening() {
        return jobOpening;
    }

    public void setJobOpening(JobOpening jobOpening) {
        this.jobOpening = jobOpening;
    }

    public Employee getReferedBy() {
        return referredBy;
    }

    public void setReferedBy(Employee referedBy) {
        this.referredBy = referedBy;
    }
}
