package com.example.hrmsbackend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PostDeleteRequestDTO {
    private String remarks;

    public PostDeleteRequestDTO() {
    }

    public PostDeleteRequestDTO(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
