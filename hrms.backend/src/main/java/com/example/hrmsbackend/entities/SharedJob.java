package com.example.hrmsbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "shared_jobs")
public class SharedJob {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_shared_job_id")
    private Long id;

    @NotBlank
    @Email
    @Column(nullable = false)
    private String email;

    @NotNull
    @Column(name = "shared_at", nullable = false)
    private LocalDateTime sharedAt = LocalDateTime.now();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_job_opening_id", nullable = false)
    private JobOpening jobOpening;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_shared_by_id", nullable = false)
    private Employee sharedBy;

    public Long getId() {
        return id;
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

    public JobOpening getJobOpening() {
        return jobOpening;
    }

    public void setJobOpening(JobOpening jobOpening) {
        this.jobOpening = jobOpening;
    }

    public Employee getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(Employee sharedBy) {
        this.sharedBy = sharedBy;
    }
}
