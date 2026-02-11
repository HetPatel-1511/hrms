package com.example.hrmsbackend.dtos.response;

import com.example.hrmsbackend.entities.DocumentType;
import com.example.hrmsbackend.entities.Media;
import com.example.hrmsbackend.entities.TravelPlanEmployee;

import java.time.LocalDateTime;

public class TravelDocumentResponseDTO {
    private Long id;
    private MediaResponseDTO media;
    private DocumentType documentType;
    private String ownerType;
    private LocalDateTime uploadedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public MediaResponseDTO getMedia() {
        return media;
    }

    public void setMedia(MediaResponseDTO media) {
        this.media = media;
    }
}
