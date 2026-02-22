package com.example.hrmsbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
@Where(clause = "is_deleted = false")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_post_id")
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String title;

    @Size(max = 5000)
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_author_id", nullable = false)
    private Employee author;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_deleted_by")
    private Employee deletedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_media_id")
    private Media media;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostTag> postTags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Like> likes;

    public Long getId() {
        return id;
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

    public Employee getAuthor() {
        return author;
    }

    public void setAuthor(Employee author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Employee getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(Employee deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public List<PostTag> getPostTags() {
        return postTags;
    }

    public void setPostTags(List<PostTag> postTags) {
        this.postTags = postTags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }
}
