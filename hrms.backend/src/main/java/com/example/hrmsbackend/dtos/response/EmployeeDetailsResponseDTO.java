package com.example.hrmsbackend.dtos.response;

import com.example.hrmsbackend.entities.Designation;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDetailsResponseDTO {
    private Long id;

    private String name;

    private String email;

    private Designation designation;

    private MediaResponseDTO profileMedia;

    private LocalDateTime createdAt;

    private EmployeeManagerDetailsResponseDTO manager;

    private List<EmployeeSummaryDTO> directReports;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public MediaResponseDTO getProfileMedia() {
        return profileMedia;
    }

    public void setProfileMedia(MediaResponseDTO profileMedia) {
        this.profileMedia = profileMedia;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<EmployeeSummaryDTO> getDirectReports() {
        return directReports;
    }

    public void setDirectReports(List<EmployeeSummaryDTO> directReports) {
        this.directReports = directReports;
    }

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public EmployeeManagerDetailsResponseDTO getManager() {
        return manager;
    }

    public void setManager(EmployeeManagerDetailsResponseDTO manager) {
        this.manager = manager;
    }
}
