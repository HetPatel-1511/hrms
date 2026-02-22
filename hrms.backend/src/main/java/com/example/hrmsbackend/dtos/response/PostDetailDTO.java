package com.example.hrmsbackend.dtos.response;

import java.time.LocalDateTime;
import java.util.List;

public class PostDetailDTO {
    private Long id;
    private String title;
    private String description;
    private EmployeeSummaryDTO author;
    private LocalDateTime createdAt;
    private List<TagDTO> tags;
    private MediaResponseDTO media;
    private Integer likeCount;
    private Integer commentCount;
    private java.util.List<CommentDTO> comments;
    private Boolean isLiked = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public EmployeeSummaryDTO getAuthor() {
        return author;
    }

    public void setAuthor(EmployeeSummaryDTO author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }

    public MediaResponseDTO getMedia() {
        return media;
    }

    public void setMedia(MediaResponseDTO media) {
        this.media = media;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
    }

    public java.util.List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(java.util.List<CommentDTO> comments) {
        this.comments = comments;
    }
}
