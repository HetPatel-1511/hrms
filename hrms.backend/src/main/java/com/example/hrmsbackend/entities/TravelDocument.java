package com.example.hrmsbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "travel_documents")
public class TravelDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_travel_document_id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "fk_travel_plan_employee_id", nullable = false)
    private TravelPlanEmployee travelPlanEmployee;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "fk_media_id", nullable = false)
    private Media media;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "fk_document_type_id", nullable = false)
    private DocumentType documentType;

    @NotBlank
    @Column(name = "owner_type")
    private String ownerType;

    @NotNull
    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public TravelPlanEmployee getTravelPlanEmployee() {
        return travelPlanEmployee;
    }

    public void setTravelPlanEmployee(TravelPlanEmployee travelPlanEmployee) {
        this.travelPlanEmployee = travelPlanEmployee;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
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
}
