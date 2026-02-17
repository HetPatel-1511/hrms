package com.example.hrmsbackend.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class ShareJobRequestDTO {
    @NotNull(message = "Recipient email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
