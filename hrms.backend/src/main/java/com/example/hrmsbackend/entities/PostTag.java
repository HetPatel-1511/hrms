package com.example.hrmsbackend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "post_tags")
public class PostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_post_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_tag_id", nullable = false)
    private Tag tag;

    public Long getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
