package com.example.hrmsbackend.dtos.response;

import java.time.LocalDateTime;
import java.util.List;

public class CommentDTO {
    private Long id;
    private String text;
    private EmployeeSummaryDTO employee;
    private LocalDateTime createdAt;
    private List<CommentDTO> replies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public EmployeeSummaryDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeSummaryDTO employee) {
        this.employee = employee;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<CommentDTO> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentDTO> replies) {
        this.replies = replies;
    }
}
