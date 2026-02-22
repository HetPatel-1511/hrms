package com.example.hrmsbackend.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class PostUpdateRequestDTO {
    @NotBlank
    @Size(max = 255)
    private String title;

    @Size(max = 5000)
    private String description;

    private List<String> tags;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
