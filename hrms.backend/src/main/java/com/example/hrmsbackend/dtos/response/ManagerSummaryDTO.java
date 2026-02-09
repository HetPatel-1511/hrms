package com.example.hrmsbackend.dtos.response;

import com.example.hrmsbackend.entities.Designation;
import com.example.hrmsbackend.entities.Media;

public class ManagerSummaryDTO {
    private Long id;
    private String name;
    private String email;
    private Designation designation;
    private Media profileMedia;

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

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public Media getProfileMedia() {
        return profileMedia;
    }

    public void setProfileMedia(Media profileMedia) {
        this.profileMedia = profileMedia;
    }
}