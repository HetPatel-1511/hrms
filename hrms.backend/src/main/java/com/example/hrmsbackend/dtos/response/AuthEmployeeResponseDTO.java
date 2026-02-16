package com.example.hrmsbackend.dtos.response;

import com.example.hrmsbackend.entities.*;
import com.fasterxml.jackson.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AuthEmployeeResponseDTO {
    private Long id;

    private String name;

    private String email;

    private LocalDate birthDate;

    private LocalDate joiningDate;

    private Boolean active;

    private LocalDateTime createdAt;

    private Designation designation;

    private MediaResponseDTO profileMedia;

    private EmployeeSummaryDTO manager;

    private List<RoleResponseDTO> roles;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String accessToken;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String refreshToken;

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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public EmployeeSummaryDTO getManager() {
        return manager;
    }

    public void setManager(EmployeeSummaryDTO manager) {
        this.manager = manager;
    }

    public List<RoleResponseDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleResponseDTO> roles) {
        this.roles = roles;
    }

    public MediaResponseDTO getProfileMedia() {
        return profileMedia;
    }

    public void setProfileMedia(MediaResponseDTO profileMedia) {
        this.profileMedia = profileMedia;
    }
}

