package com.example.hrmsbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "media")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_media_id")
    private Long id;

    @NotBlank
    @Column(name = "mime_type")
    private String mimeType;

    @NotBlank
    @Column(name = "original_name")
    private String originalName;

    @NotBlank
    @Column(name = "file_name")
    private String fileName;

    @NotBlank
    private String url;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "uploaded_by", nullable = false)
    private Employee uploadedBy;

    @NotNull
    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public Long getId() {
        return id;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Employee getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(Employee uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
