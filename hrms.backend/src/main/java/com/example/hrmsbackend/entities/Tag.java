package com.example.hrmsbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_tag_id")
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "tag", nullable = false)
    private String tag;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostTag> postTags;

    public Long getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<PostTag> getPostTags() {
        return postTags;
    }

    public void setPostTags(List<PostTag> postTags) {
        this.postTags = postTags;
    }
}
